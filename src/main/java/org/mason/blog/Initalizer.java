package org.mason.blog;

import org.mason.blog.model.BlogPost;
import org.mason.blog.model.BlogPostRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.sql.rowset.serial.SerialBlob;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.stream.Stream;

@Component
class Initializer implements CommandLineRunner {

    private final BlogPostRepository repository;

    public Initializer(BlogPostRepository repository) {
        this.repository = repository;
    }

    @Override
    public void run(String... strings) throws UnsupportedEncodingException, SQLException {
        Stream.of("philosophy", "being gay", "tiddies",
                "nutting").forEach(title ->
                repository.save(new BlogPost(title))
        );

        BlogPost example = repository.findByTitle("philosophy");
        example.setAuthor("me, bitch");
        repository.save(example);

        repository.findAll().forEach(System.out::println);
    }

}
