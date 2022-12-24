package com.bookretail.dto.order;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class OrderCreateDto {

    @Positive(message = "{validation.generic.number.positive}")
    @NotNull(message = "{validation.generic.entity.not_null}")
    private Long bookId;

    @Positive(message = "{validation.generic.number.positive}")
    @NotNull(message = "{validation.generic.entity.not_null}")
    private Long quantity;
}
