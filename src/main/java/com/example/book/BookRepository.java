package com.example.book;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @since 7/1/2021
 */
public interface BookRepository extends JpaRepository<Book, Long>, JpaSpecificationExecutor<Book> {

    List<Book> findAll();

    Book findByIdEquals(Long id);

    Book findByTitleAndAndAuthor(String title, String author);
}
