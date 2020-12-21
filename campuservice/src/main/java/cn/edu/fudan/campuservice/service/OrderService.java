package cn.edu.fudan.campuservice.service;

import cn.edu.fudan.campuservice.entity.Order;
import cn.edu.fudan.campuservice.entity.User;
import cn.edu.fudan.campuservice.exception.NoSuchOrderException;
import cn.edu.fudan.campuservice.exception.NoSuchUserException;
import cn.edu.fudan.campuservice.repository.OrderRepository;
import cn.edu.fudan.campuservice.utils.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.criteria.Predicate;
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

    public void updateAccepterPhoto(Integer id, MultipartFile file) throws Exception {
        String fileName = FileUtil.saveFile(file);
        Order order = orderRepository.getOne(id);
        order.setAccepterPhotoUrl(fileName);
        orderRepository.save(order);
    }

    public void updatePosterPhoto(Integer id, MultipartFile file) throws Exception {
        String fileName = FileUtil.saveFile(file);
        Order order = orderRepository.getOne(id);
        order.setPosterPhotoUrl(fileName);
        orderRepository.save(order);
    }

    public List<Order> searchOrderByAttribute(String attributeName, Object obj) {
        return orderRepository.findAll((root, criteriaQuery, criteriaBuilder)
                -> criteriaBuilder.equal(root.get(attributeName), obj));
    }

    public List<Order> searchOrderByPoster(int posterId) {
        return orderRepository.findAll((root, criteriaQuery, criteriaBuilder)
                -> {
            Predicate status3 = criteriaBuilder.equal(root.get("status"), 3);
            Predicate status4 = criteriaBuilder.equal(root.get("status"), 4);
            return criteriaBuilder.and(criteriaBuilder.equal(root.get("posterId"), posterId),
                    criteriaBuilder.or(status3, status4));
        });
    }

    public List<Order> searchOrderByAccepter(int accepterId) {
        return orderRepository.findAll((root, criteriaQuery, criteriaBuilder)
                -> {
            Predicate status3 = criteriaBuilder.equal(root.get("status"), 3);
            Predicate status4 = criteriaBuilder.equal(root.get("status"), 4);
            return criteriaBuilder.and(criteriaBuilder.equal(root.get("accepterId"), accepterId),
                    criteriaBuilder.or(status3, status4));
        });
    }

    public List<Order> search(int userId, int start, int destination) {
        return orderRepository.findAll((root, criteriaQuery, criteriaBuilder)
                -> {
            Predicate equalStart = criteriaBuilder.equal(root.get("start"), start);
            Predicate notEqualStart = criteriaBuilder.notEqual(root.get("start"), start);
            Predicate equalDestination = criteriaBuilder.equal(root.get("destination"), destination);
            Predicate notEqualDestination = criteriaBuilder.notEqual(root.get("destination"), destination);
            Predicate notEqualPosterId = criteriaBuilder.notEqual(root.get("posterId"), userId);
            Predicate equalStatus = criteriaBuilder.equal(root.get("status"), 0);

            Predicate p1 = criteriaBuilder.and(equalStart, equalDestination);
            Predicate p2 = criteriaBuilder.and(equalStart, notEqualDestination);
            Predicate p3 = criteriaBuilder.and(equalDestination, notEqualStart);
            return criteriaBuilder.and(notEqualPosterId, equalStatus, criteriaBuilder.or(p1, p2, p3));
        });
    }

    public List<Order> getRunningOrder(int userId) {
        return orderRepository.findAll((root, criteriaQuery, criteriaBuilder)
                -> criteriaBuilder.and(criteriaBuilder.equal(root.get("accepterId"), userId),
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
}
