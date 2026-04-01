package gr.hua.dit.kvdb.kvdb.repository;

import gr.hua.dit.kvdb.kvdb.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

// CRUD(Create, Read, Update, Delete)
public interface UserRepository extends JpaRepository<User, Long> {

    // Μέθοδος για εύρεση χρήστη με βάση το username κ Optional<User> για την περίπτωση που δεν υπάρχει χρήστης
    Optional<User> findByUsername(String username);

    // Μέθοδος για εύρεση χρήστη με βάση το email κ επιστρέφει Optional<User> για null
    Optional<User> findByEmail(String email);
}