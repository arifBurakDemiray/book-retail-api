package com.bookretail.dto.book;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Getter
@Setter
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
    @NotNull(message = "{validation.generic.entity.not_null}")
    @Schema
    private Double price;

    @Positive(message = "{validation.generic.number.positive}")
    @NotNull(message = "{validation.generic.entity.not_null}")
    @Schema
    private Long stock;
}
