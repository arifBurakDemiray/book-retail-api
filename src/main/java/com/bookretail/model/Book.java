package com.bookretail.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "books")
@Setter
public class Book {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String author;

    private String isbn;

    private String publisher;

    private String year;

    private String description;

    @OneToOne(mappedBy = "book", fetch = FetchType.EAGER)
    private BookDetail bookDetail;

    public Book(String title, String author, String isbn, String publisher, String year, String description) {
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.publisher = publisher;
        this.year = year;
        this.description = description;
    }
}
