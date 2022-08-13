package sanotesapigateway.payload;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
public class TagResponse extends FallBackResponse{
    private UUID id;
    private String name;
    private String description;

    public TagResponse(String fallbackMessage) {
        super(fallbackMessage);
    }
}
