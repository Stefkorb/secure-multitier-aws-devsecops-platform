package gr.hua.dit.kvdb.kvdb.service;

import gr.hua.dit.kvdb.kvdb.domain.appointment.Appointment;
import gr.hua.dit.kvdb.kvdb.domain.appointment.AppointmentStatus;
import gr.hua.dit.kvdb.kvdb.domain.service.MunicipalService;
import gr.hua.dit.kvdb.kvdb.domain.service.WorkingHour;
import gr.hua.dit.kvdb.kvdb.domain.user.User;
import gr.hua.dit.kvdb.kvdb.dto.AppointmentDTO;
import gr.hua.dit.kvdb.kvdb.repository.AppointmentRepository;
import gr.hua.dit.kvdb.kvdb.repository.MunicipalServiceRepository;
import gr.hua.dit.kvdb.kvdb.repository.UserRepository;
import gr.hua.dit.kvdb.kvdb.repository.WorkingHourRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final UserRepository userRepository;
    private final MunicipalServiceRepository serviceRepository;
    private final WorkingHourRepository workingHourRepository;

    private final EmailService emailService;

    public AppointmentService(AppointmentRepository appointmentRepository, UserRepository userRepository, MunicipalServiceRepository serviceRepository, WorkingHourRepository workingHourRepository, EmailService emailService) {
        this.appointmentRepository = appointmentRepository;
        this.userRepository = userRepository;
        this.serviceRepository = serviceRepository;
        this.workingHourRepository = workingHourRepository;
        this.emailService = emailService;
    }

    @Transactional
    public AppointmentDTO bookAppointment(Long citizenId, Long serviceId, LocalDateTime dateTime) {
        User citizen = userRepository.findById(citizenId).
                orElseThrow(() ->new RuntimeException("Citizen not Found"));
        MunicipalService service = serviceRepository.findById(serviceId).
                orElseThrow(() -> new RuntimeException("Service not found"));

        //Έλεγχος ωραρίων
        DayOfWeek dayOfWeek = dateTime.getDayOfWeek();
        LocalTime time = dateTime.toLocalTime();

        List<WorkingHour> workingHours =
                workingHourRepository.findByMunicipalServiceAndDayOfWeek(service, dayOfWeek);

        boolean withinWorkingHours = workingHours.stream()
                .anyMatch(wh -> !time.isBefore(wh.getStartTime()) && !time.isAfter(wh.getEndTime()));

        if (!withinWorkingHours) {
            throw new RuntimeException("Η ώρα που επιλέξατε είναι εκτός των ωραρίων της υπηρεσίας");
        }


        //Ελεγχουμε αν υπαρχει ηδη ραντεβου
        boolean slotTaken = appointmentRepository.existsByServiceAndAppointmentDateAndAppointmentStatusNot(service, dateTime, AppointmentStatus.CANCELLED);
        if(slotTaken) {
            throw new RuntimeException("This time slot is already booked");
        }



        //δημιουργία appointment
        Appointment appointment = new Appointment(citizen, service , dateTime);
        appointment = appointmentRepository.save(appointment);

        // Αποθήκευση και email
        String subject = "Επιβεβαίωση Ραντεβού";
        String body = "Το ραντεβού σας με την υπηρεσία " + service.getName() +
                " κλείστηκε για τις " + appointment.getAppointmentDate();

        emailService.sendEmail(citizen.getEmail(), subject, body);

        return mapToDTO(appointment);

    }

    @Transactional(readOnly = true)
    public List<AppointmentDTO> getServiceAppointments(Long employeeId) {
        // 1. Βρίσκουμε τον υπάλληλο
        User employee = userRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        // 2. Παίρνουμε το Service στο οποίο ανήκει ο υπάλληλος
        if (employee.getService() == null) {
            return new ArrayList<>();
        }

        // 3. Φέρνουμε τα ραντεβού μόνο για αυτό το Service
        List<Appointment> appointments = appointmentRepository.findByServiceId(employee.getService().getId());

        return appointments.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public AppointmentDTO updateAppointmentStatus(Long appointmentId, AppointmentStatus appointmentStatus) {
        Appointment appointment = appointmentRepository.findById(appointmentId).
                orElseThrow(() -> new RuntimeException("Appointment not found"));

        appointment.setAppointmentStatus(appointmentStatus);
        return mapToDTO(appointmentRepository.save(appointment));




    }

    private AppointmentDTO mapToDTO(Appointment app) {
        AppointmentDTO dto = new AppointmentDTO();
        dto.setId(app.getId());

        if (app.getCitizen() != null) {
            dto.setCitizenId(app.getCitizen().getId());
            dto.setCitizenName(app.getCitizen().getFullName());
        }

        dto.setServiceId(app.getService().getId());
        dto.setServiceName(app.getService().getName());
        dto.setAppointmentDate(app.getAppointmentDate());
        dto.setAppointmentStatus(app.getAppointmentStatus().name());
        return dto;
    }


    @Transactional(readOnly = true)
    public List<AppointmentDTO> getAppointmentsForCitizen(Long citizenId) {
        //Τραβάμε τα entities από τη βάση
        List<Appointment> appointments = appointmentRepository.findByCitizenId(citizenId);

        //Μετατρέπουμε κάθε Entity σε DTO χρησιμοποιώντας την mapToDTO
        return appointments.stream()
                .map(this::mapToDTO) // Χρησιμοποιεί την υπάρχουσα μέθοδο mapping
                .collect(Collectors.toList());
    }
}
