package innowise.com.innowise_java_hackathon.service.impl;

import innowise.com.innowise_java_hackathon.core.dto.CryptocurrencyDto;
import innowise.com.innowise_java_hackathon.core.entity.Cryptocurrency;
import innowise.com.innowise_java_hackathon.core.exceptions.FindEntityException;
import innowise.com.innowise_java_hackathon.core.mapper.CryptocurrencyMapper;
import innowise.com.innowise_java_hackathon.dao.CryptocurrencyRepository;
import innowise.com.innowise_java_hackathon.service.CryptocurrencyService;
import innowise.com.innowise_java_hackathon.service.MexcService;
import innowise.com.innowise_java_hackathon.service.feign.MexcServiceClient;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CryptocurrencyServiceImpl implements CryptocurrencyService {
    private final MexcService mexcService;
    private final CryptocurrencyRepository cryptocurrencyRepository;
    private final CryptocurrencyMapper cryptocurrencyMapper;

    @Override
    public CryptocurrencyDto getBySymbol(CryptocurrencyDto cryptocurrencyDto) {
        Cryptocurrency cryptocurrency = cryptocurrencyRepository.findBySymbol(cryptocurrencyDto.symbol()).orElseThrow(
                () -> new FindEntityException("Cryptocurrency not found!")
        );
        return cryptocurrencyMapper.toDto(cryptocurrency);
    }

    @Override
    @Transactional
    public List<CryptocurrencyDto> getAll() {
        List<Cryptocurrency> cryptocurrencies = cryptocurrencyRepository.findAll();
        return cryptocurrencyMapper.toDtos(cryptocurrencies);
    }

    @Override
    @Transactional
    public void migrateCryptocurrencies() {
        List<CryptocurrencyDto> cryptocurrencyDtos = mexcService.getAll();
        List<Cryptocurrency> cryptocurrencies = cryptocurrencyMapper.toEntities(cryptocurrencyDtos);
        cryptocurrencyRepository.saveAll(cryptocurrencies);
    }

    @Override
    @Transactional
    public void deleteAll() {
        cryptocurrencyRepository.deleteAll();
    }

    @Override
    @Transactional
    public CryptocurrencyDto updatePrice(CryptocurrencyDto cryptocurrencyDto) {
        Cryptocurrency cryptocurrency = cryptocurrencyRepository.findBySymbol(cryptocurrencyDto.symbol()).orElseThrow(
                () -> new FindEntityException("cryptocurrency not found")
        );
        cryptocurrency.setPrice(cryptocurrencyDto.price());
        cryptocurrency = cryptocurrencyRepository.save(cryptocurrency);
        return cryptocurrencyMapper.toDto(cryptocurrency);
    }

    @PreDestroy
    public void destroy() {
        cryptocurrencyRepository.deleteAll();
    }
}
