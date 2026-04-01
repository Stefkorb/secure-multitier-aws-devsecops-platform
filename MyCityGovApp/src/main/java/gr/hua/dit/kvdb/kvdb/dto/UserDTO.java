package gr.hua.dit.kvdb.kvdb.dto;

import gr.hua.dit.kvdb.kvdb.domain.service.MunicipalService;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDTO {

    @NotBlank(message = "Username is required")
    private String username;

    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    private String email;

    @NotBlank(message = "Password is required")
    private String password;

    @NotBlank(message = "Full name is required")
    private String fullName;

    @NotBlank(message = "AFM is required")
    private String afm;

    @NotBlank(message = "AMKA is required")
    private String amka;

    private MunicipalServiceDTO service;
}
