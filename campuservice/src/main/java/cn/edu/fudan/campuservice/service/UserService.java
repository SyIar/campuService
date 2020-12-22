package cn.edu.fudan.campuservice.service;


import cn.edu.fudan.campuservice.entity.User;
import cn.edu.fudan.campuservice.exception.NoSuchUserException;
import cn.edu.fudan.campuservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User saveUser(User user) {
        return userRepository.save(user);
    }

    public User getUser(Integer id) throws NoSuchUserException {
        try {
            return userRepository.getOne(id);
        } catch (Exception e) {
            throw new NoSuchUserException();
        }
    }

    public Optional<User> getUserByStudentId(Integer studentId) {
        User user = new User();
        user.setStudentId(studentId);
        return userRepository.findOne(Example.of(user));
    }

    public boolean authenUser(User user) throws NoSuchUserException {
        Optional<User> target = getUserByStudentId(user.getStudentId());
        if (!target.isPresent()) {
            throw new NoSuchUserException();
        }
        return target.get().getPassword().equals(user.getPassword());
    }
}
