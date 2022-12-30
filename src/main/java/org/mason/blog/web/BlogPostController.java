package org.mason.blog.web;

import org.jetbrains.annotations.NotNull;
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
    Collection<BlogPost> getPosts() {
        return postRepository.findAll();
    }

    @GetMapping("/post/{id}")
    ResponseEntity<?> getPost(@PathVariable Long id) {
        Optional<BlogPost> post = postRepository.findById(id);
        return post.map(response -> ResponseEntity.ok().body(response))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @CrossOrigin(origins = "http://localhost:5173")
    @PostMapping("/post")
    ResponseEntity<BlogPost> createPost(@Valid @RequestBody BlogPost post) throws URISyntaxException {
        log.info("Request to create post: {}", post);
        BlogPost result = postRepository.save(post);
        return ResponseEntity.created(new URI("/api/post/" + result.getId()))
                .body(result);
    }

    @PutMapping("/post/{id}")
    ResponseEntity<BlogPost> updatePost(@PathVariable(value = "id") Long id,
                                        @Valid @RequestBody @NotNull BlogPost incomingPostDetails)
                                        throws Exception {
        BlogPost originalPost = postRepository.findById(id)
                .orElseThrow(() -> new Exception("User not found on :: "+ id));

        incomingPostDetails.setId(originalPost.getId());

        if (incomingPostDetails.getTitle() == null) {
            incomingPostDetails.setTitle(originalPost.getTitle());
        }
        if (incomingPostDetails.getAuthor() == null) {
            incomingPostDetails.setAuthor(originalPost.getAuthor());
        }
        if (incomingPostDetails.getBody() == null) {
            incomingPostDetails.setBody(originalPost.getBody());
        }


        originalPost.setTitle(incomingPostDetails.getTitle());
        originalPost.setAuthor(incomingPostDetails.getAuthor());
        originalPost.setBody(incomingPostDetails.getBody());

        log.info("Request to update post: {}", originalPost);
        BlogPost result = postRepository.save(originalPost);
        return ResponseEntity.ok().body(result);
    }

    @DeleteMapping("/post/{id}")
    public ResponseEntity<?> deletePost(@PathVariable Long id) {
        log.info("Request to delete post: {}", id);
        postRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }
}