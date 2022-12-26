package com.bookretail.service;

import com.bookretail.dto.PageFilter;
import com.bookretail.dto.Response;
import com.bookretail.dto.book.BookCreateDto;
import com.bookretail.dto.book.BookDto;
import com.bookretail.dto.book.BookUpdateDto;
import com.bookretail.enums.EErrorCode;
import com.bookretail.factory.BookFactory;
import com.bookretail.repository.BookDetailRepository;
import com.bookretail.repository.BookRepository;
import lombok.AllArgsConstructor;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final MessageSourceAccessor messageSource;
    private final BookDetailRepository bookDetailRepository;
    private final BookFactory bookFactory;

    @Transactional(readOnly = true)
    public Response<Page<BookDto>> getAllBooks(PageFilter pageFilter) {
        return Response.ok(bookRepository.findAll(pageFilter.asPageable()).map(bookFactory::createBookDto));
    }

    @Transactional
    public Response<BookDto> createABook(BookCreateDto body) {
        var book = bookRepository.save(bookFactory.createBook(body));
        var bookDetail = bookDetailRepository.save(bookFactory.createBookDetail(body, book));
        book.setBookDetail(bookDetail);

        return Response.ok(bookFactory.createBookDto(book));
    }

    @Transactional
    public Response<BookDto> updateABook(Long id, BookUpdateDto body) {

        var maybeBook = bookRepository.findById(id);

        if (maybeBook.isEmpty()) {
            return Response.notOk(messageSource.getMessage("validation.generic.entity.not_found"), EErrorCode.BAD_REQUEST);
        }

        var book = maybeBook.get();
        bookFactory.updateBook(body, book);

        bookDetailRepository.save(book.getBookDetail());
        book = bookRepository.save(book);

        return Response.ok(bookFactory.createBookDto(book));
    }
}
