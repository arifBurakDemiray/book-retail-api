package com.bookretail.factory;

import com.bookretail.dto.order.OrderDto;
import com.bookretail.enums.ERole;
import com.bookretail.model.Order;
import com.bookretail.model.Order_;
import com.bookretail.model.User_;
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

    public OrderDto createOrderDto(Order order) {
        return new OrderDto(
                order.getId(),
                order.getQuantity(),
                order.getCost(),
                order.getBook().getId(),
                order.getUser().getId(),
                order.getCreatedAt(),
                order.getUpdatedAt()
        );
    }
}
