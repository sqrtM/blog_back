package org.mason.blog.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
@Entity
@Table(name = "blog_post")
public class BlogPost {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    @NonNull
    private String title;
    private String author;
    private String date;
    private String body;

    //@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    //private Set<Comment> comments;
}
