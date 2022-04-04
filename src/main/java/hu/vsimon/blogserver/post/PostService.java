package hu.vsimon.blogserver.post;

import hu.vsimon.blogserver.error.ResourceNotFoundException;
import hu.vsimon.blogserver.user.AppUser;
import hu.vsimon.blogserver.user.AppUserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.sql.Timestamp;

@Service
public class PostService {
    private final int PAGE_SIZE = 20;
    private final PostRepository postRepository;
    private final AppUserService appUserService;
    private final ModelMapper modelMapper;

    @Autowired
    public PostService(PostRepository postRepository, AppUserService appUserService, ModelMapper modelMapper) {
        this.postRepository = postRepository;
        this.appUserService = appUserService;
        this.modelMapper = modelMapper;
    }

    public Post find(long id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with id " + id));
    }

    public Page<Post> findAll(int pageNumber) {
        int page = pageNumber <= 0 ? 0 : pageNumber - 1;
        Pageable pageable = PageRequest.of(page, PAGE_SIZE, Sort.by("createdOn"));
        return postRepository.findAllByOrderByCreatedOnDesc(pageable);
    }

    public void addPost(PostDTO postData, Principal principal) {
        Post post = modelMapper.map(postData, Post.class);
        AppUser user = appUserService.loadUserByUsername(principal.getName());
        post.setUser(user);
        postRepository.save(post);
    }

    public boolean updatePost(long id, PostDTO postData, Principal principal) {
        Post post = find(id);
        if(!post.getUser().getEmail().equals(principal.getName())) {
            return false;
        }

        post.setTitle(postData.getTitle());
        post.setContent(postData.getContent());
        postRepository.save(post);
        return true;
    }

    public boolean deletePost(long id, Principal principal) {
        Post post = find(id);
        if(!post.getUser().getEmail().equals(principal.getName())) {
            return false;
        }

        postRepository.delete(post);
        return true;
    }
}