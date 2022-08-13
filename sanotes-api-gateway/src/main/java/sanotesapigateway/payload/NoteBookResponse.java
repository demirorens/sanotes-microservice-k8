package sanotesapigateway.payload;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
public class NoteBookResponse extends FallBackResponse {
    private UUID id;
    private String name;
    private String description;
    private List<NoteResponse> notes;

    public NoteBookResponse(String fallbackMessage) {
        super(fallbackMessage);
    }

}
