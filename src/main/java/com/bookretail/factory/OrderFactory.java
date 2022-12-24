package com.bookretail.factory;

import com.bookretail.dto.auth.RegisterDto;
import com.bookretail.dto.book.BookDto;
import com.bookretail.dto.order.OrderCreateDto;
import com.bookretail.dto.order.OrderDto;
import com.bookretail.enums.EDetail;
import com.bookretail.enums.ERole;
import com.bookretail.model.*;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor
public class OrderFactory {
    public Specification<Order> getByUserId(Long userId, String role) {
        return (root, criteriaQuery, criteriaBuilder) -> {

            criteriaQuery.orderBy(criteriaBuilder.asc(root.get(Order_.status)), criteriaBuilder.asc(root.get(Order_.createdAt)));


            if (role.equals(ERole.USER)) {
                return criteriaBuilder.equal(root.get(Order_.user).get(User_.id), userId);
            } else {
                return criteriaBuilder.conjunction();
            }
        };
    }

    public OrderDto createOrderDto(Order order, EDetail detail) {
        return new OrderDto(
                order.getId(),
                order.getQuantity(),
                order.getCost(),
                order.getBook().getId(),
                order.getUser().getId(),
                order.getCreatedAt(),
                order.getUpdatedAt(),
                EDetail.isMore(detail) ? createUserDto(order.getUser()) : null,
                EDetail.isMore(detail) ? createBookDto(order.getBook()) : null
        );
    }

    private BookDto createBookDto(Book book) {
        return new BookDto(
                book.getId(),
                book.getTitle(),
                book.getAuthor(),
                book.getIsbn(),
                book.getPublisher(),
                book.getYear(),
                book.getDescription(),
                book.getBookDetail().getPrice(),
                book.getBookDetail().getStock()
        );
    }

    private RegisterDto createUserDto(User user) {
        return new RegisterDto(
                user.getId(),
                user.getName(),
                user.getSurname(),
                user.getEmail(),
                user.getPhoneNumber()
        );
    }

    public Order createOrder(OrderCreateDto body, User user, Book book) {
        return new Order(
                body.getQuantity(),
                body.getQuantity() * book.getBookDetail().getPrice(),
                book,
                user
        );
    }
}
