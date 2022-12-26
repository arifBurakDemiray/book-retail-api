package com.bookretail.factory;

import com.bookretail.dto.book.BookCreateDto;
import com.bookretail.dto.book.BookDto;

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

    public static BookCreateDto createBookCreateDtoBlankNull() {
        return new BookCreateDto(
                "",
                "Test Author",
                "Test Publisher",
                "Test ISBN",
                "2022",
                "Test Description",
                null,
                null
        );
    }

    public static BookCreateDto createBookCreateDtoNotPositive() {
        return new BookCreateDto(
                "Test Book",
                "Test Author",
                "Test Publisher",
                "Test ISBN",
                "2022",
                "Test Description",
                -10.0,
                -10L
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
