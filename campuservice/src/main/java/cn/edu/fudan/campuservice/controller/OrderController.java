package cn.edu.fudan.campuservice.controller;

import cn.edu.fudan.campuservice.entity.Order;
import cn.edu.fudan.campuservice.entity.Response;
import cn.edu.fudan.campuservice.exception.NoSuchOrderException;
import cn.edu.fudan.campuservice.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @GetMapping("/{orderId}")
    private Response getOrderById(@PathVariable Integer orderId) {
        try {
            return Response.success(orderService.getOrder(orderId));
        } catch (NoSuchOrderException e) {
            return new Response<>("400", "wrong orderId", e.getMessage());
        }
    }

    @GetMapping("/finishOrder")
    private Response finishOrder(int orderId) {
        orderService.updateOrderStatus(orderId, 3);
        return Response.success("finish successfully");
    }

    @GetMapping("/confirmOrder")
    private Response confirmOrder(int orderId) {
        orderService.updateOrderStatus(orderId, 2);
        return Response.success("confirm successfully");
    }

    @PostMapping("/postOrder")
    private Response insertOrder(Order order) {
        order.setPostTime(new Date());
        order.setStatus(0);
        orderService.insertOrder(order);
        return Response.success("insert successfully");
    }

    @GetMapping("/getOrderByPoster")
    private Response getOrderByPoster(int posterId) {
        return Response.success(orderService.searchOrderByAttribute("posterId", posterId));
    }

    @GetMapping("/getOrderByAccepter")
    private Response getOrderByAccepter(int accepterId) {
        return Response.success(orderService.searchOrderByAttribute("accepterId", accepterId));
    }

    @GetMapping("/searchOrderByStart")
    private Response searchOrderByStart(int start) {
        return Response.success(orderService.searchOrderByAttribute("start", start));
    }

    @GetMapping("/searchOrderByDestination")
    private Response searchOrderByDestination(int destination) {
        return Response.success(orderService.searchOrderByAttribute("destination", destination));
    }
}
