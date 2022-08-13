package sanotesuserservice.service.impl;

import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.authorization.client.AuthzClient;
import org.keycloak.authorization.client.Configuration;
import org.keycloak.representations.AccessTokenResponse;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Service;
import sanotesuserservice.config.KeyCloakClientConfig;
import sanotesuserservice.model.UserModel;
import sanotesuserservice.payload.ApiResponse;
import sanotesuserservice.payload.LoginRequest;
import sanotesuserservice.security.Credentials;
import sanotesuserservice.service.UserService;

import javax.ws.rs.core.Response;
import java.util.*;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final KeyCloakClientConfig keyCloakClientConfig;

    private RealmResource getRealmInstance() {
        Keycloak keycloak = keyCloakClientConfig.getInstance();
        RealmResource realmResource = keycloak.realm(keyCloakClientConfig.getRealm());
        return realmResource;
    }


    @Override
    public UserModel addUser(UserModel userModel) {
        CredentialRepresentation credential = Credentials
                .createPasswordCredentials(userModel.getPassword());
        UserRepresentation user = new UserRepresentation();
        user.setUsername(userModel.getUsername());
        user.setFirstName(userModel.getFirstname());
        user.setLastName(userModel.getLastname());
        user.setEmail(userModel.getEmail());
        user.setCredentials(Collections.singletonList(credential));
        user.setEnabled(true);

        RealmResource realmResource = getRealmInstance();
        UsersResource usersResource = realmResource.users();

        Response response = usersResource.create(user);
        String userId = CreatedResponseUtil.getCreatedId(response);
        UserResource userResource = usersResource.get(userId);

        // Get realm level role (requires view-realms role)
        RoleRepresentation sanotesUserRole =
                realmResource.roles().get("sanotes_user").toRepresentation();

        // Assign realm level role to user
        userResource.roles().realmLevel().add(Arrays.asList(sanotesUserRole));
        userModel.setId(userId);
        return userModel;
    }

    @Override
    public AccessTokenResponse loginUser(LoginRequest loginRequest) {
        Map<String, Object> clientCredentials = new HashMap<>();
        clientCredentials.put("secret", keyCloakClientConfig.getClientSecret());
        clientCredentials.put("grant_type", "password");
        clientCredentials.put("scope", "email roles profile");

        Configuration configuration =
                new Configuration(keyCloakClientConfig.getServerUrl(),
                        keyCloakClientConfig.getRealm(),
                        keyCloakClientConfig.getClientId(),
                        clientCredentials, null);
        AuthzClient authzClient = AuthzClient.create(configuration);

        AccessTokenResponse response =
                authzClient.obtainAccessToken(loginRequest.getEmailOrUsername(), loginRequest.getPassword());
        return response;
    }

    @Override
    public List<UserRepresentation> getUserByUserName(String userName) {
        RealmResource realmResource = getRealmInstance();
        UsersResource usersResource = realmResource.users();
        List<UserRepresentation> user = usersResource.search(userName, true);
        return user;
    }

    @Override
    public UserRepresentation getUserById(String userId) {
        RealmResource realmResource = getRealmInstance();
        UsersResource usersResource = realmResource.users();
        UserResource userResource = usersResource.get(userId);
        UserRepresentation user = userResource.toRepresentation();
        return user;
    }

    @Override
    public UserModel updateUser(UserModel userModel) {
        CredentialRepresentation credential = Credentials
                .createPasswordCredentials(userModel.getPassword());
        UserRepresentation user = new UserRepresentation();
        user.setUsername(userModel.getUsername());
        user.setFirstName(userModel.getFirstname());
        user.setLastName(userModel.getLastname());
        user.setEmail(userModel.getEmail());
        user.setCredentials(Collections.singletonList(credential));

        RealmResource realmResource = getRealmInstance();
        UsersResource usersResource = realmResource.users();
        usersResource.get(userModel.getId()).update(user);
        return userModel;
    }

    @Override
    public ApiResponse deleteUser(String userId) {
        RealmResource realmResource = getRealmInstance();
        UsersResource usersResource = realmResource.users();
        usersResource.get(userId).remove();
        return new ApiResponse(Boolean.TRUE, "You successfully delete user ");
    }

    @Override
    public ApiResponse sendVerificationLink(String userId) {
        RealmResource realmResource = getRealmInstance();
        UsersResource usersResource = realmResource.users();
        usersResource.get(userId).sendVerifyEmail();
        return new ApiResponse(Boolean.TRUE, "Verification link send to the user email");
    }

    @Override
    public ApiResponse sendResetPassword(String userId) {
        RealmResource realmResource = getRealmInstance();
        UsersResource usersResource = realmResource.users();
        usersResource.get(userId).executeActionsEmail(Arrays.asList("UPDATE_PASSWORD"));
        return new ApiResponse(Boolean.TRUE, "Password reset link send to the user email");
    }
}
