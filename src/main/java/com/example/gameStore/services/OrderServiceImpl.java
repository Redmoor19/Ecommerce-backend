package com.example.gameStore.services;

import com.example.gameStore.dtos.OrderDtos.GameOrderDto;
import com.example.gameStore.dtos.OrderDtos.OrderDto;
import com.example.gameStore.dtos.OrderDtos.OrderWithUserDto;
import com.example.gameStore.dtos.UserDtos.UserDto;
import com.example.gameStore.entities.Game;
import com.example.gameStore.entities.GameOrder;
import com.example.gameStore.entities.GameOrderId;
import com.example.gameStore.entities.Order;
import com.example.gameStore.entities.User;
import com.example.gameStore.enums.OrderStatus;
import com.example.gameStore.enums.PaymentStatus;
import com.example.gameStore.repositories.GameOrderRepository;
import com.example.gameStore.repositories.GameRepository;
import com.example.gameStore.repositories.OrderRepository;
import com.example.gameStore.repositories.UserRepository;
import com.example.gameStore.services.interfaces.OrderService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private GameOrderRepository gameOrderRepository;

    @Autowired
    private UserRepository userRepository;

    private final ModelMapper modelMapper = new ModelMapper();

    public List<OrderDto> findAllOrders() {
        List<Order> orders = orderRepository.findAll();
        return orders.stream()
                .map(o -> {
                    OrderDto e = modelMapper.map(o, OrderDto.class);
                    e.setGames(new LinkedList<>());
                    List<GameOrder> gameOrders = gameOrderRepository.findAllByOrderId(o.getId());
                    gameOrders.forEach(go -> e.getGames().add(modelMapper.map(go, GameOrderDto.class)));
                    return e;
                })
                .toList();
    }

    public List<OrderWithUserDto> findAllExtendedOrders() {
        List<Order> orders = orderRepository.findAll();
        return orders.stream()
                .map(o -> {
                    OrderWithUserDto e = modelMapper.map(o, OrderWithUserDto.class);
                    e.setGames(new LinkedList<>());
                    List<GameOrder> gameOrders = gameOrderRepository.findAllByOrderId(o.getId());
                    gameOrders.forEach(go -> e.getGames().add(modelMapper.map(go, GameOrderDto.class)));
                    return e;
                })
                .toList();
    }

    public Optional<OrderDto> findOrderById(String id) {
        Optional<Order> order = orderRepository.findById(UUID.fromString(id));
        return order.map(o -> modelMapper.map(o, OrderDto.class));
    }

    public List<OrderDto> findOrdersByUser(String userId) {
        List<Order> orders = orderRepository.findAllByUserId(UUID.fromString(userId));
        return orders.stream().map(o -> modelMapper.map(o, OrderDto.class)).toList();
    }

    public Optional<OrderDto> findCurrentOrderByUser(UUID userId) {
        Optional<Order> order = orderRepository.findFirstByUserIdAndStatusAndPaymentStatus(userId, OrderStatus.PROCESSING, PaymentStatus.UNPAID);
        return order.map(o -> modelMapper.map(o, OrderDto.class));
    }

    public boolean addGameToOrder(String gameId, String orderId) {
        Game game = gameRepository.findById(UUID.fromString(gameId)).orElse(null);
        Order order = orderRepository.findById(UUID.fromString(orderId)).orElse(null);
        if (game == null || order == null) {
            return false;
        }
        List<GameOrder> gamesOrder = gameOrderRepository.findAllByOrderId(order.getId());
        for (GameOrder gameOrder : gamesOrder) {
            if (gameOrder.getGame().getId().equals(game.getId())) {
                Integer q = gameOrder.getQuantity();
                q++;
                gameOrder.setQuantity(q);
                gameOrderRepository.save(gameOrder);
                return true;
            }
        }
        GameOrder gameOrder = new GameOrder();
        gameOrder.setId(new GameOrderId(game.getId(), order.getId()));
        gameOrder.setGame(game);
        gameOrder.setOrder(order);
        gameOrder.setQuantity(1);
        gameOrderRepository.save(gameOrder);
        return true;
    }

    public Optional<OrderDto> deleteGameFromOrder(UUID gameId, UUID orderId) {
        return Optional.of(new OrderDto(UUID.randomUUID(), UUID.randomUUID(), 100.0, new Timestamp(System.currentTimeMillis()),
                OrderStatus.DELIVERED, PaymentStatus.PAID, Collections.EMPTY_LIST));
    }

    public Optional<OrderDto> cleanCurrentOrder(UUID orderId) {
        return Optional.of(new OrderDto(UUID.randomUUID(), UUID.randomUUID(), 100.0, new Timestamp(System.currentTimeMillis()),
                OrderStatus.DELIVERED, PaymentStatus.PAID, Collections.EMPTY_LIST));
    }

    public Optional<OrderDto> checkoutCurrentOrder() {
        return Optional.of(new OrderDto(UUID.randomUUID(), UUID.randomUUID(), 100.0, new Timestamp(System.currentTimeMillis()),
                OrderStatus.DELIVERED, PaymentStatus.PAID, Collections.EMPTY_LIST));
    }


    public Optional<OrderDto> createNewOrder(Optional<UserDto> userDto) {
        if (userDto.isEmpty()) {
            return Optional.empty();
        }
        Optional<User> user = userRepository.findById(userDto.get().getId());
        if (user.isEmpty()) {
            return Optional.empty();
        }
        Order order = new Order();
        order.setUser(user.get());
//        order.setUserId(userDto.get().getId());
        order.setStatus(OrderStatus.PROCESSING);
        order.setPaymentStatus(PaymentStatus.UNPAID);
        order.setTotalPrice(0.0);
        order.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        Order newOrder = orderRepository.save(order);
        return Optional.of(modelMapper.map(newOrder, OrderDto.class));
    }
}
