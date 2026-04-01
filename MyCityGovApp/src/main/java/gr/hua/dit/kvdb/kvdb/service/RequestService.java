package gr.hua.dit.kvdb.kvdb.service;

import gr.hua.dit.kvdb.kvdb.domain.request.Request;
import gr.hua.dit.kvdb.kvdb.domain.request.RequestStatus;
import gr.hua.dit.kvdb.kvdb.domain.service.MunicipalService;
import gr.hua.dit.kvdb.kvdb.domain.user.User;
import gr.hua.dit.kvdb.kvdb.dto.RequestDTO;
import gr.hua.dit.kvdb.kvdb.repository.MunicipalServiceRepository;
import gr.hua.dit.kvdb.kvdb.repository.RequestRepository;
import gr.hua.dit.kvdb.kvdb.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import gr.hua.dit.kvdb.kvdb.domain.request.Attachment;
import gr.hua.dit.kvdb.kvdb.repository.AttachmentRepository;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
public class RequestService {

    private final RequestRepository requestRepository;
    private final UserRepository userRepository;
    private final MunicipalServiceRepository serviceRepository;
    private final AttachmentRepository attachmentRepository;

    //email
    private final EmailService emailService;

    public RequestService(RequestRepository requestRepository,
                          UserRepository userRepository, MunicipalServiceRepository serviceRepository, AttachmentRepository attachmentRepository, EmailService emailService) {
        this.requestRepository = requestRepository;
        this.userRepository = userRepository;
        this.serviceRepository = serviceRepository;
        this.attachmentRepository = attachmentRepository;
        this.emailService = emailService;

    }

    @Transactional
    public RequestDTO createRequestWithFiles(RequestDTO dto, List<MultipartFile> files) {
        // 1. Δημιουργία και αποθήκευση του βασικού Request
        User citizen = userRepository.findById(dto.getCitizenId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        MunicipalService service = serviceRepository.findById(dto.getServiceId())
                .orElseThrow(() -> new RuntimeException("Service not found"));

        String protocol = "REQ-" + UUID.randomUUID();

        Request request = new Request();
        request.setProtocolNumber(protocol);
        request.setCitizen(citizen);
        request.setService(service);
        request.setTitle(dto.getTitle());
        request.setDescription(dto.getDescription());
        request.setStatus(RequestStatus.SUBMITTED);
        request.setSubmittedAt(LocalDateTime.now());
        request.setLastUpdatedAt(LocalDateTime.now());
        request.setSlaDays(service.getSlaDays());

        Request savedRequest = requestRepository.save(request);

        // 2. Διαχείριση Αρχείων
        String uploadDir = Paths.get("uploads").toAbsolutePath().toString() + "/";

        if (files != null && !files.isEmpty()) {

            for (MultipartFile file : files) {
                if (!file.isEmpty()) {
                    try {
                        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
                        Path path = Paths.get(uploadDir + fileName);

                        Files.createDirectories(path.getParent());

                        System.out.println("Trying to save file: " + path.toAbsolutePath());
                        Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
                        System.out.println("File saved!");

                        Attachment attachment = new Attachment();
                        attachment.setFileName(file.getOriginalFilename());
                        attachment.setFilePath(path.toString());
                        attachment.setFileType(file.getContentType());
                        attachment.setRequest(savedRequest);
                        attachmentRepository.save(attachment);

                    } catch (IOException e) {
                        e.printStackTrace();
                        throw new RuntimeException("Could not store file: " + file.getOriginalFilename(), e);
                    }
                }
            }
        } else {
            System.out.println("No files received!");
        }


        sendConfirmationEmail(citizen, savedRequest, service);

        return mapToDTO(savedRequest);
    }

    /**
     * νέο αίτημα για έναν πολίτη.
     */
    @Transactional
    public RequestDTO createRequest(RequestDTO dto) {

        User citizen = userRepository.findById(dto.getCitizenId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        MunicipalService service = serviceRepository.findById(dto.getServiceId())
                .orElseThrow(() -> new RuntimeException("Service not found"));

        String protocol = "REQ-" + UUID.randomUUID();

        Request request = new Request();
        request.setProtocolNumber(protocol);
        request.setCitizen(citizen);
        request.setService(service);
        request.setTitle(dto.getTitle());
        request.setDescription(dto.getDescription());
        request.setStatus(RequestStatus.SUBMITTED);
        request.setSubmittedAt(LocalDateTime.now());
        request.setLastUpdatedAt(LocalDateTime.now());
        request.setSlaDays(service.getSlaDays());




        Request savedRequest = requestRepository.save(request);
        sendConfirmationEmail(citizen, savedRequest, service);

        return mapToDTO(savedRequest);
    }

    private void sendConfirmationEmail(User citizen, Request savedRequest, MunicipalService service) {
        try {
            String subject = "MyCityGov - Επιβεβαίωση Αιτήματος: " + savedRequest.getProtocolNumber();
            String body = "Αγαπητέ/ή " + citizen.getFullName() + ",\n\n" +
                    "Το αίτημά σας για την υπηρεσία '" + service.getName() + "' καταχωρήθηκε επιτυχώς.\n" +
                    "Αριθμός Πρωτοκόλλου: " + savedRequest.getProtocolNumber() + "\n" +
                    "Ημερομηνία: " + savedRequest.getSubmittedAt() + "\n\n" +
                    "Θα ενημερωθείτε εκ νέου για την εξέλιξη του αιτήματός σας.\n" +
                    "Ο Δήμος σας.";

            // Καλεί το EmailService
            emailService.sendEmail(citizen.getEmail(), subject, body);

        } catch (Exception e) {
            // Loggaroume to laqos alla DEN afhnoume to transaction na kanei rollback
            // gia na mhn xathei to aithma an p.x. pesei o mail server.
            System.err.println("⚠ Warning: Email could not be sent, but request was saved. Error: " + e.getMessage());
        }
    }




    //!
    @Transactional(readOnly = true)

    public List<RequestDTO> getRequestsForCitizen(Long citizenId) {

        User citizen = userRepository.findById(citizenId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return requestRepository.findByCitizen(citizen)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    private RequestDTO mapToDTO(Request request) {
        RequestDTO dto = new RequestDTO();
        dto.setId(request.getId());
        dto.setProtocolNumber(request.getProtocolNumber());
        dto.setCitizenId(request.getCitizen().getId());
        dto.setCitizenUsername(request.getCitizen().getUsername());
        dto.setTitle(request.getTitle());
        dto.setDescription(request.getDescription());
        dto.setStatus(request.getStatus().name());
        dto.setSlaDays(request.getSlaDays());
        dto.setSubmittedAt(request.getSubmittedAt());
        dto.setLastUpdatedAt(request.getLastUpdatedAt());
        dto.setServiceId(request.getService() != null ? request.getService().getId() : null);
        dto.setComments(request.getComments());

        //! αν ειναι εκπροθεσμο
        dto.setOverdue(request.isOverdue());

        // Εάν υπάρχει attachment, ενημερώνουμε το DTO
        if (!request.getAttachments().isEmpty()) {
            Attachment firstAttachment = request.getAttachments().get(0);
            dto.setAttachedFileName(firstAttachment.getFileName());
            dto.setFileUrl("/uploads/" + Paths.get(firstAttachment.getFilePath()).getFileName());
        }
        return dto;
    }

    //Επιστρεφει αιτηματα που αφορουν το τμημα του υπαλληλου//
    @Transactional(readOnly = true)
    public List<RequestDTO> getRequestsForEmployeeService(Long employeeId) {
        User employee = userRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        if (employee.getService() == null) {
            return new ArrayList<>();
        }

        // Χρησιμοποιούμε τη μέθοδο findByService του repository
        return requestRepository.findByService(employee.getService())
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    //Ο υπάλληλος "αναλαμβάνει" ένα αίτημα//
    @Transactional
    public RequestDTO assignRequestToEmployee(Long requestId, Long employeeId) {
        Request request = requestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Request not found"));

        User employee = userRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        // Ο υπαλληλος πρεπει να ειναι ιδιου τμηματος με το αιτημα//
        if (!request.getService().getId().equals(employee.getService().getId())) {
            throw new RuntimeException("You cannot assign a request from another department!");

        }

        request.setHandler(employee);
        request.setStatus(RequestStatus.IN_PROGRESS);
        request.setLastUpdatedAt(LocalDateTime.now());

        return mapToDTO(requestRepository.save(request));

    }

    //Ο υπάλληλος ενημερώνει το αίτημα//
    @Transactional
    public RequestDTO updateRequestStatus(Long requestId, String newStatus, String comments) {
        Request request = requestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Request not found"));

        try {
            RequestStatus status = RequestStatus.valueOf(newStatus);
            request.setStatus(status);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid status");
        }

        request.setComments(comments);
        request.setLastUpdatedAt(LocalDateTime.now());

        return mapToDTO(requestRepository.save(request));

    }

    //Αλλαξε εγκριση-απρροψη//
    @Transactional
    public RequestDTO finalizeRequest(Long requestId, RequestStatus newStatus, String comments) {
        Request request = requestRepository.findById(requestId).
                orElseThrow(() -> new RuntimeException("Request not found"));

        if (newStatus != RequestStatus.COMPLETED && newStatus != RequestStatus.REJECTED) {
            throw new RuntimeException("This method is only for completing or rejecting requests.");
        }
        request.setStatus(newStatus);
        request.setResolutionComments(comments); // Εδώ αποθηκεύεται η τεκμηρίωση
        request.setLastUpdatedAt(LocalDateTime.now());

        Request saved = requestRepository.save(request);
        return mapToDTO(saved);

    }

}
