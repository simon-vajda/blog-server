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
    @Size(max = 40)
    private String name;

    @NotBlank
    @Size(max = 50)
    @Email
    private String email;

    @NotBlank
    @Size(min = 8, max = 40)
    private String password;
}
