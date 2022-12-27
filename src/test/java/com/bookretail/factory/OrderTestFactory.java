package com.bookretail.factory;

import com.bookretail.dto.book.BookDto;
import com.bookretail.dto.order.OrderCreateDto;
import com.bookretail.dto.order.OrderDto;
import com.bookretail.enums.EDetail;
import com.bookretail.enums.EOrderStatus;
import com.bookretail.model.Book;
import com.bookretail.model.Order;
import com.bookretail.model.User;

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
                EOrderStatus.CANCELLED,
                null,
                null,
                detail.equals(EDetail.MORE) ? UserTestFactory.createRegisterDto() : null,
                detail.equals(EDetail.MORE) ? new BookDto() : null
        );
    }

    public static Order createOrder(Book book, User user) {
        return new Order(
                1L,
                1.0,
                book,
                user
        );
    }

    public static OrderCreateDto createOrderCreateDto() {
        return new OrderCreateDto(
                1L,
                1L
        );
    }

}
