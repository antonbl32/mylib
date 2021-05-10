package by.library.mylib.controller;

import by.library.mylib.dto.UserDTO;
import by.library.mylib.entity.User;
import by.library.mylib.facade.UserFacade;
import by.library.mylib.service.UserService;
import by.library.mylib.validation.ResponseErrorValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;

/**
 * This controller for user editing
 */
@RestController
@RequestMapping("api/user")
@CrossOrigin
public class UserController {
    private UserService userService;
    private UserFacade userFacade;
    private ResponseErrorValidation responseErrorValidation;
    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }
    @Autowired
    public void setUserFacade(UserFacade userFacade) {
        this.userFacade = userFacade;
    }
    @Autowired
    public void setResponseErrorValidation(ResponseErrorValidation responseErrorValidation) {
        this.responseErrorValidation = responseErrorValidation;
    }

    /**
     * @param principal current user
     * @return current user details
     */
    @GetMapping("/")
    public ResponseEntity<UserDTO> getCurrentUserDTO(Principal principal) {
        return new ResponseEntity<>(userFacade.userToUserDTO((User) userService
                .getCurrentUser(principal)), HttpStatus.OK);
    }

    /**
     *
     * @param userId id user
     * @return user details by id
     */
    @GetMapping("/{userid}")
    public ResponseEntity<UserDTO> getUserProfile(@PathVariable("userid") String userId) {
        User user = userService.getUserById(Long.parseLong(userId));
        return new ResponseEntity<>(userFacade.userToUserDTO(user), HttpStatus.OK);
    }

    /**
     *
     * @param userDTO facade from user to client
     * @param principal current user details from client
     * @param bindingResult validation from client information
     * @return updated user information
     */
    public ResponseEntity<Object> updateUser(@Valid @RequestBody UserDTO userDTO, Principal principal, BindingResult bindingResult) {
        ResponseEntity<Object> error=responseErrorValidation.mapValidationService(bindingResult);
        if(!ObjectUtils.isEmpty(error)) return error;
        User user=userService.updateUser(userDTO,principal);
        UserDTO updatedUser=userFacade.userToUserDTO(user);
        return new ResponseEntity<>(updatedUser,HttpStatus.OK);
    }

}
