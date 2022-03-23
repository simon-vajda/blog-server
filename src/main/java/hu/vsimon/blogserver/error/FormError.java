package hu.vsimon.blogserver.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class FormError {
    private String field;
    private String message;
}
