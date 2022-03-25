package hu.vsimon.blogserver.post;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class PostPageResponse {
    int pageNumber;
    int totalPages;
    long totalPosts;
    List<Post> posts;
}
