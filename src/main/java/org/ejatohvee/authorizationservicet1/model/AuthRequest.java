package org.ejatohvee.authorizationservicet1.model;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AuthRequest {
    @NotBlank
    private String username;

    @NotBlank
    private String password;
}