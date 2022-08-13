package sanotesnotebookservice.payload;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BooleanResponse extends FallBackResponse{
    private Boolean result;

    public BooleanResponse(Boolean result) {
        this.result = result;
    }

}
