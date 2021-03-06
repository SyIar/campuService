package cn.edu.fudan.campuservice.service;


import cn.edu.fudan.campuservice.entity.User;
import cn.edu.fudan.campuservice.exception.NoSuchUserException;
import cn.edu.fudan.campuservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.util.List;
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

    public Optional<User> getUserByStudentId(String studentId) {
        User user = new User();
        user.setStudentId(studentId);
        return userRepository.findOne(Example.of(user));
    }

    public boolean authenUser(User user) throws NoSuchUserException {
        User target = getUser(user.getUserId());
        return target.getPassword().equals(user.getPassword());
    }

    public List<User> getUncheckedUsers() {
        return userRepository.findAll((root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.equal(root.get("balance"), -1));
    }

    public void checkPass(Integer userId) throws NoSuchUserException {
        try {
            User user = userRepository.getOne(userId);
            user.setBalance(100);
            userRepository.save(user);
        } catch (Exception e) {
            throw new NoSuchUserException();
        }
    }

    public void checkFail(Integer userId) throws NoSuchUserException {
        try {
            User user = userRepository.getOne(userId);
            user.setBalance(-2);
            userRepository.save(user);
        } catch (Exception e) {
            throw new NoSuchUserException();
        }
    }

    public List<User> getUsers() {
        return userRepository.findAll((root, criteriaQuery, criteriaBuilder)
                -> criteriaBuilder.greaterThanOrEqualTo(root.get("balance"), 0));
    }
}
