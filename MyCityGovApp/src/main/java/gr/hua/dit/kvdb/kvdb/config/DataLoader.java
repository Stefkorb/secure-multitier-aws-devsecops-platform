package gr.hua.dit.kvdb.kvdb.config;

import gr.hua.dit.kvdb.kvdb.domain.service.MunicipalService;
import gr.hua.dit.kvdb.kvdb.domain.user.User;
import gr.hua.dit.kvdb.kvdb.domain.user.User.Role;
import gr.hua.dit.kvdb.kvdb.repository.MunicipalServiceRepository;
import gr.hua.dit.kvdb.kvdb.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
//!
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

@Configuration
//!
public class DataLoader {

    @Bean
    @Transactional
        // Πρόσθεσε τον PasswordEncoder εδώ ως παράμετρο για να τον κάνει η Spring Inject
    CommandLineRunner loadData(UserRepository userRepository,
                               MunicipalServiceRepository serviceRepository,
                               PasswordEncoder passwordEncoder) {
        return args -> {
            final MunicipalService techService = serviceRepository.findByName("Τεχνική Υπηρεσία")
                    .orElseGet(() -> {
                        MunicipalService s = new MunicipalService(null, "Τεχνική Υπηρεσία", "Υπεύθυνη για δρόμους", 10, true);
                        return serviceRepository.save(s);
                    });

            final MunicipalService KEP = serviceRepository.findByName("KEP")
                    .orElseGet(() -> {
                        MunicipalService s = new MunicipalService(null, "KEP", "Κέντρο Εξυπηρέτησης Πολιτών", 9, true);
                        return serviceRepository.save(s);
                    });

            final MunicipalService residenceService = serviceRepository.findByName("Βεβαίωση μόνιμης κατοικίας")
                    .orElseGet(() -> serviceRepository.save(new MunicipalService(null, "Βεβαίωση μόνιμης κατοικίας", "Έκδοση βεβαίωσης για την κατοικία", 5, true)));

            final MunicipalService sidewalkService = serviceRepository.findByName("Άδεια κατάληψης πεζοδρομίου")
                    .orElseGet(() -> serviceRepository.save(new MunicipalService(null, "Άδεια κατάληψης πεζοδρομίου", "Αίτηση για χρήση κοινόχρηστου χώρου", 15, true)));

            final MunicipalService lightingService = serviceRepository.findByName("Αναφορά προβλήματος φωτισμού")
                    .orElseGet(() -> serviceRepository.save(new MunicipalService(null, "Αναφορά προβλήματος φωτισμού", "Αποκατάσταση βλάβης σε λάμπες δρόμου", 3, true)));

            System.out.println("✔ Services initialized!");



            // 2. Δημιουργία Χρηστών αν η βάση είναι άδεια
            if (userRepository.count() == 0) {

                // ΠΟΛΙΤΗΣ
                User citizen = new User(
                        null,
                        "giorgos",
                        "giorgos@mail.gr",
                        passwordEncoder.encode("1234"), // <--- ΠΑΝΤΑ encode
                        "Γιώργος Παπαδόπουλος",
                        Role.CITIZEN,
                        "123456789",
                        "01019912345",
                        null
                );
                userRepository.save(citizen);

                // ΥΠΑΛΛΗΛΟΣ 1

                User employee1 = new User();
                employee1.setUsername("employee1");
                employee1.setPassword(passwordEncoder.encode("1234"));
                employee1.setEmail("emp2@city.gov");
                employee1.setFullName("Dimitris YP2");
                employee1.setRole(Role.EMPLOYEE);
                employee1.setService(KEP);
                employee1.setAmka("02029099996");
                employee1.setAfm("888888888");


                userRepository.save(employee1);

                // ΥΠΑΛΛΗΛΟΣ 2
                User employee2 = new User();
                employee2.setUsername("employee2");
                employee2.setPassword(passwordEncoder.encode("1234"));
                employee2.setEmail("emp1@city.gov");
                employee2.setFullName("Dimitris Ypallilos");
                employee2.setRole(Role.EMPLOYEE);
                employee2.setService(techService);
                employee2.setAmka("02029099998");
                employee2.setAfm("888888880");


                userRepository.save(employee2);

                //προσθηκη admin
                if (userRepository.findByUsername("admin").isEmpty()) {
                    User admin = new User();
                    admin.setUsername("admin");
                    admin.setPassword(passwordEncoder.encode("admin123")); // ΚΡΥΠΤΟΓΡΑΦΗΣΗ!
                    admin.setEmail("admin@hua.gr");
                    admin.setFullName("System Administrator");
                    admin.setRole(User.Role.ADMIN);
                    admin.setAfm("000000000");
                    admin.setAmka("00000000000");
                    userRepository.save(admin);
                    System.out.println("✔ Admin user created: admin/admin123");
                }

                System.out.println("✔ All Test Users saved with encrypted passwords!");
            }
        };
    }
}