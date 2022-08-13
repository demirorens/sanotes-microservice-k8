package sanotesnotebookservice.payload;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.UUID;

@Data
@NoArgsConstructor
public class NoteBookRequest {

    private UUID id;
    @NotBlank
    private String name;
    @NotBlank
    private String description;
}
