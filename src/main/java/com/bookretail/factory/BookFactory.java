package com.bookretail.factory;

import com.bookretail.dto.book.BookCreateDto;
import com.bookretail.dto.book.BookDto;
import com.bookretail.dto.book.BookUpdateDto;
import com.bookretail.model.Book;
import com.bookretail.model.BookDetail;
import com.bookretail.util.StringUtil;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor
public class BookFactory {
    public BookDto createBookDto(Book book) {
        return new BookDto(
                book.getId(),
                book.getTitle(),
                book.getAuthor(),
                book.getIsbn(),
                book.getPublisher(),
                book.getYear(),
                book.getDescription(),
                book.getBookDetail().getPrice(),
                book.getBookDetail().getStock()

        );
    }


    public BookDetail createBookDetail(BookCreateDto body, Book book) {
        return new BookDetail(
                body.getPrice(),
                body.getStock(),
                book
        );
    }

    public Book createBook(BookCreateDto body) {

        return new Book(
                body.getTitle(),
                body.getAuthor(),
                body.getIsbn(),
                body.getPublisher(),
                body.getYear(),
                body.getDescription()
        );
    }

    public void updateBook(BookUpdateDto body, Book book) {
        if (StringUtil.isValid(body.getTitle())) {
            book.setTitle(body.getTitle());
        }
        book.setTitle(body.getTitle());
        book.setAuthor(body.getAuthor());
        book.setIsbn(body.getIsbn());
        book.setPublisher(body.getPublisher());
        book.setYear(body.getYear());
        book.setDescription(body.getDescription());

        if (body.getPrice() != null && body.getPrice() > 0) {
            book.getBookDetail().setPrice(body.getPrice());
        }
        if (body.getStock() != null && body.getStock() > 0) {
            book.getBookDetail().setStock(body.getStock());
        }
    }
}
