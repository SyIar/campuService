package cn.edu.fudan.campuservice.service;


import cn.edu.fudan.campuservice.entity.User;
import cn.edu.fudan.campuservice.exception.NoSuchUserException;
import cn.edu.fudan.campuservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User insertUser(User user) {
        return userRepository.save(user);
    }

    public User getUser(Integer id) throws NoSuchUserException {
        try {
            return userRepository.getOne(id);
        } catch (Exception e) {
            throw new NoSuchUserException();
        }
    }

    public boolean authenUser(User user) throws NoSuchUserException {
        User target = getUser(user.getUserId());
        return target.getPassword().equals(user.getPassword());
    }
}
