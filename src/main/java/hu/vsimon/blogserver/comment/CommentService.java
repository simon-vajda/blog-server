package hu.vsimon.blogserver.comment;

import hu.vsimon.blogserver.error.ResourceNotFoundException;
import hu.vsimon.blogserver.post.Post;
import hu.vsimon.blogserver.post.PostService;
import hu.vsimon.blogserver.user.AppUser;
import hu.vsimon.blogserver.user.AppUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

    public Page<Comment> findAllByPost(long postId, int pageNumber) {
        int page = pageNumber <= 0 ? 0 : pageNumber - 1;
        int pageSize = 5;
        Post post = postService.find(postId);

        Pageable pageable = PageRequest.of(page, pageSize, Sort.by("updatedOn"));
        return commentRepository.findAllByPostOrderByUpdatedOnDesc(post, pageable);
    }

    public boolean deleteComment(long id, Principal principal) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Comment not found with id " + id));
        String loggedInEmail = principal.getName();

        if(!comment.getAuthor().getEmail().equals(loggedInEmail) && !appUserService.isAdmin(loggedInEmail)) {
            return false;
        }

        commentRepository.delete(comment);
        return true;
    }
}
