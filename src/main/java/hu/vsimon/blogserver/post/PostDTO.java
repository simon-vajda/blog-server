package hu.vsimon.blogserver.post;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
public class PostDTO {

    @NotBlank
    private String title;

    @NotBlank
    private String content;
}
