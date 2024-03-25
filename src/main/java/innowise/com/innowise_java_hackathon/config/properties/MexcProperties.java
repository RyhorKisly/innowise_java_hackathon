package innowise.com.innowise_java_hackathon.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "mex")
@Getter
@Setter
@Component
public class MexcProperties {
    private String api;

}
