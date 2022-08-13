package sanotesuserservice.controller;

import lombok.RequiredArgsConstructor;
import org.keycloak.representations.AccessTokenResponse;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sanotesuserservice.model.UserModel;
import sanotesuserservice.payload.*;
import sanotesuserservice.service.UserService;

import javax.validation.Valid;


@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class LoginController {

    private final UserService userService;

    private final ModelMapper modelMapper;

    @PostMapping("/login")
    public ResponseEntity<JwtAuthenticationResponse> loginUser(@Valid @RequestBody LoginRequest loginRequest) {
        AccessTokenResponse response;
        try {
            response = userService.loginUser(loginRequest);
        }catch (Exception e){
            response = new AccessTokenResponse();
            response.setError(e.getMessage());
        }
        JwtAuthenticationResponse jwtAuthenticationResponse = modelMapper.map(response, JwtAuthenticationResponse.class);
        return ResponseEntity.ok(jwtAuthenticationResponse);
    }

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse> signupUser(@Valid @RequestBody SignUpRequest signUpRequest) {
        UserModel userModel = modelMapper.map(signUpRequest, UserModel.class);
        userModel = userService.addUser(userModel);
        return ResponseEntity.ok(new ApiResponse(Boolean.TRUE, "User signed up successfully."));
    }

}
