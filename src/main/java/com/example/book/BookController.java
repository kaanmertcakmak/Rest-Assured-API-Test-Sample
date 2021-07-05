package com.example.book;

import java.util.List;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;

/**
 * @since 7/1/2021
 */
@RestController
@RequestMapping("/books")
@AllArgsConstructor
public class BookController {

    BookService bookService;

    @GetMapping("")
    List<Book> getAllBooks() {
        return bookService.getAllBooks();
    }

    @GetMapping("/{id}")
    Book getBookById(@PathVariable Long id) {
        return bookService.getBookById(id);
    }

    @PutMapping
    @Valid
    Book putBook(@Valid @RequestBody Book book) {
        bookService.save(book);
        return book;
    }
}
