package hu.vsimon.blogserver.comment;

import hu.vsimon.blogserver.post.Post;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class CommentPageResponse {
    int pageNumber;
    int totalPages;
    long totalComments;
    List<Comment> comments;
}