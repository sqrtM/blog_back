package org.mason.blog;

import org.mason.blog.model.Event;
import org.mason.blog.model.BlogPost;
import org.mason.blog.model.BlogPostRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Collections;
import java.util.stream.Stream;

@Component
class Initializer implements CommandLineRunner {

    private final BlogPostRepository repository;

    public Initializer(BlogPostRepository repository) {
        this.repository = repository;
    }

    @Override
    public void run(String... strings) {
        Stream.of("philosophy", "being gay", "tiddies",
                "nutting").forEach(title ->
                repository.save(new BlogPost(title))
        );

        BlogPost example = repository.findByTitle("philosophy");
        example.setAuthor("me, bitch");

        /*

        Event e = Event.builder().title("Micro Frontends for Java Developers")
                .description("JHipster now has microfrontend support!")
                .date(Instant.parse("2022-09-13T17:00:00.000Z"))
                .build();
        example.setEvents(Collections.singleton(e));
        repository.save(example);
         */

        repository.findAll().forEach(System.out::println);
    }

}
