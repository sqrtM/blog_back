package org.mason.blog.model;

import lombok.*;

import jakarta.persistence.*;

import java.sql.Blob;
import java.time.Instant;
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
    private Instant date;
    private Blob body;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<Comment> comments;
}
