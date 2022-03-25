package hu.vsimon.blogserver.error;

import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

@NoArgsConstructor
public class ResourceNotFoundException extends RuntimeException{

    public ResourceNotFoundException(String message) {
        super(message);
    }

    public ResponseEntity<Object> getResponse() {
        Map<String, String> body = new HashMap<>();
        body.put("message", getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
    }
}
