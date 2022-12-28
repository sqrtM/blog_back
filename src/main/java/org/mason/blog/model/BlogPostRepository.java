package org.mason.blog.model;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BlogPostRepository extends JpaRepository<BlogPost, Long> {
    BlogPost findByTitle(String title);
}