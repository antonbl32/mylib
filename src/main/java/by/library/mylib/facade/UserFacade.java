package by.library.mylib.facade;

import by.library.mylib.dto.UserDTO;
import by.library.mylib.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserFacade {
    public UserDTO userToUserDTO(User user){
        UserDTO userDTO=new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setUsername(user.getUsername());
        userDTO.setLastname(user.getLastname());
        return userDTO;
    }
}
