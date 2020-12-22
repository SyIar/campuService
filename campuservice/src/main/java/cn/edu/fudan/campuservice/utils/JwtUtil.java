package cn.edu.fudan.campuservice.utils;

import cn.edu.fudan.campuservice.entity.User;
import cn.edu.fudan.campuservice.service.UserService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtil {
    @Autowired
    private UserService userService;

    private static JwtUtil jwtUtil;

    @PostConstruct
    public void init() {
        jwtUtil = this;
        jwtUtil.userService = this.userService;
    }

    private static final String SECRET = "project management";

    public static String generateToken(User user) {
        HashMap<String, Object> body = new HashMap<>();
        body.put("studentId", user.getStudentId());
        body.put("password", user.getPassword());
        return Jwts.builder()
                .setClaims(body)
                .setExpiration(new Date(System.currentTimeMillis() + 3600_000))
                .signWith(SignatureAlgorithm.HS512, SECRET)
                .compact();
    }

    public static User validateToken(String token) {
        if (token == null || "".equals(token)) {
            throw new IllegalStateException("unauthorized");
        }

        try {
            Map<String, Object> body = Jwts.parser()
                    .setSigningKey(SECRET)
                    .parseClaimsJws(token)
                    .getBody();
            User user = new User();
            user.setStudentId((int) body.get("studentId"));
            user.setPassword((String) body.get("password"));
            System.out.println(user);
            System.out.println(jwtUtil.userService.authenUser(user));
            if (!jwtUtil.userService.authenUser(user)) {
                throw new Exception("wrong password");
            }
            return user;
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalStateException("Invalid Token " + e.getMessage());
        }
    }
}
