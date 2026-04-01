package gr.hua.dit.kvdb.kvdb.domain.request;

import gr.hua.dit.kvdb.kvdb.domain.service.MunicipalService;
import gr.hua.dit.kvdb.kvdb.domain.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "requests")
@NoArgsConstructor   // ΑΠΑΡΑΙΤΗΤΟ για JPA
@Getter
@Setter
public class Request {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Μοναδικός αριθμός πρωτοκόλλου
    @Column(nullable = false, unique = true)
    private String protocolNumber;

    // Πολίτης που υπέβαλε το αίτημα
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private User citizen;

    @Column(nullable = false)
    private String title;

    @Column(length = 2000)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RequestStatus status;

    //  ημέρες πριν βγει εκπρόθεσμο
    @Column(nullable = false)
    private int slaDays;

    @Column(nullable = false)
    private LocalDateTime submittedAt;

    @Column(nullable = false)
    private LocalDateTime lastUpdatedAt;

    @Column(length = 2000)
    private String comments;

    //Αλλαξε//
    @Column(length = 1000)
    private String resolutionComments;

    public Request(String protocolNumber,
                   User citizen,
                   String title,
                   String description,
                   int slaDays) {

        this.protocolNumber = protocolNumber;
        this.citizen = citizen;
        this.title = title;
        this.description = description;
        this.slaDays = slaDays;

        // αρχικές τιμές συστήματος
        this.status = RequestStatus.SUBMITTED;
        this.submittedAt = LocalDateTime.now();
        this.lastUpdatedAt = LocalDateTime.now();
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_id", nullable = false)
    private MunicipalService service;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "handler_id") // FK στη βάση για τον υπάλληλο
    private User handler;




    public void updateStatus(RequestStatus newStatus) {
        this.status = newStatus;
        this.lastUpdatedAt = LocalDateTime.now();
    }

    public boolean isOverdue() {
        return submittedAt
                .plusDays(slaDays)
                .isBefore(LocalDateTime.now())
                && status != RequestStatus.COMPLETED
                && status != RequestStatus.REJECTED;
    }

    @OneToMany(mappedBy = "request", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Attachment> attachments = new ArrayList<>();

public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProtocolNumber() {
        return protocolNumber;
    }

    public void setProtocolNumber(String protocolNumber) {
        this.protocolNumber = protocolNumber;
    }

    public User getCitizen() {
        return citizen;
    }

    public void setCitizen(User citizen) {
        this.citizen = citizen;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public RequestStatus getStatus() {
        return status;
    }

    public void setStatus(RequestStatus status) {
        this.status = status;
    }

    public int getSlaDays() {
        return slaDays;
    }

    public void setSlaDays(int slaDays) {
        this.slaDays = slaDays;
    }

    public LocalDateTime getSubmittedAt() {
        return submittedAt;
    }

    public void setSubmittedAt(LocalDateTime submittedAt) {
        this.submittedAt = submittedAt;
    }

    public LocalDateTime getLastUpdatedAt() {
        return lastUpdatedAt;
    }

    public void setLastUpdatedAt(LocalDateTime lastUpdatedAt) {
        this.lastUpdatedAt = lastUpdatedAt;
    }


}
