package hu.vsimon.blogserver.post;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/api/v1/post")
public class PostController {

    private final PostService postService;

    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping()
    public ResponseEntity<?> createPost(@Valid @RequestBody PostDTO postData, Principal user) {
        postService.addPost(postData, user);
        return ResponseEntity.ok("");
    }

    @GetMapping()
    public ResponseEntity<?> findAllPosts(@RequestParam(required = false, name = "page", defaultValue = "0") int pageNumber) {
        Page<Post> page = postService.findAll(pageNumber);
        return ResponseEntity.ok(new PostPageResponse(page.getNumber()+1, page.getTotalPages(), page.getTotalElements(), page.getContent()));
    }

    @GetMapping("/{postId}")
    public ResponseEntity<?> findPost(@PathVariable long postId) {
        return ResponseEntity.ok(postService.find(postId));
    }

    @PutMapping("/{postId}")
    public ResponseEntity<?> updatePost(@PathVariable long postId, @Valid @RequestBody PostDTO postData, Principal principal) {
        // Only the same user can update the post, who created it
        if(postService.updatePost(postId, postData, principal)) {
            return ResponseEntity.ok("");
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<?> deletePost(@PathVariable long postId, Principal principal) {
        // Only the same user can delete the post, who created it
        if(postService.deletePost(postId, principal)) {
            return ResponseEntity.ok("");
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
}
