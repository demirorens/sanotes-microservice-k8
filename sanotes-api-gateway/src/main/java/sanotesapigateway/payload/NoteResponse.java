package sanotesapigateway.payload;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@JsonIdentityInfo(scope = NoteResponse.class,
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
public class NoteResponse extends FallBackResponse {

    private UUID id;
    private String noteId;
    private String topic;
    private String text;

    private UUID noteBookId;
    private List<UUID> tags;

    public NoteResponse(String fallbackMessage) {
        super(fallbackMessage);
    }

}
