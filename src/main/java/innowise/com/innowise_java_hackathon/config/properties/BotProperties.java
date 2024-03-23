package innowise.com.innowise_java_hackathon.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "bot")
@Getter
@Setter
@Component
public class BotProperties {
    private String name;
    private String token;
}
