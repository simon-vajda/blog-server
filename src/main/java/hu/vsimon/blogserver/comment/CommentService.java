package hu.vsimon.blogserver.comment;

import hu.vsimon.blogserver.post.Post;
import hu.vsimon.blogserver.post.PostService;
import hu.vsimon.blogserver.user.AppUser;
import hu.vsimon.blogserver.user.AppUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Principal;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostService postService;
    private final AppUserService appUserService;

    @Autowired
    public CommentService(CommentRepository commentRepository, PostService postService, AppUserService appUserService) {
        this.commentRepository = commentRepository;
        this.postService = postService;
        this.appUserService = appUserService;
    }

    public void insertComment(CommentDTO data, Principal principal) {
        Post post = postService.find(data.getPostId());
        AppUser user = appUserService.loadUserByUsername(principal.getName());

        Comment comment = new Comment();
        comment.setContent(data.getContent());
        comment.setAuthor(user);
        comment.setPost(post);
        commentRepository.save(comment);
    }
}
