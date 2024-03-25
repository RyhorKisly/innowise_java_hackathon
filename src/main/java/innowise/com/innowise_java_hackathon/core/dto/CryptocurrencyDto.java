package innowise.com.innowise_java_hackathon.core.dto;

import java.math.BigDecimal;

public record CryptocurrencyDto(
        String symbol,
        BigDecimal price
) {
}
