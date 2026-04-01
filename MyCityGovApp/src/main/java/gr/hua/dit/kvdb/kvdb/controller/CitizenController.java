package gr.hua.dit.kvdb.kvdb.controller;

import gr.hua.dit.kvdb.kvdb.domain.service.MunicipalService;
import gr.hua.dit.kvdb.kvdb.domain.user.User;
import gr.hua.dit.kvdb.kvdb.dto.RequestDTO;
import gr.hua.dit.kvdb.kvdb.repository.MunicipalServiceRepository;
import gr.hua.dit.kvdb.kvdb.repository.UserRepository;
import gr.hua.dit.kvdb.kvdb.service.RequestService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/citizen")
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('CITIZEN')") // Μόνο πολίτες έχουν πρόσβαση
public class CitizenController {

    //Authorization: Bearer <jwt_token>

    private final RequestService requestService;
    private final UserRepository userRepository;
    private final MunicipalServiceRepository serviceRepository;


//    public ResponseEntity<RequestDTO> createRequest(@Valid @RequestBody RequestDTO dto, Principal principal) {
//        // Βρίσκουμε τον συνδεδεμένο χρήστη από το username του Token
//        User citizen = userRepository.findByUsername(principal.getName())
//                .orElseThrow(() -> new RuntimeException("User not found"));
//
//        // Θέτουμε το ID του πολίτη στο DTO για να το επεξεργαστεί το Service
//        dto.setCitizenId(citizen.getId());
//
//        return ResponseEntity.ok(requestService.createRequest(dto));
//    }

     //Προβολή καταλόγου διαθέσιμων υπηρεσιών
    @GetMapping("/services")
    public ResponseEntity<List<MunicipalService>> getAllAvailableServices() {
        // Επιστρέφει όλες τις υπηρεσίες που έχουμε στο data loader
        //return ResponseEntity.ok(serviceRepository.findAll());
        return ResponseEntity.ok(serviceRepository.findByActiveTrue());
    }


    // Υποβολή νέας αίτησης
    @PostMapping(value = "/requests", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<RequestDTO> createRequest(
            @RequestPart("request") @Valid RequestDTO dto,
            @RequestPart(value = "files", required = false) List<MultipartFile> files,
            Principal principal) {

        User citizen = userRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        dto.setCitizenId(citizen.getId());

        // Καλούμε τη μέθοδο που χειρίζεται αρχεία
        return ResponseEntity.ok(requestService.createRequestWithFiles(dto, files));
    }


    // Προβολή όλων των αιτήσεων του συνδεδεμένου πολίτη.
    @GetMapping("/requests")
    public ResponseEntity<List<RequestDTO>> getMyRequests(Principal principal) {
        User citizen = userRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));


        // Επιστρέφει όλα τα αιτήματα του συγκεκριμένου πολίτη
        List<RequestDTO> requests = requestService.getRequestsForCitizen(citizen.getId());

        return ResponseEntity.ok(requestService.getRequestsForCitizen(citizen.getId()));
    }


}