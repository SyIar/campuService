package cn.edu.fudan.campuservice.entity;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class OrderDTO {
    private Order order;
    private MultipartFile file;
}
