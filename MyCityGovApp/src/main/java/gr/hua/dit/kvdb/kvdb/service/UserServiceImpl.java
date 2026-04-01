package gr.hua.dit.kvdb.kvdb.service;

import gr.hua.dit.kvdb.kvdb.domain.user.User;
import gr.hua.dit.kvdb.kvdb.dto.RegisterRequestDTO;
import gr.hua.dit.kvdb.kvdb.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // constructor
    public UserServiceImpl(UserRepository userRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User registerCitizen(RegisterRequestDTO dto) {

        // έλεγχος username
        userRepository.findByUsername(dto.getUsername())
                .ifPresent(u -> {
                    throw new RuntimeException("Username already exists");
                });

        // έλεγχος email
        userRepository.findByEmail(dto.getEmail())
                .ifPresent(u -> {
                    throw new RuntimeException("Email already exists");
                });

        User user = new User();
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword())); // 🔐 ΠΟΛΥ ΣΗΜΑΝΤΙΚΟ
        user.setFullName(dto.getFullName());
        user.setAfm(dto.getAfm());
        user.setAmka(dto.getAmka());
        user.setRole(User.Role.CITIZEN);

        return userRepository.save(user);
    }


    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}

