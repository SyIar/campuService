package cn.edu.fudan.campuservice.service;

import cn.edu.fudan.campuservice.entity.Order;
import cn.edu.fudan.campuservice.exception.NoSuchOrderException;
import cn.edu.fudan.campuservice.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@SuppressWarnings("unchecked")
public class OrderService {
    private final OrderRepository orderRepository;

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

    public Order insertOrder(Order order) {
        return orderRepository.save(order);
    }

    public List<Order> searchOrderByAttribute(String attributeName, Object obj) {
        return orderRepository.findAll((root, criteriaQuery, criteriaBuilder)
                -> criteriaBuilder.equal(root.get(attributeName), obj));
    }


    public void updateOrderStatus(int orderId, int status) {
        Order order = orderRepository.getOne(orderId);
        order.setStatus(status);
        orderRepository.save(order);
    }
}
