package gr.hua.dit.kvdb.kvdb.controller;

import gr.hua.dit.kvdb.kvdb.domain.user.User;
import gr.hua.dit.kvdb.kvdb.dto.RegisterRequestDTO;
import gr.hua.dit.kvdb.kvdb.repository.UserRepository;
import gr.hua.dit.kvdb.kvdb.service.AuthService;
import gr.hua.dit.kvdb.kvdb.service.TokenBlacklistService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final AuthService authService;
    private final TokenBlacklistService tokenBlacklistService;
    private final UserRepository userRepository;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequestDTO request) {
        User registeredUser = authService.register(request);
        return ResponseEntity.ok(registeredUser);
    }


//    @PostMapping("/login")
//    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
//        String token = authService.login(request.getUsername(), request.getPassword());
//        return ResponseEntity.ok(new JwtResponse(token));
//
//
//    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {

        String token = authService.login(request.getUsername(), request.getPassword());

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        java.util.Map<String, Object> response = new java.util.HashMap<>();
        response.put("token", token);
        response.put("authority", user.getRole().name());
        response.put("userId", user.getId());

        return ResponseEntity.ok(response);
    }


    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String jwt = authHeader.substring(7);

            tokenBlacklistService.blacklistToken(jwt);

            return ResponseEntity.ok("Logout successful. Token invalidated.");
        }
        return ResponseEntity.badRequest().body("Invalid logout request.");
    }

    @Data
    static class LoginRequest {
        private String username;
        private String password;
    }

    @Data
    @AllArgsConstructor
    static class JwtResponse {
        private String token;
    }
}
