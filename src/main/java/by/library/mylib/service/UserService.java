package by.library.mylib.service;

import by.library.mylib.dto.UserDTO;
import by.library.mylib.entity.User;
import by.library.mylib.entity.enums.ERole;
import by.library.mylib.exceptions.UserExistException;
import by.library.mylib.payload.request.SignupRequest;
import by.library.mylib.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;

@Service
public class UserService {
    public static final Logger LOG = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public User createUser(SignupRequest userIn) {
        User user = new User();
        user.setUsername(userIn.getUsername());
        user.setPassword(bCryptPasswordEncoder.encode(userIn.getPassword()));
        user.getRoles().add(ERole.ROLE_USER);
        try {
            LOG.info("Saving user {}", userIn.getUsername());
            return userRepository.save(user);
        } catch (Exception ex) {
            LOG.error("Error during registration {}", ex.getMessage());
            throw new UserExistException("The user" + user.getUsername() +
                    " already exists.");
        }
    }

    public User updateUser(UserDTO userDTO, Principal principal) {
        User user = getUserFromPrincipal(principal);
        user.setLastname(userDTO.getLastname());
        LOG.info("Update user " + user.getUsername());
        return userRepository.save(user);
    }

    public User getCurrentUser(Principal principal){
        return getUserFromPrincipal(principal);
    }
    /**
     * Map input User from principal to User object
     *
     * @param principal
     * @return User or error
     */
    private User getUserFromPrincipal(Principal principal) {
        String username = principal.getName();
        return userRepository.getUserByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User with name " + username + " not found."));
    }

    public User getUserById(long id) {
        LOG.info("Get user by id "+id);
        return userRepository.getUserById(id).orElseThrow(()->new UsernameNotFoundException("User with id "+id+" not found."));
    }
}
