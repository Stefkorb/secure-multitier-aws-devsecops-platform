package gr.hua.dit.kvdb.kvdb.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginViewController {

    @GetMapping("/")
    public String indexPage() {
        return "index"; // Επιστρέφει το index.html
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login"; // Επιστρέφει το login.html
    }

    @GetMapping("/register")
    public String registerPage() {
        return "register";// Επιστρέφει το register.html
    }

    @GetMapping("/citizen-dashboard")
    public String citizenDashboard() {
        return "citizen-dashboard";
    }

    @GetMapping("/new-request")
    public String newRequestPage() {
        return "new-request";
        
    }

    @GetMapping("/employee-dashboard")
    public String employeeDashboard() { return "employee-dashboard"; }

    @GetMapping("/admin-dashboard")
    public String adminDashboard() { return "admin-dashboard"; }

}