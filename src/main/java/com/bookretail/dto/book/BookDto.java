package com.bookretail.dto.book;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BookDto {

    @Schema
    private Long id;

    @Schema
    private String title;

    @Schema
    private String author;

    @Schema
    private String isbn;

    @Schema
    private String publisher;

    @Schema
    private String year;

    @Schema
    private String description;

    @Schema
    private Double price;

    @Schema
    private Long stock;
}
