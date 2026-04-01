package gr.hua.dit.kvdb.kvdb.controller;

import gr.hua.dit.kvdb.kvdb.domain.user.User;
import gr.hua.dit.kvdb.kvdb.dto.AppointmentDTO;
import gr.hua.dit.kvdb.kvdb.repository.UserRepository;
import gr.hua.dit.kvdb.kvdb.service.AppointmentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/citizen/appointments")
@PreAuthorize("hasAuthority('CITIZEN')")
public class CitizenAppointmentController {

    private final AppointmentService appointmentService;
    @Autowired
    private final UserRepository userRepository;

    public CitizenAppointmentController(AppointmentService appointmentService, UserRepository userRepository) {
        this.appointmentService = appointmentService;
        this.userRepository = userRepository;
    }

//    @PostMapping
//    public AppointmentDTO bookAppointment(@RequestBody @Valid AppointmentDTO dto) {
//        return appointmentService.bookAppointment(
//                dto.getCitizenId(),
//                dto.getServiceId(),
//                dto.getAppointmentDate()
//        );
//    }

    @PostMapping
    public AppointmentDTO bookAppointment(@RequestBody @Valid AppointmentDTO dto, Principal principal) {
        // Βρίσκουμε τον χρήστη από το Token
        User citizen = userRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Χρησιμοποιούμε το ID του συνδεδεμένου χρήστη
        return appointmentService.bookAppointment(
                citizen.getId(),
                dto.getServiceId(),
                dto.getAppointmentDate()
        );
    }
    @GetMapping("/my")
    public List<AppointmentDTO> getMyAppointments(java.security.Principal principal) {
        // Βρίσκουμε τον χρήστη από το Token
        User citizen = userRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Καλούμε το service για να μας φέρει τα ραντεβού του
        return appointmentService.getAppointmentsForCitizen(citizen.getId());
    }

}
