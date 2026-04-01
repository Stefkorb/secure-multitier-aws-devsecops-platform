package gr.hua.dit.kvdb.kvdb.domain.service;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import jakarta.persistence.Table;

//αυτοματα getters, setters κ constructors
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "municipal_services")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MunicipalService {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    private String description;

    // default SLA σε μέρες
    private int slaDays;

    private boolean active = true;
}
