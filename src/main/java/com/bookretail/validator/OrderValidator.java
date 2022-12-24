package com.bookretail.validator;


import com.bookretail.enums.ERole;
import com.bookretail.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderValidator {

    private final MessageSourceAccessor messageSource;

    private final OrderRepository orderRepository;


    public ValidationResult validate(Long userId, String userRole, Long orderId) {
        if (userRole.equals(ERole.USER) && !orderRepository.existsByUserIdAndId(userId, orderId)) {

            return ValidationResult.failed(messageSource.getMessage("validation.order.not_found"));

        }
        return ValidationResult.success();
    }

}
