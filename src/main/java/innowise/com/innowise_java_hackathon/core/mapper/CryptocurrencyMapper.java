package innowise.com.innowise_java_hackathon.core.mapper;

import innowise.com.innowise_java_hackathon.core.dto.CryptocurrencyDto;
import innowise.com.innowise_java_hackathon.core.entity.Cryptocurrency;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * Mapper interface for converting between CryptocurrencyDto and Cryptocurrency entities.
 */
@Mapper(componentModel = "spring")
public interface CryptocurrencyMapper {

    /**
     * Maps a CryptocurrencyDto to a Cryptocurrency entity.
     *
     * @param cryptocurrencyDto The CryptocurrencyDto to be mapped.
     * @return The corresponding Cryptocurrency entity.
     */
    Cryptocurrency toEntity(CryptocurrencyDto cryptocurrencyDto);

    /**
     * Maps a list of CryptocurrencyDto objects to a list of Cryptocurrency entities.
     *
     * @param cryptocurrencyDtos The list of CryptocurrencyDto objects to be mapped.
     * @return The list of corresponding Cryptocurrency entities.
     */
    List<Cryptocurrency> toEntities(List<CryptocurrencyDto> cryptocurrencyDtos);
    CryptocurrencyDto toDto(Cryptocurrency cryptocurrency);
    List<CryptocurrencyDto> toDtos(List<Cryptocurrency> cryptocurrencies);
}
