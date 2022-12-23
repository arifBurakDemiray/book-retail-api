package com.bookretail.controller;

import com.bookretail.dto.PageFilter;
import com.bookretail.dto.book.BookCreateDto;
import com.bookretail.dto.book.BookUpdateDto;
import com.bookretail.enums.ERole;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;

@RestController
@Tag(name = BookController.tag, description = BookController.description)
@RequestMapping(AuthController.tag)
@AllArgsConstructor
public class BookController {
    public static final String description = "Book related endpoints.";
    public static final String tag = "book";

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public void getAllBooks(PageFilter pageFilter) {
    }

    @PostMapping
    @RolesAllowed(ERole.SYSADMIN)
    public void createABook(@RequestBody @Valid BookCreateDto body) {
    }

    @PatchMapping("/{id}")
    @RolesAllowed(ERole.SYSADMIN)
    public void updateABook(@PathVariable Long id, @RequestBody BookUpdateDto body) {
    }
}
