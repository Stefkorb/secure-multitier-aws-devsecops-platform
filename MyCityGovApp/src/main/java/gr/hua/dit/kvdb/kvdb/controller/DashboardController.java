package gr.hua.dit.kvdb.kvdb.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

//Dashboard Redirect ΑΝΑ ΡΟΛΟ
@Controller
public class DashboardController {

    @GetMapping("/dashboard")
    public String dashboard(Authentication auth) {
        if (auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ADMIN")))
            return "redirect:/admin/dashboard";

        if (auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("EMPLOYEE")))
            return "redirect:/employee/requests";

        return "redirect:/citizen/dashboard";
    }
}
