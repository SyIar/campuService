package cn.edu.fudan.campuservice.repository;


import cn.edu.fudan.campuservice.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
}
