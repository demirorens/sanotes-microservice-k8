package sanotesnoteservice.payload;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
public class NoteRequest {

    private UUID id;
    private String noteId;
    @NotBlank
    private String topic;
    @NotBlank
    private String text;
    @NotNull
    private UUID noteBookId;
    private List<UUID> tags;

}
