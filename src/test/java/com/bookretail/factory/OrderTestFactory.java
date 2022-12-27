package com.bookretail.factory;

import com.bookretail.dto.book.BookDto;
import com.bookretail.dto.order.OrderCreateDto;
import com.bookretail.dto.order.OrderDto;
import com.bookretail.enums.EDetail;

public class OrderTestFactory {

    private OrderTestFactory() {
    }

    public static OrderDto createOrderDto(EDetail detail) {
        return new OrderDto(
                1L,
                1L,
                1.0,
                1L,
                2L,
                null,
                null,
                detail.equals(EDetail.MORE) ? UserTestFactory.createRegisterDto() : null,
                detail.equals(EDetail.MORE) ? new BookDto() : null
        );
    }

    public static OrderCreateDto createOrderCreateDto() {
        return new OrderCreateDto(
                1L,
                1L
        );
    }
    
}
