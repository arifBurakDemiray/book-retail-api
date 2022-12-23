package com.bookretail.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PageFilter {

    @Schema(description = "If you do not give, its default value is 20")
    private Integer page_size;

    @Schema(description = "If you do not give, its default value is 0")
    private Integer page;

    @Schema(description = "If you do not give, you can not sort given items")
    private Sort.Direction direction;

    @Schema(description = "It is the field by sorted, if you do not give you can not sort given items")
    private String fields;

    public Pageable asPageable() {
        if (direction != null && fields != null) {
            return PageRequest.of(page != null ? page : 0, page_size != null ? page_size : 20
                    , Sort.by(direction, fields.split(",")));
        }
        return PageRequest.of(page != null ? page : 0, page_size != null ? page_size : 20);
    }
}
