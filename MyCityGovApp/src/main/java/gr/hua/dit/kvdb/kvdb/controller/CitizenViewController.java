package gr.hua.dit.kvdb.kvdb.controller;

import gr.hua.dit.kvdb.kvdb.domain.user.User;
import gr.hua.dit.kvdb.kvdb.repository.MunicipalServiceRepository;
import gr.hua.dit.kvdb.kvdb.repository.UserRepository;
import gr.hua.dit.kvdb.kvdb.service.RequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@RequestMapping("/citizen")
@RequiredArgsConstructor
public class CitizenViewController {

    private final MunicipalServiceRepository serviceRepo;
    private final RequestService requestService;
    private final UserRepository userRepo;

    @GetMapping("/dashboard")
    public String dashboard() {
        return "citizen/dashboard";
    }

    @GetMapping("/services")
    public String services(Model model) {
        model.addAttribute("services", serviceRepo.findAll());
        return "citizen/services";
    }

    @GetMapping("/requests")
    public String myRequests(Model model, Principal principal) {
        User user = userRepo.findByUsername(principal.getName()).orElseThrow();
        model.addAttribute("requests",
                requestService.getRequestsForCitizen(user.getId()));
        return "citizen/requests";
    }
}
