package cn.edu.fudan.campuservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class CampuserviceApplication {

    public static void main(String[] args) {
        SpringApplication.run(CampuserviceApplication.class, args);
    }

}
