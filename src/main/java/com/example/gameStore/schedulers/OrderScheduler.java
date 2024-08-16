package com.example.gameStore.schedulers;

import com.example.gameStore.services.interfaces.OrderService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


@AllArgsConstructor
@Component
public class OrderScheduler {

    @Autowired
    private final OrderService orderService;

    @Scheduled(cron = "0 0/10 * * * ?")
    public void scheduleDeclineOrderIfDelay() throws InterruptedException {
        System.out.println("=====================================SCHEDULER=====================================");
        orderService.declineAllOrdersIfDelay();
    }
}
