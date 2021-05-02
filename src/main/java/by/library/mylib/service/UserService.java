package by.library.mylib.service;

import by.library.mylib.entity.User;
import by.library.mylib.entity.enums.ERole;
import by.library.mylib.exceptions.UserExistException;
import by.library.mylib.payload.request.SignupRequest;
import by.library.mylib.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    public static final Logger LOG= LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    public UserService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }
    public User createUser(SignupRequest userIn){
        User user=new User();
        user.setUsername(userIn.getUsername());
        user.setPassword(bCryptPasswordEncoder.encode(userIn.getPassword()));
        user.getRoles().add(ERole.ROLE_USER);
        try{
            LOG.info("Saving user {}",userIn.getUsername());
            return userRepository.save(user);
        }catch (Exception ex){
            LOG.error("Error during registration {}",ex.getMessage());
            throw new UserExistException("The user"+ user.getUsername()+
            " already exists.");
        }


    }
}
