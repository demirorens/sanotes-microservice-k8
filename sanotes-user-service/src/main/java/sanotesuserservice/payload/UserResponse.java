package sanotesuserservice.payload;


import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Data
@NoArgsConstructor
public class UserResponse extends FallBackResponse{
    private String id;
    private String firstname;
    private String lastname;
    private String username;
    private String email;

}
