package com.kmadrugstore.utils;

import com.kmadrugstore.service.IOrderService;
import com.kmadrugstore.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@PropertySource("classpath:cron.properties")
@RequiredArgsConstructor
public class ScheduledTasks {

    private final IOrderService orderService;
    private final IUserService userService;

    @Scheduled(cron = "${cancel_expired_orders_cron}")
    public void cancelExpiredOrders() {
        orderService.cancelExpiredOrders();
    }

    @Scheduled(cron = "${cleanup_expired_password_reset_tokens_cron}")
    public void cleanupExpiredPasswordResetTokens() {
        userService.cleanupExpiredPasswordResetTokens();
    }

}
