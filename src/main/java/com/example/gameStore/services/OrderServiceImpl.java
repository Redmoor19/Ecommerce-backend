package com.example.gameStore.services;

import com.example.gameStore.dtos.OrderDtos.GameOrderDto;
import com.example.gameStore.dtos.OrderDtos.OrderDto;
import com.example.gameStore.dtos.OrderDtos.OrderWithUserDto;
import com.example.gameStore.dtos.UserDtos.UserDto;
import com.example.gameStore.entities.Game;
import com.example.gameStore.entities.GameOrder;
import com.example.gameStore.entities.GameOrderId;
import com.example.gameStore.entities.Key;
import com.example.gameStore.entities.Order;
import com.example.gameStore.entities.User;
import com.example.gameStore.enums.OrderStatus;
import com.example.gameStore.enums.PaymentStatus;
import com.example.gameStore.enums.UserStatus;
import com.example.gameStore.repositories.GameOrderRepository;
import com.example.gameStore.repositories.GameRepository;
import com.example.gameStore.repositories.KeyRepository;
import com.example.gameStore.repositories.OrderRepository;
import com.example.gameStore.repositories.UserRepository;
import com.example.gameStore.services.interfaces.EmailService;
import com.example.gameStore.services.interfaces.OrderService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private GameRepository gameRepository;
    @Autowired
    private EmailService emailService;
    @Autowired
    private KeyRepository keyRepository;
    @Autowired
    private GameOrderRepository gameOrderRepository;
    @Autowired
    private UserRepository userRepository;

    private final ModelMapper modelMapper = new ModelMapper();

    public List<OrderDto> findAllOrders() {
        List<Order> orders = orderRepository.findAll();
        return orders.stream()
                .map(this::mapOrderToOrderDto)
                .toList();
    }

    public List<OrderWithUserDto> findAllExtendedOrders() {
        List<Order> orders = orderRepository.findAll();
        return orders.stream()
                .map(this::mapOrderToOrderWithUserDto)
                .toList();
    }

    public Optional<OrderDto> findOrderById(String id) {
        Optional<Order> order = orderRepository.findById(UUID.fromString(id));
        return order.map(this::mapOrderToOrderDto);
    }

    public List<OrderDto> findOrdersByUser(String userId) {
        List<Order> orders = orderRepository.findAllByUserId(UUID.fromString(userId));
        return orders.stream()
                .map(this::mapOrderToOrderDto)
                .toList();
    }

    public Optional<OrderDto> findCurrentOrderByUser(String userId) {
        Optional<Order> order = orderRepository.findFirstByUserIdAndStatusAndPaymentStatus(UUID.fromString(userId), OrderStatus.PROCESSING, PaymentStatus.UNPAID);
        return order.map(this::mapOrderToOrderDto);
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

    public Optional<OrderDto> deleteGameFromOrder(String gameId, String orderId) {
        Optional<GameOrder> gamesInOrder = gameOrderRepository.findFirstByOrderIdAndGameId(UUID.fromString(orderId), UUID.fromString(gameId));
        if (gamesInOrder.isEmpty()) {
            return Optional.empty();
        }
        if (gamesInOrder.get().getQuantity() > 1) {
            Integer q = gamesInOrder.get().getQuantity();
            q--;
            gamesInOrder.get().setQuantity(q);
            gameOrderRepository.save(gamesInOrder.get());
            return Optional.of(mapOrderToOrderDto(orderRepository.findById(UUID.fromString(orderId)).get()));
        }

        gameOrderRepository.deleteByGameIdAndOrderId(UUID.fromString(gameId), UUID.fromString(orderId));
        return Optional.of(mapOrderToOrderDto(orderRepository.findById(UUID.fromString(orderId)).get()));
    }

    @Transactional
    public Optional<OrderDto> cleanCurrentOrder(String orderId) {
        Optional<Order> order = orderRepository.findById(UUID.fromString(orderId));
        if (order.isEmpty()) {
            return Optional.empty();
        }
        gameOrderRepository.deleteByOrderId(UUID.fromString(orderId));
        return Optional.of(mapOrderToOrderDto(order.get()));
    }

    public Optional<OrderDto> checkoutCurrentOrder(String userId) {
        Optional<User> user = userRepository.findById(UUID.fromString(userId));
        if (user.isEmpty()) {
            return Optional.empty();
        }
        if (user.get().getActiveStatus() != UserStatus.ACTIVE) {
            return Optional.empty();
        }

        Optional<Order> order = orderRepository.findFirstByUserIdAndStatusAndPaymentStatus(UUID.fromString(userId), OrderStatus.PROCESSING, PaymentStatus.UNPAID);
        if (order.isEmpty()) {
            return Optional.empty();
        }
        List<GameOrder> gamesOrder = gameOrderRepository.findAllByOrderId(order.get().getId());
        if (gamesOrder.isEmpty()) {
            return Optional.empty();
        }
        for (GameOrder gameOrder : gamesOrder) {
            if (gameOrder.getGame().getQuantity() < gameOrder.getQuantity()) {
                return Optional.empty();
            }
        }

        order.get().setPaymentStatus(PaymentStatus.WAITING);
        orderRepository.save(order.get());
        createNewOrder(user.get());

        // some pay function

        order.get().setPaymentStatus(PaymentStatus.PAID);
        order.get().setStatus(OrderStatus.APPROVED);
        orderRepository.save(order.get());

        Map<String, List<String>> gameKeys = getKeysByGamesOrder(gamesOrder);
        emailService.sendMessagePurchasedKeys(user.get().getEmail(), gameKeys);

        order.get().setStatus(OrderStatus.DELIVERED);
        Order checkoutOrder = orderRepository.save(order.get());

        return Optional.of(mapOrderToOrderDto(checkoutOrder));
    }

    private Map<String, List<String>> getKeysByGamesOrder(List<GameOrder> gamesOrder) {
        Map<String, List<String>> gameKeys = new HashMap<>();
        List<UUID> toDelete = new LinkedList<>();
        for (GameOrder gameOrder : gamesOrder) {
            Game game = gameOrder.getGame();
            List<Key> keys = keyRepository.findByGameId(game.getId());
            for (int i = 0; i < gameOrder.getQuantity(); i++) {
                Key key = keys.get(i);
                gameKeys.computeIfAbsent(game.getName(), k -> new LinkedList<>()).add(key.getValue().toString());
                toDelete.add(key.getId());
                game.setQuantity(game.getQuantity() - 1);
                gameRepository.save(game);
            }
        }
        keyRepository.deleteAllById(toDelete);
        return gameKeys;
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
        order.setStatus(OrderStatus.PROCESSING);
        order.setPaymentStatus(PaymentStatus.UNPAID);
        order.setTotalPrice(0.0);
        order.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        Order newOrder = orderRepository.save(order);
        return Optional.of(modelMapper.map(newOrder, OrderDto.class));
    }

    public Optional<OrderDto> createNewOrder(User user) {
        Order order = new Order();
        order.setUser(user);
        order.setStatus(OrderStatus.PROCESSING);
        order.setPaymentStatus(PaymentStatus.UNPAID);
        order.setTotalPrice(0.0);
        order.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        Order newOrder = orderRepository.save(order);
        return Optional.of(modelMapper.map(newOrder, OrderDto.class));
    }

    private OrderWithUserDto mapOrderToOrderWithUserDto(Order order) {
        OrderWithUserDto dto = modelMapper.map(order, OrderWithUserDto.class);
        dto.setGames(new LinkedList<>());
        List<GameOrder> gameOrders = gameOrderRepository.findAllByOrderId(order.getId());
        gameOrders.forEach(go -> dto.getGames().add(modelMapper.map(go, GameOrderDto.class)));
        return dto;
    }

    private OrderDto mapOrderToOrderDto(Order order) {
        OrderDto dto = modelMapper.map(order, OrderDto.class);
        dto.setGames(new LinkedList<>());
        List<GameOrder> gameOrders = gameOrderRepository.findAllByOrderId(order.getId());
        gameOrders.forEach(go -> dto.getGames().add(modelMapper.map(go, GameOrderDto.class)));
        return dto;
    }
}
