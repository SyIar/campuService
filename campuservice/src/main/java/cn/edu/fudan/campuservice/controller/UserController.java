package cn.edu.fudan.campuservice.controller;

import cn.edu.fudan.campuservice.annotation.ParamCheck;
import cn.edu.fudan.campuservice.entity.Response;
import cn.edu.fudan.campuservice.entity.User;
import cn.edu.fudan.campuservice.entity.UserDTO;
import cn.edu.fudan.campuservice.exception.NoSuchUserException;
import cn.edu.fudan.campuservice.service.UserService;
import cn.edu.fudan.campuservice.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public Response login(User user) {
        try {
            if (!userService.authenUser(user)) {
                return new Response<>("401", "unauthorized", "wrong password");
            }
            return Response.success(new UserDTO(userService.getUserByStudentId(user.getStudentId()).get().getUserId(),
                    JwtUtil.generateToken(user)));
        } catch (NoSuchUserException e) {
            return new Response<>("401", "unauthorized", e.getMessage());
        }
    }

    @GetMapping("/logout")
    public void logout() {

    }

    @PostMapping("/register")
    public Response register(@RequestBody User user) {
        if (userService.getUserByStudentId(user.getStudentId()).isPresent()) {
            return new Response<>("400", "register failed", "the studentId has already been registered");
        }
        user.setBalance(-1);
        User savedUser = userService.saveUser(user);
        return Response.success(new UserDTO(savedUser.getUserId(), JwtUtil.generateToken(user)));
    }

    @GetMapping("/user/{id}")
    public Response userInfo(@PathVariable("id") Integer id) {
        try {
            return Response.success(userService.getUser(id));
        } catch (NoSuchUserException e) {
            return new Response<>("400", "no such user", e.getMessage());
        }
    }

    @GetMapping("/user/getUncheckedUsers")
    public Response getUncheckedUsers() {
        return Response.success(userService.getUncheckedUsers());
    }

    @GetMapping("/user/checkPass")
    public Response checkPass(@ParamCheck Integer userId) {
        try {
            userService.checkPass(userId);
            return Response.success("operate successfully");
        } catch (NoSuchUserException e) {
            return new Response<>("400", "failed", e.getMessage());
        }
    }

    @GetMapping("/user/checkFail")
    public Response checkFail(@ParamCheck Integer userId) {
        try {
            userService.checkFail(userId);
            return Response.success("operate successfully");
        } catch (NoSuchUserException e) {
            return new Response<>("400", "failed", e.getMessage());
        }
    }
}
