package com.example.gameStore.services;

import com.example.gameStore.dtos.OrderDtos.GameOrderDto;
import com.example.gameStore.dtos.OrderDtos.OrderDto;
import com.example.gameStore.dtos.OrderDtos.OrderWithUserDto;
import com.example.gameStore.dtos.OrderDtos.PayDto;
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
import com.example.gameStore.shared.exceptions.BadRequestException;
import com.example.gameStore.shared.exceptions.ResourceNotFoundException;
import com.example.gameStore.utilities.TypeConverter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.naming.AuthenticationException;
import java.sql.Timestamp;
import java.time.Instant;
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
        UUID orderId = TypeConverter.convertStringToUUID(id, "Invalid order id format: " + id);
        Optional<Order> order = orderRepository.findById(orderId);
        return order.map(this::mapOrderToOrderDto);
    }

    public Optional<OrderDto> findOrderById(String orderId, String userId) throws AuthenticationException {
        UUID orderUUID = TypeConverter.convertStringToUUID(orderId, "Invalid order id format: " + orderId);
        UUID userUUID = TypeConverter.convertStringToUUID(userId, "Invalid user id format: " + userId);
        Optional<Order> order = orderRepository.findById(orderUUID);
        if (order.isEmpty()) {
            throw new BadRequestException("Order not found by id: " + orderId);
        }
        if (!order.get().getUser().getId().equals(userUUID)) {
            throw new AuthenticationException("User not authorized to view this order");
        }
        return Optional.of(mapOrderToOrderDto(order.get()));
    }

    public List<OrderDto> findOrdersByUser(String userId) {
        List<Order> orders = orderRepository.findAllByUserId(TypeConverter.convertStringToUUID(userId));
        return orders.stream()
                .map(this::mapOrderToOrderDto)
                .toList();
    }

    public Optional<OrderDto> findCurrentOrderByUser(String userId) {
        Optional<Order> order = orderRepository.findFirstByUserIdAndStatusAndPaymentStatus(TypeConverter.convertStringToUUID(userId), OrderStatus.PROCESSING, PaymentStatus.UNPAID);
        return order.map(this::mapOrderToOrderDto);
    }

    public Optional<OrderDto> addGameToOrder(String gameId, String userId) {
        Optional<Order> order = orderRepository.findFirstByUserIdAndStatusAndPaymentStatus(TypeConverter.convertStringToUUID(userId), OrderStatus.PROCESSING, PaymentStatus.UNPAID);
        Optional<Game> game = gameRepository.findById(TypeConverter.convertStringToUUID(gameId));

        if (game.isEmpty()) throw new BadRequestException("Game not found by id: " + gameId);
        if (order.isEmpty()) throw new BadRequestException("order not found by userId: " + userId);

        if (!game.get().isActive()) throw new BadRequestException("Game found but it isn't ACTIVE, gameId: " + gameId);

        order.get().setTotalPrice(order.get().getTotalPrice() + game.get().getPrice());
        order.get().setUpdatedAt(Timestamp.from(Instant.now()));
        orderRepository.save(order.get());

        List<GameOrder> gamesOrder = gameOrderRepository.findAllByOrderId(order.get().getId());
        for (GameOrder gameOrder : gamesOrder) {
            if (gameOrder.getGame().getId().equals(game.get().getId())) {
                Integer q = gameOrder.getQuantity();
                q++;
                gameOrder.setQuantity(q);
                gameOrderRepository.save(gameOrder);
                return Optional.of(mapOrderToOrderDto(orderRepository.findById(order.get().getId()).get()));
            }
        }
        GameOrder gameOrder = new GameOrder();
        gameOrder.setId(new GameOrderId(game.get().getId(), order.get().getId()));
        gameOrder.setGame(game.get());
        gameOrder.setOrder(order.get());
        gameOrder.setQuantity(1);
        gameOrderRepository.save(gameOrder);
        return Optional.of(mapOrderToOrderDto(orderRepository.findById(order.get().getId()).get()));
    }

    public Optional<OrderDto> deleteGameFromOrder(String gameId, String userId) {
        Optional<Order> order = orderRepository.findFirstByUserIdAndStatusAndPaymentStatus(TypeConverter.convertStringToUUID(userId), OrderStatus.PROCESSING, PaymentStatus.UNPAID);
        if (order.isEmpty()) {
            throw new BadRequestException("order not found by userId: " + userId);
        }

        Optional<Game> game = gameRepository.findById(TypeConverter.convertStringToUUID(gameId));
        if (game.isEmpty()) {
            throw new BadRequestException("game not found by gameId: " + gameId);
        }

        Optional<GameOrder> gamesInOrder = gameOrderRepository.findFirstByOrderIdAndGameId(order.get().getId(), TypeConverter.convertStringToUUID(gameId));
        if (gamesInOrder.isEmpty()) {
            throw new BadRequestException("can't delete game that not in order");
        }

        order.get().setTotalPrice(order.get().getTotalPrice() - game.get().getPrice());

        if (gamesInOrder.get().getQuantity() > 1) {
            Integer q = gamesInOrder.get().getQuantity();
            q--;
            gamesInOrder.get().setQuantity(q);
            gameOrderRepository.save(gamesInOrder.get());
            return Optional.of(mapOrderToOrderDto(orderRepository.findById(order.get().getId()).get()));
        }

        gameOrderRepository.deleteByGameIdAndOrderId(TypeConverter.convertStringToUUID(gameId), order.get().getId());
        return Optional.of(mapOrderToOrderDto(orderRepository.findById(order.get().getId()).get()));
    }

    @Transactional
    public Optional<OrderDto> cleanCurrentOrder(String userId) {
        Optional<Order> order = orderRepository.findFirstByUserIdAndStatusAndPaymentStatus(TypeConverter.convertStringToUUID(userId), OrderStatus.PROCESSING, PaymentStatus.UNPAID);
        if (order.isEmpty()) {
            throw new BadRequestException("order not found by userId: " + userId);
        }
        gameOrderRepository.deleteByOrderId(order.get().getId());
        return Optional.of(mapOrderToOrderDto(order.get()));
    }

    public Optional<OrderDto> checkoutCurrentOrder(String userId) {
        Optional<User> user = userRepository.findById(TypeConverter.convertStringToUUID(userId));
        if (user.isEmpty()) {
            throw new BadRequestException("order not found by userId: " + userId);
        }
        if (user.get().getActiveStatus() != UserStatus.ACTIVE) {
            throw new BadRequestException("User must be ACTIVE to checkout order");
        }

        Optional<Order> order = orderRepository.findFirstByUserIdAndStatusAndPaymentStatus(TypeConverter.convertStringToUUID(userId), OrderStatus.PROCESSING, PaymentStatus.UNPAID);
        if (order.isEmpty()) {
            throw new BadRequestException("Current order not found by userId: " + userId);
        }
        List<GameOrder> gamesOrder = gameOrderRepository.findAllByOrderId(order.get().getId());
        if (gamesOrder.isEmpty()) {
            throw new BadRequestException("There is no game in Current Order of User with userId: " + userId);
        }
        for (GameOrder gameOrder : gamesOrder) {
            if (!gameOrder.getGame().isActive()) {
                throw new BadRequestException("Game in order is not ACTIVE: " + gameOrder.getGame().getName());
            }
            if (gameOrder.getGame().getQuantity() < gameOrder.getQuantity()) {
                throw new ResourceNotFoundException("Not enough quantity of game: " + gameOrder.getGame().getName());
            }
        }

        order.get().setPaymentStatus(PaymentStatus.WAITING);
        order.get().setUpdatedAt(Timestamp.from(Instant.now()));
        Order checkoutOrder = orderRepository.save(order.get());
        createNewOrder(user.get());

        return Optional.of(mapOrderToOrderDto(checkoutOrder));
    }

    public Optional<OrderDto> payForOrder(PayDto payDto, String userId) {
        Optional<User> user = userRepository.findById(TypeConverter.convertStringToUUID(userId));
        if (user.isEmpty()) {
            throw new BadRequestException("order not found by userId: " + userId);
        }
        if (user.get().getActiveStatus() != UserStatus.ACTIVE) {
            throw new BadRequestException("User must be ACTIVE to checkout order");
        }

        Optional<Order> order = orderRepository.findById(TypeConverter.convertStringToUUID(payDto.getOrderId()));
        if (order.isEmpty()) {
            throw new BadRequestException("Wrong orderId: " + payDto.getOrderId());
        }
        if (!order.get().getStatus().equals(OrderStatus.PROCESSING) || !order.get().getPaymentStatus().equals(PaymentStatus.WAITING)) {
            throw new BadRequestException("You can't pay for this order");
        }

        if (!payDto.getIsPaidSuccessfully()) {
            order.get().setPaymentStatus(PaymentStatus.UNPAID);
            order.get().setStatus(OrderStatus.DECLINED);
            order.get().setUpdatedAt(Timestamp.from(Instant.now()));
            Order unpaidOrder = orderRepository.save(order.get());
            return Optional.of(mapOrderToOrderDto(unpaidOrder));
        }

        List<GameOrder> gamesOrder = gameOrderRepository.findAllByOrderId(order.get().getId());
        if (gamesOrder.isEmpty()) {
            throw new BadRequestException("There is no game in Current Order of User with userId: " + userId);
        }
        for (GameOrder gameOrder : gamesOrder) {
            if (!gameOrder.getGame().isActive()) {
                throw new BadRequestException("Game in order is not ACTIVE: " + gameOrder.getGame().getName());
            }
            if (gameOrder.getGame().getQuantity() < gameOrder.getQuantity()) {
                throw new ResourceNotFoundException("Not enough quantity of game: " + gameOrder.getGame().getName());
            }
        }

        order.get().setPaymentStatus(PaymentStatus.PAID);
        order.get().setStatus(OrderStatus.APPROVED);
        order.get().setUpdatedAt(Timestamp.from(Instant.now()));
        orderRepository.save(order.get());

        Map<String, List<String>> gameKeys = getKeysByGamesOrder(gamesOrder);
        emailService.sendMessagePurchasedKeys(user.get().getEmail(), gameKeys);

        order.get().setStatus(OrderStatus.DELIVERED);
        order.get().setUpdatedAt(Timestamp.from(Instant.now()));
        Order paidOrder = orderRepository.save(order.get());

        return Optional.of(mapOrderToOrderDto(paidOrder));
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
            throw new BadRequestException("User not found");
        }
        Optional<User> user = userRepository.findById(userDto.get().getId());
        if (user.isEmpty()) {
            throw new BadRequestException("User not found by id: " + userDto.get().getId());
        }
        Order order = new Order();
        order.setUser(user.get());
        order.setStatus(OrderStatus.PROCESSING);
        order.setPaymentStatus(PaymentStatus.UNPAID);
        order.setTotalPrice(0.0);
        order.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        order.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
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

    public void declineAllOrdersIfDelay() {
        List<Order> orders = orderRepository.findAllByStatusAndPaymentStatus(OrderStatus.PROCESSING, PaymentStatus.WAITING);
        orders.forEach(order -> {
            System.out.println(order.getId());
            if (order.getUpdatedAt().before(Timestamp.from(Instant.now().minusSeconds(60 * 30)))) {
                order.setStatus(OrderStatus.DECLINED);
                order.setPaymentStatus(PaymentStatus.REJECTED);
                order.setUpdatedAt(Timestamp.from(Instant.now()));
                orderRepository.save(order);
            }
        });
    }
}
