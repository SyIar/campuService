package cn.edu.fudan.campuservice.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Proxy;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "my_order")
@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler"})
@DynamicUpdate
@Proxy(lazy = false)
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private int orderId;

    private int start;
    private int destination;
    private String description;
    private int size;
    private int price;
    @Column(name = "poster_id")
    private Integer posterId;

    @Column(name = "accepter_id")
    private Integer accepterId;

    @Column(name = "post_time")
    private Date postTime;

    @Column(name = "accept_time")
    private Date acceptTime;

    @Column(name = "finish_time")
    private Date finishTime;

    @Column(name = "confirm_time")
    private Date confirmTime;

    @Column(name = "refuse_time")
    private Date refuseTime;

    private int status;

    @Column(name = "poster_photo")
    private String posterPhotoUrl;

    @Column(name = "accepter_photo")
    private String accepterPhotoUrl;
}
