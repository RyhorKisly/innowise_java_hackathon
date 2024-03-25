package innowise.com.innowise_java_hackathon.listener;

import innowise.com.innowise_java_hackathon.service.CryptocurrencyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@Profile("!test")
@RequiredArgsConstructor
public class CryptocurrencyListener {
    private final CryptocurrencyService cryptocurrencyService;

    @EventListener(ApplicationReadyEvent.class)
    public void onApplicationReady() {
        cryptocurrencyService.migrateCryptocurrencies();
        System.out.println("migrated");
    }

}
