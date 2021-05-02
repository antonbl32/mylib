package by.library.mylib.entity;

import by.library.mylib.entity.enums.ERole;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.*;

@Entity
@Table(name = "user")
@Data
@NoArgsConstructor
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "name",unique = true,nullable = false)
    private String username;
    @Column(name = "password",nullable = false,length = 3000)
    private String password;
    @Column(name = "createtime",updatable = false)
    @JsonFormat(pattern = "yyyy-mm-dd HH:mm:ss")
    private LocalDateTime createTime;
    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.LAZY,mappedBy = "user",orphanRemoval = true)
    private List<Note> notes=new ArrayList<>();
    @ElementCollection(targetClass = ERole.class)
    @CollectionTable(name = "user_role",joinColumns = @JoinColumn(name = "user_id"))
    private Set<ERole> roles=new HashSet<>();
//    set time before save to base
    @PrePersist
    protected void onCreate(){
        this.createTime=LocalDateTime.now();
    }
    @Transient
    private Collection<? extends GrantedAuthority> authorities;

    public User(Long id,String name, String password, Collection<? extends GrantedAuthority> authorities) {
        this.id=id;
        this.username = name;
        this.password = password;
        this.authorities = authorities;
    }

    /**
     * SECURITY
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }
    @Override
    public String getPassword(){
        return password;
    }
    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
