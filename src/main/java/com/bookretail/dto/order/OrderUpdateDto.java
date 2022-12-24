package com.bookretail.dto.order;

import com.bookretail.enums.EOrderStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class OrderUpdateDto {

    @NotNull(message = "{validation.generic.entity.not_null}")
    private EOrderStatus status;
}
