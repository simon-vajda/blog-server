package hu.vsimon.blogserver.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import hu.vsimon.blogserver.comment.Comment;
import hu.vsimon.blogserver.post.Post;
import hu.vsimon.blogserver.role.Role;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "app_user")
public class AppUser implements UserDetails {

    @Id
    @SequenceGenerator(name = "user_sequence", sequenceName = "user_sequence", allocationSize = 1)
    @GeneratedValue
    private Long id;

    @Column(name = "email", nullable = false, columnDefinition = "TEXT", unique = true)
    private String email;

    @JsonIgnore
    @Column(name = "password", nullable = false, columnDefinition = "TEXT")
    private String password;

    @Column(name = "name", nullable = false, columnDefinition = "TEXT")
    private String name;

    @JsonIgnore
    @Column(name = "posts")
    @OneToMany(mappedBy = "user")
    private Collection<Post> posts;

    @JsonIgnore
    @Column(name = "roles")
    @ManyToMany(fetch = FetchType.EAGER)
    private Collection<Role> roles = new ArrayList<>();

    @OneToMany(mappedBy = "author")
    private Collection<Comment> comments;

    @JsonIgnore
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @JsonIgnore
    @Override
    public String getUsername() {
        return email;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isEnabled() {
        return true;
    }

    public AppUser(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
    }
}
