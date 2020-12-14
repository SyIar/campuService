package cn.edu.fudan.campuservice.controller;

import cn.edu.fudan.campuservice.entity.Response;
import cn.edu.fudan.campuservice.entity.User;
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
        } catch (NoSuchUserException e) {
            return new Response<>("401", "unauthorized", e.getMessage());
        }

        return Response.success(JwtUtil.generateToken(user));
    }

    @GetMapping("/logout")
    public void logout() {

    }

    @PostMapping("/register")
    public Response register(@RequestBody User user) {
        user.setBalance(0);
        userService.saveUser(user);
        return Response.success("insert successfully");
    }

    @GetMapping("/user/{id}")
    public Response userInfo(@PathVariable("id") Integer id) {
        try {
            return Response.success(userService.getUser(id));
        } catch (NoSuchUserException e) {
            return new Response<>("400", "no such user", e.getMessage());
        }
    }
}
