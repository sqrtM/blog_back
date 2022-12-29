package org.mason.blog;

import org.jetbrains.annotations.NotNull;
import org.mason.blog.model.BlogPost;
import org.mason.blog.model.BlogPostRepository;
import org.mason.blog.model.Comment;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.time.Instant;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

@Component
class Initializer implements CommandLineRunner {

    private final BlogPostRepository repository;

    public Initializer(BlogPostRepository repository) {
        this.repository = repository;
    }

    @Override
    public void run(String... strings) throws UnsupportedEncodingException, SQLException {
        Stream.of("philosophy", "running from the police", "being very cool",
                "driving very fast", "getting free things on an airplane").forEach(title ->
                repository.save(new BlogPost(title))
        );

        BlogPost example = repository.findByTitle("philosophy");
        example.setAuthor("wow it is me");
        example.setBody(
                """
                wow this is crazy
                i can start new lines
                and idk, maybe i can even
                do html editing and stuff like that
                """
        );

        repository.save(example);

        repository.findAll().forEach(System.out::println);
    }

}
