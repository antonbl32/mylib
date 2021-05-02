package by.library.mylib.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "note")
@Data
public class Note {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "name")
    private String name;
    @Column(name = "createtime")
    private LocalDateTime createTime;
    @Column(name = "description")
    private String desc;
    @ManyToOne(fetch = FetchType.EAGER)
    private User user;
    @PrePersist
    protected void onCreate(){
        this.createTime=LocalDateTime.now();
    }
}
