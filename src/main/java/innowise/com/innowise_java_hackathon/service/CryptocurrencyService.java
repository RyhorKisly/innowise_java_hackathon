package innowise.com.innowise_java_hackathon.service;

import innowise.com.innowise_java_hackathon.core.dto.CryptocurrencyDto;

import java.util.List;

public interface CryptocurrencyService {
    CryptocurrencyDto getBySymbol(CryptocurrencyDto cryptocurrencyDto);
    List<CryptocurrencyDto> getAll();
    void migrateCryptocurrencies();
    void deleteAll();
    CryptocurrencyDto updatePrice(CryptocurrencyDto cryptocurrencyDto);
}
