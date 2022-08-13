package sanotesuserservice.payload;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
public class ByIdRequest {
    private String id;

}
