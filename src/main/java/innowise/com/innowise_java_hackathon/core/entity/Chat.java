package innowise.com.innowise_java_hackathon.core.entity;

import innowise.com.innowise_java_hackathon.core.enums.UserStates;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(schema = "app", name = "chat")
public class Chat {
    @Id
    @GeneratedValue
    @UuidGenerator
    private UUID uuid;

    @NotNull
    @Column(name = "chat_id")
    private Long chatId;

    @Column(name = "threshold_percentage")
    private Long thresholdPercentage;

    @NotNull
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private UserStates status;
}
