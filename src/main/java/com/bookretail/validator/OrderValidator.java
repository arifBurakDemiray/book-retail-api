package com.bookretail.validator;


import com.bookretail.dto.order.OrderCreateDto;
import com.bookretail.dto.order.OrderUpdateDto;
import com.bookretail.enums.EOrderStatus;
import com.bookretail.enums.ERole;
import com.bookretail.model.Order;
import com.bookretail.model.User;
import com.bookretail.repository.BookRepository;
import com.bookretail.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class OrderValidator {

    private final MessageSourceAccessor messageSource;

    private final OrderRepository orderRepository;

    private final BookRepository bookRepository;

    private static final List<EOrderStatus> ORDER_STATUS_LIST = List.of(EOrderStatus.APPROVED, EOrderStatus.DELIVERED);
    private static final List<EOrderStatus> ORDER_DONE_LIST = List.of(EOrderStatus.DELIVERED, EOrderStatus.CANCELLED);


    public ValidationResult validate(Long userId, String userRole, Long orderId) {
        if (userRole.equals(ERole.USER) && !orderRepository.existsByUserIdAndId(userId, orderId)) {

            return ValidationResult.failed(messageSource.getMessage("validation.order.not_found"));

        }
        return ValidationResult.success();
    }

    public ValidationResult validate(OrderCreateDto body, User user) {
        var maybeBook = bookRepository.findById(body.getBookId());

        if (maybeBook.isEmpty()) {
            return ValidationResult.failed(messageSource.getMessage("validation.order.book.not_found"));
        }

        var book = maybeBook.get();

        if (book.getBookDetail().getStock() < body.getQuantity()) {
            return ValidationResult.failed(messageSource.getMessage("validation.order.book.stock.not_enough"));
        }

        if (user.getMoney() < book.getBookDetail().getPrice() * body.getQuantity()) {
            return ValidationResult.failed(messageSource.getMessage("validation.order.user.money.not_enough"));
        }

        return ValidationResult.success();

    }

    public ValidationResult validate(OrderUpdateDto body, Order order, String userRole) {

        if (ORDER_DONE_LIST.contains(order.getStatus())) {
            return ValidationResult.failed(messageSource.getMessage("validation.order.status.not_updatable"));
        }

        if (!userRole.equals(ERole.SYSADMIN) && ORDER_STATUS_LIST.contains(body.getStatus())) {
            return ValidationResult.failed(messageSource.getMessage("validation.order.status.not_updatable"));
        }

        if (order.getStatus().equals(EOrderStatus.APPROVED) && body.getStatus().equals(EOrderStatus.CANCELLED)) {
            return ValidationResult.failed(messageSource.getMessage("validation.order.approved.not_cancelable"));
        }

        if (body.getStatus().equals(EOrderStatus.PENDING)) {
            return ValidationResult.failed(messageSource.getMessage("validation.order.pending.not_updatable"));
        }


        return ValidationResult.success();
    }
}
