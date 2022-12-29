package org.mason.blog.model;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Blob;
import java.time.Instant;

// TODO: 1. create a Comments class under the model package.

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    @NonNull
    private String author;
    private Instant date;
    private Blob body;
}
