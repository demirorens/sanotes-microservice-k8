package sanotesuserservice.payload;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Data
@NoArgsConstructor
public class JwtAuthenticationResponse extends FallBackResponse{

    @JsonProperty("access_token")
    protected String token;
    @JsonProperty("expires_in")
    protected long expiresIn;
    @JsonProperty("refresh_expires_in")
    protected long refreshExpiresIn;
    @JsonProperty("refresh_token")
    protected String refreshToken;
    @JsonProperty("token_type")
    protected String tokenType;
    @JsonProperty("id_token")
    protected String idToken;
    @JsonProperty("not-before-policy")
    protected int notBeforePolicy;
    @JsonProperty("session_state")
    protected String sessionState;
    protected Map<String, Object> otherClaims = new HashMap();
    @JsonProperty("scope")
    protected String scope;
    @JsonProperty("error")
    protected String error;
    @JsonProperty("error_description")
    protected String errorDescription;
    @JsonProperty("error_uri")
    protected String errorUri;

}
