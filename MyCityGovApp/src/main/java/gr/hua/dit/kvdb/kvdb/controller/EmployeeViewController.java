package gr.hua.dit.kvdb.kvdb.controller;

import gr.hua.dit.kvdb.kvdb.domain.user.User;
import gr.hua.dit.kvdb.kvdb.repository.UserRepository;
import gr.hua.dit.kvdb.kvdb.service.RequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@RequestMapping("/employee")
@RequiredArgsConstructor
public class EmployeeViewController {

    private final RequestService requestService;
    private final UserRepository userRepository;

    @GetMapping("/requests")
    public String requests(Model model, Principal principal) {

        //βρίσκουμε τον συνδεδεμένο υπάλληλο
        User employee = userRepository
                .findByUsername(principal.getName())
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        //φέρνουμε τα αιτήματα του τμήματός του
        model.addAttribute(
                "requests",
                requestService.getRequestsForEmployeeService(employee.getId())
        );

        return "employee/requests";
    }
}
