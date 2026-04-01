package gr.hua.dit.kvdb.kvdb.controller;

import gr.hua.dit.kvdb.kvdb.domain.appointment.AppointmentStatus;
import gr.hua.dit.kvdb.kvdb.domain.request.RequestStatus;
import gr.hua.dit.kvdb.kvdb.domain.user.User;
import gr.hua.dit.kvdb.kvdb.dto.AppointmentDTO;
import gr.hua.dit.kvdb.kvdb.dto.RequestDTO;
import gr.hua.dit.kvdb.kvdb.repository.UserRepository;
import gr.hua.dit.kvdb.kvdb.service.AppointmentService;
import gr.hua.dit.kvdb.kvdb.service.RequestService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/employee")
public class EmployeeActionController {

    private final RequestService requestService;
    private final AppointmentService appointmentService;
    private final UserRepository userRepository;

    public EmployeeActionController(RequestService requestService,
                                    AppointmentService appointmentService,
                                    UserRepository userRepository) {
        this.requestService = requestService;
        this.appointmentService = appointmentService;
        this.userRepository = userRepository;
    }

    // 1. Αιτήματα της υπηρεσίας
    @GetMapping("/requests/department/my")
    public List<RequestDTO> getMyServiceRequests(Authentication authentication) {
        User user = userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));
        return requestService.getRequestsForEmployeeService(user.getId());
    }

    // 2. Ραντεβού της υπηρεσίας
    @GetMapping("/appointments/service/my")
    public List<AppointmentDTO> getMyServiceAppointments(Authentication authentication) {
        User user = userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));
        return appointmentService.getServiceAppointments(user.getId());
    }

    // 3. Ανάληψη αιτήματος
    @PostMapping("/requests/{requestId}/assign/{employeeId}")
    public RequestDTO assignRequest(@PathVariable Long requestId, @PathVariable Long employeeId) {
        return requestService.assignRequestToEmployee(requestId, employeeId);
    }

    // 4. Ενημέρωση κατάστασης αιτήματος
    @PutMapping("/requests/{requestId}/status")
    public RequestDTO updateRequestStatus(@PathVariable Long requestId,
                                          @RequestParam String status,
                                          @RequestParam(required = false) String comments) {
        return requestService.updateRequestStatus(requestId, status, comments);
    }

    // 5. Οριστικοποίηση
    @PostMapping("/requests/{id}/finalize")
    public RequestDTO finalizeRequest(@PathVariable Long id, @RequestBody Map<String, String> payload) {
        RequestStatus status = RequestStatus.valueOf(payload.get("status"));
        return requestService.finalizeRequest(id, status, payload.get("comments"));
    }

    // 6. Aλλαγή status
    @PutMapping("/appointments/{appointmentId}/status")
    public AppointmentDTO updateAppointmentStatus(@PathVariable Long appointmentId,
                                                  @RequestParam String status) {
        AppointmentStatus appointmentStatus = AppointmentStatus.valueOf(status);
        return appointmentService.updateAppointmentStatus(appointmentId, appointmentStatus);
    }

}