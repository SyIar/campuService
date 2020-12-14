package cn.edu.fudan.campuservice.service;

import cn.edu.fudan.campuservice.entity.Order;
import cn.edu.fudan.campuservice.entity.User;
import cn.edu.fudan.campuservice.exception.NoSuchOrderException;
import cn.edu.fudan.campuservice.exception.NoSuchUserException;
import cn.edu.fudan.campuservice.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@SuppressWarnings("unchecked")
public class OrderService {
    private final OrderRepository orderRepository;

    @Autowired
    private UserService userService;

    @Autowired
    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public Order getOrder(Integer id) throws NoSuchOrderException {
        try {
            return orderRepository.getOne(id);
        } catch (Exception e) {
            throw new NoSuchOrderException();
        }
    }

    public Order saveOrder(Order order) {
        return orderRepository.save(order);
    }

    public List<Order> searchOrderByAttribute(String attributeName, Object obj) {
        return orderRepository.findAll((root, criteriaQuery, criteriaBuilder)
                -> criteriaBuilder.equal(root.get(attributeName), obj));
    }

    public List<Order> searchOrderByStartAndDestination(int start, int destination) {
        List<Order> res = orderRepository.findAll((root, criteriaQuery, criteriaBuilder)
                -> criteriaBuilder.and(criteriaBuilder.equal(root.get("start"), start),
                criteriaBuilder.equal(root.get("destination"), destination)));
        res.addAll(orderRepository.findAll((root, criteriaQuery, criteriaBuilder)
                -> criteriaBuilder.and(criteriaBuilder.equal(root.get("start"), start),
                criteriaBuilder.notEqual(root.get("destination"), destination))));
        res.addAll(orderRepository.findAll((root, criteriaQuery, criteriaBuilder)
                -> criteriaBuilder.and(criteriaBuilder.equal(root.get("destination"), destination),
                criteriaBuilder.notEqual(root.get("start"), start))));
        return res;
    }

    public List<Order> searchPostedOrder(int userId) {
        return orderRepository.findAll((root, criteriaQuery, criteriaBuilder)
                -> criteriaBuilder.and(criteriaBuilder.notEqual(root.get("posterId"), userId),
                criteriaBuilder.equal(root.get("status"), 0)));
    }

    public List<Order> getRunningOrder(int userId) {
        return orderRepository.findAll((root, criteriaQuery, criteriaBuilder)
                -> criteriaBuilder.and(criteriaBuilder.equal(root.get("accepter"), userId),
                criteriaBuilder.or(criteriaBuilder.equal(root.get("status"), 1), criteriaBuilder.equal(root.get("status"), 2))));
    }

    public List<Order> getWaitingOrder(int userId) {
        return orderRepository.findAll((root, criteriaQuery, criteriaBuilder)
                -> criteriaBuilder.and(criteriaBuilder.equal(root.get("posterId"), userId),
                criteriaBuilder.or(criteriaBuilder.equal(root.get("status"), 0),
                        criteriaBuilder.equal(root.get("status"), 1), criteriaBuilder.equal(root.get("status"), 2))));
    }

    public void acceptOrder(int orderId, int accepterId) {
        Order order = orderRepository.getOne(orderId);
        order.setStatus(1);
        order.setAcceptTime(new Date());
        order.setAccepterId(accepterId);
        orderRepository.save(order);
    }

    public void finishOrder(int orderId) {
        Order order = orderRepository.getOne(orderId);
        order.setStatus(2);
        order.setFinishTime(new Date());
        orderRepository.save(order);
    }

    public void confirmOrder(int orderId) {
        Order order = orderRepository.getOne(orderId);
        order.setStatus(3);
        order.setConfirmTime(new Date());
        int price = order.getPrice();
        try {
            User poster = userService.getUser(order.getAccepterId());
            User accepter = userService.getUser(order.getPosterId());
            poster.setBalance(poster.getBalance() - price);
            accepter.setBalance(poster.getBalance() + price);
            userService.saveUser(poster);
            userService.saveUser(accepter);
        } catch (NoSuchUserException e) {
            e.printStackTrace();
        }
        orderRepository.save(order);
    }

    public void refuseOrder(int orderId) {
        Order order = orderRepository.getOne(orderId);
        order.setStatus(4);
        order.setRefuseTime(new Date());
        orderRepository.save(order);
    }
}
