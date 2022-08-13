package sanotesuserservice.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserModel {

    private static final long serialVersionUID = 1L;

    private String id;

    private String firstname;

    private String lastname;

    private String username;

    private String password;

    private String email;

}
