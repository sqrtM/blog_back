package org.mason.blog;

import org.jetbrains.annotations.NotNull;
import org.mason.blog.model.BlogPost;
import org.mason.blog.model.BlogPostRepository;
import org.mason.blog.model.Comment;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.time.Instant;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import javax.sql.DataSource;
import java.sql.*;

@Component
class Initializer implements CommandLineRunner {



    private final BlogPostRepository repository;

    public Initializer(BlogPostRepository repository) throws SQLException {
        this.repository = repository;
    }


    @Override
    public void run(String... strings) {
        {
            ApplicationContext applicationContext = new ClassPathXmlApplicationContext("context.xml");
            DataSource ds = (DataSource)applicationContext.getBean("dataSource");
            JdbcTemplate jt = new JdbcTemplate(ds);

            jt.execute("select title from blog_post where id = 2");
            System.out.println();
        }
    }

}
