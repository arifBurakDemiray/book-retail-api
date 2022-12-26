package com.bookretail.service;

import com.bookretail.dto.PageFilter;
import com.bookretail.dto.Response;
import com.bookretail.dto.book.BookCreateDto;
import com.bookretail.dto.book.BookDto;
import com.bookretail.factory.BookFactory;
import com.bookretail.factory.BookTestFactory;
import com.bookretail.repository.BookDetailRepository;
import com.bookretail.repository.BookRepository;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.AuthenticationException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ContextConfiguration(classes = {BookService.class})
@ExtendWith(SpringExtension.class)
public class BookServiceTest {

    @Autowired
    private BookService bookService;

    @MockBean
    private BookFactory bookFactory;

    @MockBean
    private BookRepository bookRepository;

    @MockBean
    private MessageSourceAccessor messageSource;

    @MockBean
    private BookDetailRepository bookDetailRepository;

    @Nested
    class CreateABook_Method_Test_Cases {

        @Test
        void CreateABook_Success() throws AuthenticationException {
            //given
            var request = BookTestFactory.createBookCreateDto();
            var book = BookTestFactory.createBook();
            book.setBookDetail(BookTestFactory.createBookDetail(book));

            //when
            when(bookFactory.createBook(any(BookCreateDto.class))).thenReturn(book);
            when(bookRepository.save(any())).thenReturn(book);
            when(bookDetailRepository.save(any())).thenReturn(book.getBookDetail());
            when(bookFactory.createBookDetail(any(BookCreateDto.class), any())).thenReturn(book.getBookDetail());
            when(bookFactory.createBookDto(any())).thenReturn(BookTestFactory.createBookDto());

            var response = bookService.createABook(request);

            //then
            assertTrue(response.isOk());
            assertEquals(response.getData().getTitle(), request.getTitle());

            verify(bookFactory).createBook(any(BookCreateDto.class));
            verify(bookRepository).save(any());
            verify(bookDetailRepository).save(any());
            verify(bookFactory).createBookDetail(any(BookCreateDto.class), any());
            verify(bookFactory).createBookDto(any());
        }


    }

    @Nested
    class GetAllBooks_Method_Test_Cases {

        @Test
        void GetAllBooks_Success() throws AuthenticationException {
            //given

            //when
            when(bookRepository.findAll(any(Pageable.class)))
                    .thenReturn(new PageImpl<>(new ArrayList<>()));
            Response<Page<BookDto>> response = bookService.getAllBooks(new PageFilter());
            var data = response.getData();

            //then
            assertTrue(data instanceof PageImpl);
            assertTrue(response.isOk());
            verify(bookRepository).findAll(any(Pageable.class));
        }

        @Test
        void GetAllBooks_Fails_PageRequest() throws AuthenticationException {
            //given
            var pageFiler = new PageFilter(-1, 0, null, null);
            //when
            when(messageSource.getMessage(anyString())).thenReturn("error");
            //then
            assertThrows(IllegalArgumentException.class, () -> bookService.getAllBooks(pageFiler));
            verify(bookRepository, times(0)).findAll(any(Pageable.class));
        }

        @Test
        void GetAllBooks_Success_FailPageRequest() throws AuthenticationException {
            //given
            var pageFiler = new PageFilter(null, null, null, null);
            //when
            when(bookRepository.findAll(pageFiler.asPageable()))
                    .thenReturn(new PageImpl<>(new ArrayList<>()));
            Response<Page<BookDto>> response = bookService.getAllBooks(pageFiler);
            var data = response.getData();

            //then
            assertTrue(data instanceof PageImpl);
            assertTrue(response.isOk());
            verify(bookRepository).findAll(any(Pageable.class));
        }


    }

    @Nested
    class UpdateABook_Method_Test_Cases {

        @Test
        void UpdateABook_Success() throws AuthenticationException {
            //given
            var request = BookTestFactory.createBookUpdateDto();
            var book = BookTestFactory.createBook();
            book.setBookDetail(BookTestFactory.createBookDetail(book));

            //when
            when(messageSource.getMessage(anyString())).thenReturn("success");
            when(bookRepository.findById(any())).thenReturn(java.util.Optional.of(book));
            when(bookRepository.save(any())).thenReturn(book);
            when(bookDetailRepository.save(any())).thenReturn(book.getBookDetail());
            when(bookFactory.createBookDto(any())).thenReturn(BookTestFactory.createBookDto());

            var response = bookService.updateABook(book.getId(), request);

            //then
            assertTrue(response.isOk());
            assertEquals(response.getData().getTitle(), request.getTitle());

            verify(bookRepository).findById(any());
            verify(bookRepository).save(any());
            verify(bookDetailRepository).save(any());
            verify(bookFactory).createBookDto(any());
        }

        @Test
        void UpdateABook_Fails() throws AuthenticationException {
            //given
            var request = BookTestFactory.createBookUpdateDto();
            var book = BookTestFactory.createBook();

            //when
            when(messageSource.getMessage(anyString())).thenReturn("validation.generic.entity.not_found");
            when(bookRepository.findById(any())).thenReturn(java.util.Optional.empty());


            var response = bookService.updateABook(book.getId(), request);

            //then
            assertTrue(response.isNotOk());
            assertNull(response.getData());

            verify(messageSource).getMessage((String) any());
            verify(bookRepository).findById(any());
            verify(bookRepository, times(0)).save(any());
        }
    }
}
