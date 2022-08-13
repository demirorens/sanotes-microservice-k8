package sanotesapigateway.payload;


import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class UserItemsResponse extends FallBackResponse {
    private String id;
    private String firstname;
    private String lastname;
    private String username;
    private String email;
    private List<NoteBookResponse> noteBooks;
    private List<TagResponse> tags;

    public UserItemsResponse(String fallbackMessage) {
        super(fallbackMessage);
    }
}
