package gr.hua.dit.kvdb.kvdb.repository;

import gr.hua.dit.kvdb.kvdb.domain.appointment.Appointment;
import gr.hua.dit.kvdb.kvdb.domain.appointment.AppointmentStatus;
import gr.hua.dit.kvdb.kvdb.domain.service.MunicipalService;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime;
import java.util.List;


public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    // Επιστρέφει τη λίστα των ραντεβού για έναν συγκεκριμένο πολίτη
    List<Appointment> findByCitizenId(Long citizenId);

    //Ευρεση αν υπαρχει το ραντεβου ανα ωρα και status
    boolean existsByServiceAndAppointmentDateAndAppointmentStatusNot(
            MunicipalService service,
            LocalDateTime date,
            gr.hua.dit.kvdb.kvdb.domain.appointment.AppointmentStatus appointmentStatus
    );



    //Ευρεση ραντεβου ανα υπηρεσια για τον υπαλληλο
    List<Appointment> findByServiceId(Long serviceId);

}

