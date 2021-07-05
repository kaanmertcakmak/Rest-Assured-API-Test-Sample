package com.example.book;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * @since 7/5/2021
 */
public class UniqueBookValidator implements ConstraintValidator<UniqueBook, Book> {

    @Autowired
    BookRepository bookRepository;

    @Override
    public boolean isValid(Book book, ConstraintValidatorContext context) {
        Book bookInDb = bookRepository.findByTitleAndAndAuthor(book.getTitle(), book.getAuthor());
        return bookInDb == null;
    }
}
