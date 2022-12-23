package com.bookretail.controller;

import com.bookretail.dto.PageFilter;
import com.bookretail.dto.Response;
import com.bookretail.dto.book.BookCreateDto;
import com.bookretail.dto.book.BookDto;
import com.bookretail.dto.book.BookUpdateDto;
import com.bookretail.enums.ERole;
import com.bookretail.service.BookService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;

@RestController
@Tag(name = BookController.tag, description = BookController.description)
@RequestMapping(BookController.tag)
@AllArgsConstructor
public class BookController {

    public static final String description = "Book related endpoints.";
    public static final String tag = "book";

    private final BookService bookService;

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Response<Page<BookDto>>> getAllBooks(PageFilter pageFilter) {
        return bookService.getAllBooks(pageFilter).toResponseEntity();
    }

    @PostMapping
    @RolesAllowed(ERole.SYSADMIN)
    public ResponseEntity<Response<BookDto>> createABook(@RequestBody @Valid BookCreateDto body) {
        return bookService.createABook(body).toResponseEntity();
    }

    @PatchMapping("/{id}")
    @RolesAllowed(ERole.SYSADMIN)
    public ResponseEntity<Response<BookDto>> updateABook(@PathVariable Long id, @RequestBody BookUpdateDto body) {
        return bookService.updateABook(id, body).toResponseEntity();
    }
}
