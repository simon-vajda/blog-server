package hu.vsimon.blogserver.comment;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PositiveOrZero;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class CommentDTO {

    @NotBlank
    private String content;

    @PositiveOrZero
    private long postId;
}
