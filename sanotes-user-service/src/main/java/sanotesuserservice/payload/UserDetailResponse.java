package sanotesuserservice.payload;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.keycloak.json.StringListMapDeserializer;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.FederatedIdentityRepresentation;
import org.keycloak.representations.idm.SocialLinkRepresentation;
import org.keycloak.representations.idm.UserConsentRepresentation;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Data
@NoArgsConstructor
public class UserDetailResponse extends FallBackResponse{
    protected String self;
    protected String id;
    protected String origin;
    protected Long createdTimestamp;
    protected String username;
    protected Boolean enabled;
    protected Boolean totp;
    protected Boolean emailVerified;
    protected String firstName;
    protected String lastName;
    protected String email;
    protected String federationLink;
    protected String serviceAccountClientId;
    @JsonDeserialize(using = StringListMapDeserializer.class)
    protected Map<String, List<String>> attributes;
    protected List<CredentialRepresentation> credentials;
    protected Set<String> disableableCredentialTypes;
    protected List<String> requiredActions;
    protected List<FederatedIdentityRepresentation> federatedIdentities;
    protected List<String> realmRoles;
    protected Map<String, List<String>> clientRoles;
    protected List<UserConsentRepresentation> clientConsents;
    protected Integer notBefore;
    /** @deprecated */
    @Deprecated
    protected Map<String, List<String>> applicationRoles;
    /** @deprecated */
    @Deprecated
    protected List<SocialLinkRepresentation> socialLinks;
    protected List<String> groups;
    private Map<String, Boolean> access;
}
