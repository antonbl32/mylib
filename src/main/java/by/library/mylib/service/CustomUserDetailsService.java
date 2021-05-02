package by.library.mylib.service;

import by.library.mylib.entity.User;
import by.library.mylib.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    @Autowired
    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String s) {
        User user=userRepository.getUserByUsername(s).orElseThrow(()->
                new UsernameNotFoundException("User not found with "+s));
        return build(user);
    }
    public User findById(Long id){
        return userRepository.getUserById(id).orElse(null);
    }
    public static User build(User user){
        List<GrantedAuthority> authorities=user.getRoles().stream()
                .map(r->new SimpleGrantedAuthority(r.name()))
                .collect(Collectors.toList());
        return new User(user.getId(),user.getUsername()
                ,user.getPassword(),authorities);
    }
}
