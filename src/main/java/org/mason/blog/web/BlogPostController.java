package org.mason.blog.web;

import jakarta.validation.Valid;
import org.jetbrains.annotations.NotNull;
import org.mason.blog.model.BlogPost;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;

// TODO: note that while the API is now CRUD functional, it still breaks whenever you delete a post because it's always searching for "next". fix this.

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api")
class BlogPostController {

    ApplicationContext applicationContext = new ClassPathXmlApplicationContext("context.xml");
    DataSource dataSource = (DataSource)applicationContext.getBean("dataSource");
    JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);


    String url = "jdbc:postgresql://surus.db.elephantsql.com:5432/wsnldhmf";
    String username = "wsnldhmf";
    String password = "zFlUUIc9YQ-06HCJgblXBQq35lb4GlqB";

    Connection conn = DriverManager.getConnection(url, username, password);

    BlogPostController() throws SQLException {
    }


    @GetMapping("/posts")
    ResponseEntity<ArrayList<BlogPost>> getPosts() throws SQLException {
        ArrayList<BlogPost> postList = new ArrayList<>();
        String query = "SELECT * FROM blog_post";
        Statement statement = conn.createStatement();
        ResultSet resultSet = statement.executeQuery(query);
        while (resultSet.next()) {
            BlogPost nextPost = new BlogPost();

            nextPost.setId(resultSet.getLong("id"));
            nextPost.setTitle(resultSet.getString("title"));
            nextPost.setAuthor(resultSet.getString("author"));
            nextPost.setDate(resultSet.getString("date"));
            nextPost.setBody(resultSet.getString("body"));
            postList.add(nextPost);
        }

        return ResponseEntity.ok(postList);


    }

    @GetMapping("/post/{id}")
    ResponseEntity<BlogPost> getPost(@PathVariable int id) throws SQLException {
        BlogPost post = new BlogPost();
        PreparedStatement query = conn.prepareStatement("SELECT * FROM blog_post WHERE id = ?");
        query.setInt(1, id);
        ResultSet indexedRow = query.executeQuery();

        while (indexedRow.next()) {
            post.setId(indexedRow.getLong("id"));
            post.setTitle(indexedRow.getString("title"));
            post.setAuthor(indexedRow.getString("author"));
            post.setDate(indexedRow.getString("date"));
            post.setBody(indexedRow.getString("body"));
        }

        return ResponseEntity.ok(post);
    }

    @CrossOrigin(origins = "http://localhost:5173")
    @PostMapping("/post")
    ResponseEntity<BlogPost> createPost(@Valid @RequestBody BlogPost incomingPostDetails) throws SQLException {

        PreparedStatement query = conn.prepareStatement(
                "INSERT INTO blog_post (id, title, author, body) VALUES (?, ?, ?, ?)"
        );
        PreparedStatement getHighestID = conn.prepareStatement("SELECT id FROM blog_post WHERE id=(SELECT max(id) FROM blog_post)");
        ResultSet highestIDResultSet = getHighestID.executeQuery();
        while (highestIDResultSet.next()) {
            query.setLong(1, highestIDResultSet.getInt("id") + 1);
        }
        query.setString(2, incomingPostDetails.getTitle());
        query.setString(3, incomingPostDetails.getAuthor());
        query.setString(4, incomingPostDetails.getBody());
        query.executeUpdate();

        BlogPost returnedPost = new BlogPost();
        PreparedStatement returnQuery = conn.prepareStatement("SELECT * FROM blog_post WHERE id=(SELECT max(id) FROM blog_post)");
        ResultSet indexedRow = returnQuery.executeQuery();

        while (indexedRow.next()) {
            returnedPost.setId(indexedRow.getLong("id"));
            returnedPost.setTitle(indexedRow.getString("title"));
            returnedPost.setAuthor(indexedRow.getString("author"));
            returnedPost.setDate(indexedRow.getString("date"));
            returnedPost.setBody(indexedRow.getString("body"));
        }

        return ResponseEntity.ok(returnedPost);
    }

    @PutMapping("/post/{id}")
    ResponseEntity<BlogPost> updatePost(@PathVariable(value = "id") int id,
                                        @Valid @RequestBody @NotNull BlogPost incomingPostDetails)
                                        throws Exception {

        BlogPost originalPost = new BlogPost();
        PreparedStatement returnQuery = conn.prepareStatement("SELECT * FROM blog_post WHERE id = ?");
        returnQuery.setInt(1, id);
        ResultSet indexedRow = returnQuery.executeQuery();

        while (indexedRow.next()) {
            originalPost.setId(indexedRow.getLong("id"));
            originalPost.setTitle(indexedRow.getString("title"));
            originalPost.setAuthor(indexedRow.getString("author"));
            originalPost.setDate(indexedRow.getString("date"));
            originalPost.setBody(indexedRow.getString("body"));
        }

        if (incomingPostDetails.getTitle() == null) {
            incomingPostDetails.setTitle(originalPost.getTitle());
        }
        if (incomingPostDetails.getAuthor() == null) {
            incomingPostDetails.setAuthor(originalPost.getAuthor());
        }
        if (incomingPostDetails.getBody() == null) {
            incomingPostDetails.setBody(originalPost.getBody());
        }

        PreparedStatement query = conn.prepareStatement(
                "UPDATE blog_post " +
                    "SET title = ?, author = ?, body = ? " +
                    "WHERE id = " + id
        );
        query.setString(1, incomingPostDetails.getTitle());
        query.setString(2, incomingPostDetails.getAuthor());
        query.setString(3, incomingPostDetails.getBody());
        query.executeUpdate();

        return ResponseEntity.ok(incomingPostDetails);
    }

    @DeleteMapping("/post/{id}")
    public ResponseEntity<?> deletePost(@PathVariable int id) throws SQLException {
        PreparedStatement returnQuery = conn.prepareStatement("DELETE FROM blog_post WHERE id = ?");
        returnQuery.setInt(1, id);
        returnQuery.executeUpdate();
        return ResponseEntity.ok().build();
    }
}
