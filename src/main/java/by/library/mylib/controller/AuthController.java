package by.library.mylib.controller;

import by.library.mylib.payload.request.LoginRequest;
import by.library.mylib.payload.request.SignupRequest;
import by.library.mylib.payload.response.JWTTokenSuccessResponse;
import by.library.mylib.payload.response.MessageResponse;
import by.library.mylib.security.JWTTokenProvider;
import by.library.mylib.security.SecurityConstants;
import by.library.mylib.service.UserService;
import by.library.mylib.validation.ResponseErrorValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin
@RestController
@RequestMapping("/api/auth")
@PreAuthorize(value = "permitAll()")
public class AuthController {

    private ResponseErrorValidation responseErrorValidation;

    @Autowired
    public void setResponseErrorValidation(ResponseErrorValidation responseErrorValidation) {
        this.responseErrorValidation = responseErrorValidation;
    }

    private UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    private AuthenticationManager authenticationManager;

    @Autowired
    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    private JWTTokenProvider jwtTokenProvider;

    @Autowired
    public void setJwtTokenProvider(JWTTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    /**
     * Enter to authorization with request
     *
     * @param loginRequest request for checking user
     * @param result
     * @return error if denied or 200 with authorization token
     */
    @PostMapping("/signin")
    public ResponseEntity<Object> authenticateUser(@Valid @RequestBody LoginRequest loginRequest,
                                                   BindingResult result) {
        ResponseEntity<Object> errors = responseErrorValidation.mapValidationService(result);
        if (!ObjectUtils.isEmpty(errors)) {
            return errors;
        }
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginRequest.getUsername(), loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = SecurityConstants.TOKEN_PREFIX + jwtTokenProvider.generateToken(authentication);
        return ResponseEntity.ok(new JWTTokenSuccessResponse(true, jwt));
    }

    /**
     * Registration with request
     *
     * @param signupRequest request for registration user.
     * @param result        Object with new User
     * @return 200 or error
     */
    @PostMapping("/signup")
    public ResponseEntity<Object> registerUser(@Valid @RequestBody SignupRequest signupRequest, BindingResult result) {
        ResponseEntity<Object> errors = responseErrorValidation.mapValidationService(result);
        if (!ObjectUtils.isEmpty(errors)) return errors;
        userService.createUser(signupRequest);
        return ResponseEntity.ok(new MessageResponse("User registered successfully"));
    }
}
