package gr.hua.dit.kvdb.kvdb.repository;

import gr.hua.dit.kvdb.kvdb.domain.request.Request;
import gr.hua.dit.kvdb.kvdb.domain.service.MunicipalService;
import gr.hua.dit.kvdb.kvdb.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;



import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public interface RequestRepository extends JpaRepository<Request, Long> {

    List<Request> findByCitizen(User citizen);

    Optional<Request> findByProtocolNumber(String protocolNumber);

    //! Arrays findByCitizenId(Long citizenId);

    //Βρησκει ολα τα αιτηματα αναλογα με μια συγκεκριμενη υπηρεσια//
    List<Request> findByService(MunicipalService service);

    List<Request> findByServiceId(Long serviceId);

    //Βρησκει ολα τα αιτηματα που διαχειριζονται απο εναν συγκεκριμενο  υπαλληλο//
    List<Request> findByHandler(User handler);


}
