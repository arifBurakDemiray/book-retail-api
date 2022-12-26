package com.bookretail.factory;

import com.bookretail.dto.book.BookCreateDto;
import com.bookretail.dto.book.BookDto;
import com.bookretail.dto.book.BookUpdateDto;
import com.bookretail.model.Book;
import com.bookretail.model.BookDetail;

public class BookTestFactory {

    private BookTestFactory() {
    }

    public static BookCreateDto createBookCreateDto() {
        return new BookCreateDto(
                "Test Book",
                "Test Author",
                "Test Publisher",
                "Test ISBN",
                "2022",
                "Test Description",
                10.0,
                10L
        );
    }

    public static Book createBook() {
        var book = new Book(
                "Test Book",
                "Test Author",
                "Test Publisher",
                "Test ISBN",
                "2022",
                "Test Description"
        );
        book.setId(1L);

        return book;
    }

    public static BookDetail createBookDetail(Book book) {
        return new BookDetail(10.0, 10L, book);
    }


    public static BookUpdateDto createBookUpdateDto() {
        return new BookUpdateDto(
                "Test Book",
                "Test Author",
                "Test Publisher",
                "Test ISBN",
                "2022",
                "Test Description",
                10.0,
                10L
        );
    }

    public static BookDto createBookDto() {
        return new BookDto(
                1L,
                "Test Book",
                "Test Author",
                "Test Publisher",
                "Test ISBN",
                "2022",
                "Test Description",
                10.0,
                10L
        );
    }

}
