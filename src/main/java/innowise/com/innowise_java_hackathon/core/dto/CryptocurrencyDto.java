package innowise.com.innowise_java_hackathon.core.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CryptocurrencyDto(
        String symbol,
        BigDecimal price,
        boolean isUpdated
) {
}
