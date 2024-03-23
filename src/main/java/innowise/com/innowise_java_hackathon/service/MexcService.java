package innowise.com.innowise_java_hackathon.service;

import innowise.com.innowise_java_hackathon.core.dto.CryptocurrencyDto;

import java.util.List;

/**
 * Interface for interacting with the Mexc API.
 * Provides methods to retrieve data related to cryptocurrencies.
 */
public interface MexcService {

    /**
     * Retrieves data for all cryptocurrencies from the Mexc API.
     *
     * @return A list of CryptocurrencyDto objects containing information about all cryptocurrencies.
     */
    List<CryptocurrencyDto> getAll();
}
