package cn.edu.fudan.campuservice.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.hibernate.annotations.Proxy;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "my_order")
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler" })
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
    private String posterId;

    @Column(name = "accepter_id")
    private String accepterId;

    @Column(name = "post_time")
    private Date postTime;

    @Column(name = "accept_time")
    private Date acceptTime;

    @Column(name = "finish_time")
    private Date finishTime;

    @Column(name = "confirm_time")
    private Date confirmTime;

    private int status;
}
