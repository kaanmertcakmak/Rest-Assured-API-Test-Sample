package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

/**
 * @since 7/1/2021
 */
@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

//    @Bean
//    CommandLineRunner createBook(BookService bookService) {
//        return args -> {
//            System.out.println("Book is creating");
//            Book book = new Book();
//            book.setAuthor("Kaan");
//            book.setId(1L);
//            book.setTitle("Title");
//            bookService.save(book);
//        };
//    }

}
