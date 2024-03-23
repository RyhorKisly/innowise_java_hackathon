package innowise.com.innowise_java_hackathon.core.exceptions;

import org.springframework.dao.DataAccessException;

public class FindEntityException extends DataAccessException {
    public FindEntityException(String message) {
        super(message);
    }

    public FindEntityException(String message, Throwable cause) {
        super(message, cause);
    }
}
