package innowise.com.innowise_java_hackathon.service.impl;

import innowise.com.innowise_java_hackathon.core.dto.CryptocurrencyDto;
import innowise.com.innowise_java_hackathon.core.entity.Cryptocurrency;
import innowise.com.innowise_java_hackathon.core.exceptions.FindEntityException;
import innowise.com.innowise_java_hackathon.core.mapper.CryptocurrencyMapper;
import innowise.com.innowise_java_hackathon.dao.CryptocurrencyRepository;
import innowise.com.innowise_java_hackathon.service.CryptocurrencyService;
import innowise.com.innowise_java_hackathon.service.feign.CryptocurrencyServiceClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CryptocurrencyServiceImpl implements CryptocurrencyService {
    private final CryptocurrencyServiceClient cryptocurrencyServiceClient;
    private final CryptocurrencyRepository cryptocurrencyRepository;
    private final CryptocurrencyMapper cryptocurrencyMapper;
    @Override
    @Transactional
    public List<CryptocurrencyDto> getAll() {
        List<Cryptocurrency> cryptocurrencies = cryptocurrencyRepository.findAll();
        return cryptocurrencyMapper.toDtos(cryptocurrencies);
    }

    @Override
    public CryptocurrencyDto saveAll() {
        return null;
    }

    @Override
    @Transactional
    public void migrateCryptocurrencies() {
        List<CryptocurrencyDto> cryptocurrencyDtos = cryptocurrencyServiceClient.getAll().getBody();
        List<Cryptocurrency> cryptocurrencies = cryptocurrencyMapper.toEntities(cryptocurrencyDtos);
        List<Cryptocurrency> saved = cryptocurrencyRepository.saveAll(cryptocurrencies);
    }

    @Override
    @Transactional
    public void deleteAll() {
        cryptocurrencyRepository.deleteAll();
    }

    @Override
    @Transactional
    public CryptocurrencyDto update(CryptocurrencyDto cryptocurrencyDto) {
        Cryptocurrency cryptocurrency = cryptocurrencyRepository.findBySymbol(cryptocurrencyDto.symbol()).orElseThrow(
                () -> new FindEntityException("cryptocurrency not found")
        );
        LocalDateTime version = cryptocurrency.getDtUpdate();
        cryptocurrency.setPrice(cryptocurrencyDto.price());
        cryptocurrency = cryptocurrencyRepository.save(cryptocurrency);
        LocalDateTime updatedVersion = cryptocurrency.getDtUpdate();

        cryptocurrency.setUpdated(version.isEqual(updatedVersion));

        return cryptocurrencyMapper.toDto(cryptocurrency);
    }
}
