package com.bookretail.dto.order;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class StatisticDto {

    private String month;

    private Long totalOrder;

    private Double totalCost;

    private Long totalQuantity;

    private Long totalDeliveredOrder;
}
