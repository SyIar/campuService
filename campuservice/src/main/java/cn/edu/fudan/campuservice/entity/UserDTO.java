package cn.edu.fudan.campuservice.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserDTO {
    private int userId;
    private String userName;
    private String token;
}
