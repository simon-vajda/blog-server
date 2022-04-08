package hu.vsimon.blogserver.post;

import com.fasterxml.jackson.annotation.*;
import hu.vsimon.blogserver.comment.Comment;
import hu.vsimon.blogserver.user.AppUser;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Collection;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Post {

    @Id
    @SequenceGenerator(name = "post_sequence", sequenceName = "post_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "post_sequence")
    private Long id;

    @Column(name = "title", nullable = false, columnDefinition = "TEXT")
    private String title;

    @Column(name = "content", nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(name = "created_on", updatable = false)
    @CreationTimestamp
    private Timestamp createdOn;

    @Column(name = "updated_on")
    @UpdateTimestamp
    private Timestamp updatedOn;

    @JsonProperty("author")
    @ManyToOne
    @JoinColumn(name = "app_user_id", nullable = false, updatable = false)
    private AppUser user;

    @JsonIgnore
    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE)
    private Collection<Comment> comments;
}
