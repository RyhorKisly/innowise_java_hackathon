package innowise.com.innowise_java_hackathon.service.impl;

import innowise.com.innowise_java_hackathon.core.dto.CryptocurrencyDto;
import innowise.com.innowise_java_hackathon.service.MexcService;
import innowise.com.innowise_java_hackathon.service.feign.MexcServiceClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class MexcServiceImpl implements MexcService {
    private final MexcServiceClient mexcServiceClient;

    @Override
    public List<CryptocurrencyDto> getAll() {
        return mexcServiceClient.getAll().getBody();
    }
}
