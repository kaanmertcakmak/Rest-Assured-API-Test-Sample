package com.example.book;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Null;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @since 7/1/2021
 */
@Entity
@Data
@NoArgsConstructor
@UniqueBook(message = "There is already a book with same title and author")
public class Book {

    private static final long serialVersionUID = 3203364416943033232L;

    @Id
    @Null(message = "id parameter is read only")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Field 'title' is mandatory")
    private String title;

    @NotBlank(message = "Field 'author' is mandatory")
    private String author;
}
