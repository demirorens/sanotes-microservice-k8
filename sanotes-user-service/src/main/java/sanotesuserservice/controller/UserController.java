package sanotesuserservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.keycloak.representations.idm.UserRepresentation;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.web.bind.annotation.*;
import sanotesuserservice.model.UserModel;
import sanotesuserservice.payload.*;
import sanotesuserservice.security.CurrentUser;
import sanotesuserservice.service.UserService;

import java.util.Arrays;
import java.util.List;


@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    private final ModelMapper modelMapper;

    @PostMapping
    @PreAuthorize("hasAuthority('sanotes_admin')")
    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<UserResponse> addUser(@RequestBody UserRequest userRequest) {
        UserModel userModel = modelMapper.map(userRequest, UserModel.class);
        userModel = userService.addUser(userModel);
        UserResponse userResponse = modelMapper.map(userModel, UserResponse.class);
        return new ResponseEntity<>(userResponse, HttpStatus.CREATED);
    }

    @GetMapping(path = "/byUserName/{userName}")
    @PreAuthorize("hasAuthority('sanotes_admin')")
    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<List<UserDetailResponse>> getUserByUserName(@PathVariable("userName") String userName) {
        List<UserRepresentation> users = userService.getUserByUserName(userName);
        List<UserDetailResponse> userResponses = Arrays.asList(modelMapper.map(users, UserDetailResponse[].class));
        return new ResponseEntity<>(userResponses, HttpStatus.OK);
    }

    @GetMapping(path = "/byUserId/{userId}")
    @PreAuthorize("hasAuthority('sanotes_admin')")
    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<UserDetailResponse> getUserById(@PathVariable("userId") String userId) {
        UserRepresentation user = userService.getUserById(userId);
        UserDetailResponse userResponse = modelMapper.map(user, UserDetailResponse.class);
        return new ResponseEntity<>(userResponse, HttpStatus.OK);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('sanotes_user')")
    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<UserResponse> getSelf(
            @Parameter(hidden = true)
            @CurrentUser OAuth2AuthenticatedPrincipal userPrincipal) {
        UserRepresentation user = userService.getUserById(userPrincipal.getName());
        UserResponse userResponse = modelMapper.map(user, UserResponse.class);
        return new ResponseEntity<>(userResponse, HttpStatus.OK);
    }

    @PutMapping
    @PreAuthorize("hasAuthority('sanotes_admin')")
    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<UserResponse> updateUser(@RequestBody UserRequest userRequest) {
        UserModel userModel = modelMapper.map(userRequest, UserModel.class);
        userModel = userService.updateUser(userModel);
        UserResponse userResponse = modelMapper.map(userModel, UserResponse.class);
        return new ResponseEntity<>(userResponse, HttpStatus.CREATED);
    }

    @DeleteMapping
    @PreAuthorize("hasAuthority('sanotes_admin')")
    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<ApiResponse> deleteUser(@RequestBody ByIdRequest request) {
        ApiResponse apiResponse = userService.deleteUser(request.getId());
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @GetMapping(path = "/verification-link/{userId}")
    @PreAuthorize("hasAuthority('sanotes_admin')")
    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<ApiResponse> sendVerificationLink(@PathVariable("userId") String userId) {
        ApiResponse apiResponse = userService.sendVerificationLink(userId);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @GetMapping(path = "/reset-password/{userId}")
    @PreAuthorize("hasAuthority('sanotes_admin')")
    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<ApiResponse> sendResetPassword(@PathVariable("userId") String userId) {
        ApiResponse apiResponse = userService.sendResetPassword(userId);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

}
