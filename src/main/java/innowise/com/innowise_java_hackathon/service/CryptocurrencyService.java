package innowise.com.innowise_java_hackathon.service;

import innowise.com.innowise_java_hackathon.core.dto.CryptocurrencyDto;

import java.util.List;

public interface CryptocurrencyService {
    List<CryptocurrencyDto> getAll();
    CryptocurrencyDto saveAll();
    void migrateCryptocurrencies();
    void deleteAll();
    CryptocurrencyDto update(CryptocurrencyDto cryptocurrencyDto);
}
