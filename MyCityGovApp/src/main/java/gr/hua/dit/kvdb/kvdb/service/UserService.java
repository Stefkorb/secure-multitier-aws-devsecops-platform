package gr.hua.dit.kvdb.kvdb.service;

import gr.hua.dit.kvdb.kvdb.domain.user.User;
import gr.hua.dit.kvdb.kvdb.dto.RegisterRequestDTO;

import java.util.List;

public interface UserService {

    User registerCitizen(RegisterRequestDTO dto);

    User findByUsername(String username);

    User findByEmail(String email);

    List<User> getAllUsers();
}
