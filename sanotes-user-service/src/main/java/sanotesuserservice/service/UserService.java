package sanotesuserservice.service;


import org.keycloak.representations.AccessTokenResponse;
import org.keycloak.representations.idm.UserRepresentation;
import sanotesuserservice.model.UserModel;
import sanotesuserservice.payload.ApiResponse;
import sanotesuserservice.payload.LoginRequest;

import java.util.List;

public interface UserService {

    UserModel addUser(UserModel userModel);

    List<UserRepresentation> getUserByUserName(String userName);

    UserRepresentation getUserById(String userName);

    UserModel updateUser(UserModel userModel);

    ApiResponse deleteUser(String userId);

    ApiResponse sendVerificationLink(String userId);

    AccessTokenResponse loginUser(LoginRequest loginRequest);

    ApiResponse sendResetPassword(String userId);
}
