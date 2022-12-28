package org.mason.blog.web;

import org.mason.blog.model.BlogPost;
import org.mason.blog.model.BlogPostRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api")
class BlogPostController {

    private final Logger log = LoggerFactory.getLogger(BlogPostController.class);
    private final BlogPostRepository postRepository;

    public BlogPostController(BlogPostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @GetMapping("/posts")
    Collection<BlogPost> posts() {
        return postRepository.findAll();
    }

    @GetMapping("/post/{id}")
    ResponseEntity<?> getpost(@PathVariable Long id) {
        Optional<BlogPost> post = postRepository.findById(id);
        return post.map(response -> ResponseEntity.ok().body(response))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @CrossOrigin(origins = "http://localhost:5173")
    @PostMapping("/post")
    ResponseEntity<BlogPost> createpost(@Valid @RequestBody BlogPost post) throws URISyntaxException {
        log.info("Request to create post: {}", post);
        BlogPost result = postRepository.save(post);
        return ResponseEntity.created(new URI("/api/post/" + result.getId()))
                .body(result);
    }

    @PutMapping("/post/{id}")
    ResponseEntity<BlogPost> updatepost(@Valid @RequestBody BlogPost post) {
        log.info("Request to update post: {}", post);
        BlogPost result = postRepository.save(post);
        return ResponseEntity.ok().body(result);
    }

    @DeleteMapping("/post/{id}")
    public ResponseEntity<?> deletepost(@PathVariable Long id) {
        log.info("Request to delete post: {}", id);
        postRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }
}