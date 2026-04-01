package gr.hua.dit.kvdb.kvdb.dto;

import gr.hua.dit.kvdb.kvdb.domain.user.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterRequestDTO {

    @NotBlank
    private String username;

    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String password;

    @NotBlank
    private String fullName;

    @NotBlank
    private String afm;

    @NotBlank
    private String amka;

    public void setRole(User.Role role) {
    }
}
