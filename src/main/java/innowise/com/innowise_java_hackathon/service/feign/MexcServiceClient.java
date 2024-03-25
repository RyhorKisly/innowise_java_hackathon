package innowise.com.innowise_java_hackathon.service.feign;

import innowise.com.innowise_java_hackathon.core.dto.CryptocurrencyDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(name = "tokenClient", url = "${mexc.api}")
public interface MexcServiceClient {

    @GetMapping
    ResponseEntity<List<CryptocurrencyDto>> getAll();
}
