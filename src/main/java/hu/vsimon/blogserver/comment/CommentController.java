package hu.vsimon.blogserver.comment;

import hu.vsimon.blogserver.post.Post;
import hu.vsimon.blogserver.post.PostDTO;
import hu.vsimon.blogserver.post.PostPageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/api/v1/comment")
public class CommentController {

    private final CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping()
    public ResponseEntity<?> insertComment(@Valid @RequestBody CommentDTO comment, Principal principal) {
        commentService.insertComment(comment, principal);
        return ResponseEntity.ok("");
    }

    @GetMapping()
    public ResponseEntity<?> findAllCommentsByPost(@RequestParam(name = "post") long postId,
                                                   @RequestParam(required = false, name = "page", defaultValue = "0") int pageNumber) {
        Page<Comment> page = commentService.findAllByPost(postId, pageNumber);
        return ResponseEntity.ok(new CommentPageResponse(page.getNumber()+1, page.getTotalPages(), page.getTotalElements(), page.getContent()));
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<?> deleteComment(@PathVariable long commentId, Principal principal) {
        // Only the same user can delete the comment, who created it
        if(commentService.deleteComment(commentId, principal)) {
            return ResponseEntity.ok("");
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    @PutMapping("/{commentId}")
    public ResponseEntity<?> updatePost(@PathVariable long commentId, @Valid @RequestBody CommentDTO comment, Principal principal) {
        // Only the same user can update the post, who created it
        if(commentService.updateComment(commentId, comment, principal)) {
            return ResponseEntity.ok("");
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
}
