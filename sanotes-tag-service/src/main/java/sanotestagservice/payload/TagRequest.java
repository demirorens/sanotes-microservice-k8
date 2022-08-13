package sanotestagservice.payload;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.util.UUID;

@Data
@NoArgsConstructor
public class TagRequest {
    private UUID id;
    @NotBlank
    private String name;
    @NotBlank
    private String description;

}
