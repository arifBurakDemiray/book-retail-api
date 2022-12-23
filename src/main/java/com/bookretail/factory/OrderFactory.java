package com.bookretail.factory;

import com.bookretail.dto.order.OrderDto;
import com.bookretail.model.Order;
import com.bookretail.model.Order_;
import com.bookretail.model.User_;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor
public class OrderFactory {
    public Specification<Order> getByUserId(Long userId) {
        return (root, criteriaQuery, criteriaBuilder) -> {

            criteriaQuery.orderBy(criteriaBuilder.asc(root.get(Order_.status)), criteriaBuilder.asc(root.get(Order_.createdAt)));

            return criteriaBuilder.equal(root.get(Order_.user).get(User_.id), userId);
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
