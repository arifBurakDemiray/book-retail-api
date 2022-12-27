package com.bookretail.factory;

import com.bookretail.dto.order.OrderDto;

public class OrderTestFactory {

    private OrderTestFactory() {
    }

    public static OrderDto createOrderDto() {
        return new OrderDto(
                1L,
                1L,
                1.0,
                1L,
                2L,
                null,
                null,
                null,
                null
        );
    }

}
