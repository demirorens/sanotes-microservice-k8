package sanotesuserservice.payload;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
public class LoginRequest {
    @NotBlank
    private String emailOrUsername;

    @NotBlank
    private String password;

}
