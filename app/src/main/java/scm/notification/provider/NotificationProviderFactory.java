package scm.notification.provider;

import scm.notification.enums.NotificationChannel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class NotificationProviderFactory {

    private final List<NotificationProvider> providers;

    public NotificationProvider getProvider(NotificationChannel channel) {
        return providers.stream()
                .filter(provider -> provider.supports(channel))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No provider found for channel: " + channel));
    }
}
