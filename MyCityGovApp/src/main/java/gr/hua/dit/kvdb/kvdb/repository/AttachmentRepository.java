package gr.hua.dit.kvdb.kvdb.repository;

import gr.hua.dit.kvdb.kvdb.domain.request.Attachment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AttachmentRepository extends JpaRepository<Attachment, Long> {
    // Μέθοδος αν θέλεις αργότερα να βρίσκεις όλα τα αρχεία μιας αίτησης
    List<Attachment> findByRequestId(Long requestId);
}