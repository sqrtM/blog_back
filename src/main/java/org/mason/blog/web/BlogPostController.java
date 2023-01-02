package org.mason.blog.web;

import org.jetbrains.annotations.NotNull;
import org.mason.blog.model.BlogPost;
import org.mason.blog.model.BlogPostRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import javax.sql.DataSource;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api")
class BlogPostController {

    private final Logger log = LoggerFactory.getLogger(BlogPostController.class);
    private final BlogPostRepository postRepository;

    ApplicationContext applicationContext = new ClassPathXmlApplicationContext("context.xml");
    DataSource dataSource = (DataSource)applicationContext.getBean("dataSource");
    JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);


    String url = "jdbc:postgresql://surus.db.elephantsql.com:5432/wsnldhmf";
    String username = "wsnldhmf";
    String password = "zFlUUIc9YQ-06HCJgblXBQq35lb4GlqB";

    Connection conn = DriverManager.getConnection(url, username, password);

    public BlogPostController(BlogPostRepository postRepository) throws SQLException {
        this.postRepository = postRepository;
    }

    @GetMapping("/posts")
    ResponseEntity[] getPosts() throws SQLException {
        ArrayList<BlogPost> postList = new ArrayList<>();
        PreparedStatement preparedStatement = conn.prepareStatement("SELECT * FROM blog_post WHERE id = ?");
        int index = 1;
        preparedStatement.setInt(1, index);
        ResultSet rs = preparedStatement.executeQuery();
        while (rs.next()) {
            BlogPost nextPost = new BlogPost();

            nextPost.setId(rs.getLong("id"));
            nextPost.setTitle(rs.getString("title"));
            nextPost.setAuthor(rs.getString("author"));
            nextPost.setDate(rs.getString("date"));
            nextPost.setBody(rs.getString("body"));
            postList.add(nextPost);

            index++;
            preparedStatement.setInt(1, index);
            rs = preparedStatement.executeQuery();
        }
        return new ResponseEntity[]{ResponseEntity.ok(postList)};
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
                .orElseThrow(() -> new Exception("Post not found on :: "+ id));

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