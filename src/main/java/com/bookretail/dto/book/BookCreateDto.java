package com.bookretail.dto.book;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BookCreateDto {

    @NotBlank(message = "{validation.book.title.not_blank}")
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

    @Positive(message = "{validation.generic.number.positive}")
    @Schema
    private Double price;

    @Positive(message = "{validation.generic.number.positive}")
    @Schema
    private Long stock;
}
