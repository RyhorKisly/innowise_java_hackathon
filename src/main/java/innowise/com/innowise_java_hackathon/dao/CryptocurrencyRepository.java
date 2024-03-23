package innowise.com.innowise_java_hackathon.dao;

import innowise.com.innowise_java_hackathon.core.entity.Cryptocurrency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CryptocurrencyRepository extends JpaRepository<Cryptocurrency, UUID> {
    Optional<Cryptocurrency> findBySymbol(String symbol);
}
