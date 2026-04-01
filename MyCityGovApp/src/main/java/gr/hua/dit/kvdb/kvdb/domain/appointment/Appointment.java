package gr.hua.dit.kvdb.kvdb.domain.appointment;

import gr.hua.dit.kvdb.kvdb.domain.service.MunicipalService;
import gr.hua.dit.kvdb.kvdb.domain.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "appointments")
@Getter
@Setter
@NoArgsConstructor
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "service_id")
    private MunicipalService service;

    @ManyToOne(optional = false)
    @JoinColumn(name = "citizen_id")
    private User citizen;

    @Column(nullable = false)
    private LocalDateTime appointmentDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AppointmentStatus appointmentStatus;

    //δημιουργια ραντεβου
    private LocalDateTime createdAt;


    public Appointment(User citizen, MunicipalService service, LocalDateTime appointmentDate) {
        this.citizen = citizen;
        this.service = service;
        this.appointmentDate = appointmentDate;
        this.appointmentStatus = AppointmentStatus.PENDING;
        this.createdAt = LocalDateTime.now();
    }
}







