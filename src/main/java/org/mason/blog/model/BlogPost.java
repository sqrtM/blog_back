package org.mason.blog.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

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
    @GeneratedValue
    private Long id;
    @NonNull
    private String title;
    private String author;
    private Instant date;
    private Blob body;


    /*
    @ManyToOne(cascade = CascadeType.PERSIST)
    private User user;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<Event> events;
     */
}
