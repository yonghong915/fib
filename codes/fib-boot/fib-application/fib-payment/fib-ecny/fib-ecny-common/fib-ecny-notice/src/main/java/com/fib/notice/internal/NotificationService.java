package src.main.java.com.fib.notice.internal;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import src.main.java.com.fib.notice.Notification;

@Service
@Slf4j
public class NotificationService {
    public void send(Notification notification) {
        String message = String.format("Notification for type '%s' with content: '%s'", notification.getType(), notification.getContent());
        log.info("Notification Module (Internal): Sending notification internally: {}", message);
    }
}
