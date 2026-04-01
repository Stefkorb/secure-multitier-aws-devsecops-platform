package gr.hua.dit.kvdb.kvdb.controller;

import gr.hua.dit.kvdb.kvdb.domain.user.User;
import gr.hua.dit.kvdb.kvdb.dto.RegisterRequestDTO;
import gr.hua.dit.kvdb.kvdb.dto.UserDTO;
import gr.hua.dit.kvdb.kvdb.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/test/users")
public class UserTestController {

    private final UserService userService;

    public UserTestController(UserService userService) {
        this.userService = userService;
    }

    // --- GET endpoint για testing στο browser ---
    @GetMapping
    public String testEndpoint() {
        return "UserTestController is running!";
    }

    // --- GET όλων των χρηστών για testing ---
    @GetMapping("/all")
    public List<User> getAllUsers() {
        return userService.getAllUsers(); // θα χρειαστεί να προσθέσουμε αυτή τη μέθοδο στο UserService
    }

    // --- POST για δημιουργία νέου χρήστη ---
    @PostMapping("/register")
    public User register(@RequestBody RegisterRequestDTO dto) {
        return userService.registerCitizen(dto);
    }
}
