package hu.vsimon.blogserver.auth.payload;

import hu.vsimon.blogserver.role.RoleName;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Collection;

@Getter
@Setter
public class SignupRequest {
    @NotBlank
    @Size(max = 40, message = "*The cannot be longer than 40 characters")
    private String name;

    @NotBlank
    @Size(max = 50, message = "The email cannot be longer than 50 characters")
    @Email
    private String email;

    @NotBlank
    @Size(min = 8, max = 40, message = "*The password has to be between 8 and 40 characters long")
    private String password;

    private Collection<RoleName> roles;
}
