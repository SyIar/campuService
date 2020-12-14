package cn.edu.fudan.campuservice.controller;

import cn.edu.fudan.campuservice.entity.Order;
import cn.edu.fudan.campuservice.entity.Response;
import cn.edu.fudan.campuservice.exception.NoSuchOrderException;
import cn.edu.fudan.campuservice.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

    @GetMapping("/acceptOrder")
    private Response acceptOrder(int orderId, int accepterId) {
        orderService.acceptOrder(orderId, accepterId);
        return Response.success("accept successfully");
    }

    @GetMapping("/finishOrder")
    private Response finishOrder(int orderId) {
        orderService.finishOrder(orderId);
        return Response.success("finish successfully");
    }

    @GetMapping("/confirmOrder")
    private Response confirmOrder(int orderId) {
        orderService.confirmOrder(orderId);
        return Response.success("confirm successfully");
    }

    @GetMapping("/refuseOrder")
    private Response refuseOrder(int orderId) {
        orderService.refuseOrder(orderId);
        return Response.success("refuse successfully");
    }

    @PostMapping("/postOrder")
    private Response insertOrder(Order order) {
        order.setPostTime(new Date());
        order.setStatus(0);
        orderService.saveOrder(order);
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

    @GetMapping("/searchOrderByStartAndDestination")
    private Response searchOrderByStartAndDestination(int start, int destination) {
        return Response.success(orderService.searchOrderByStartAndDestination(start, destination));
    }

    @PostMapping("/uploadPosterPhoto")
    private Response uploadPosterPhoto(MultipartFile file){
        return null;
    }

    @PostMapping("/uploadAccepterPhoto")
    private Response uploadAccepterPhoto(MultipartFile file){
        return null;
    }
}
