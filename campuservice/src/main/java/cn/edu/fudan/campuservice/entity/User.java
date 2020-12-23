package cn.edu.fudan.campuservice.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.hibernate.annotations.Proxy;

import javax.persistence.*;

@Data
@Entity
@Table(name = "user")
@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler"})
@Proxy(lazy = false)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "user_name")
    private String userName;

    private String password;

    private Integer balance;

    @Column(name = "student_id")
    private String studentId;

    @Column(name = "student_name")
    private String studentName;

    @Column(name = "is_admin")
    private Boolean isAdmin;

    @Column(name = "user_photo")
    private String userPhoto;
}
