package sanotesnoteservice.payload;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
public class NoteResponse extends FallBackResponse {

    private UUID id;
    private String noteId;
    private String topic;
    private String text;

    private UUID noteBookId;
    private List<UUID> tags;


}
