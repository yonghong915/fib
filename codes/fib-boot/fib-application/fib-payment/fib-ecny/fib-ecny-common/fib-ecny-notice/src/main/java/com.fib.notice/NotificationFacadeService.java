package src.main.java.com.fib.notice;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import src.main.java.com.fib.notice.internal.NotificationService;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationFacadeService {
    protected final NotificationService notificationService;

    public void send(Notification notification) {
        log.info("Notification Module (Facade): Received notification: {} - {}", notification.getType(), notification.getContent());
        // 调用内部服务处理通知
        notificationService.send(notification);
        log.info("Notification Module (Facade): Notification sent successfully!");
    }
}
