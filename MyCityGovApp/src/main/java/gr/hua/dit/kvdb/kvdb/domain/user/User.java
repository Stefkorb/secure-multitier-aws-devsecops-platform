package gr.hua.dit.kvdb.kvdb.domain.user;

// mapping
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Email;

import gr.hua.dit.kvdb.kvdb.domain.service.MunicipalService;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;




//αυτοματα getters, setters κ constructors
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User implements UserDetails {

    // 🔑 Primary key
    @Id

    //αυτοματη δημιουργια id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false, unique = true)
    @NotBlank
    @Email
    private String email;

    @Column(nullable = false)
    @JsonIgnore
    private String password;

    @Column(nullable = false)
    private String fullName;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(nullable = false, unique = true)
    private String afm;

    @Column(nullable = false, unique = true)
    private String amka;



    //Ενας υπαλληλος ανηκει σε μια υπηρεσια αν ειναι υπαλληλος//

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "service_id")
    private MunicipalService service;


    //enum για τους διαφορετικους χρηστες

    // =========================
    // Spring Security methods
    // =========================

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        return List.of(new SimpleGrantedAuthority(this.role.name()));
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }


    // =========================
    // Ρόλοι χρηστών
    // =========================
    public enum Role {
        CITIZEN, //πολιτης

        EMPLOYEE, // Υπάλληλος Υπηρεσίας (employee / case handler)

        ADMIN // Διαχειριστής Δήμου (Admin)
    }
}
