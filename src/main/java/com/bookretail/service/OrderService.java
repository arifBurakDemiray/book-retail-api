package com.bookretail.service;

import com.bookretail.config.security.JwtUtil;
import com.bookretail.dto.PageFilter;
import com.bookretail.dto.Response;
import com.bookretail.dto.order.OrderCreateDto;
import com.bookretail.dto.order.OrderDto;
import com.bookretail.dto.order.OrderUpdateDto;
import com.bookretail.enums.EDetail;
import com.bookretail.enums.EErrorCode;
import com.bookretail.factory.OrderFactory;
import com.bookretail.model.Order;
import com.bookretail.repository.OrderRepository;
import com.bookretail.util.SpecUtil;
import com.bookretail.validator.OrderValidator;
import lombok.AllArgsConstructor;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    private final OrderFactory orderFactory;

    private final OrderValidator orderValidator;

    private final JwtUtil jwtUtil;

    private final MessageSourceAccessor messageSource;

    @Transactional(readOnly = true)
    public Response<Page<OrderDto>> getAll(String token, Specification<Order> spec, PageFilter pageFilter) {
        var userId = jwtUtil.getUserId(token);
        var role = jwtUtil.getUserRole(token);

        return Response.ok(
                orderRepository
                        .findAll(SpecUtil.and(spec, orderFactory.getByUserId(userId, role)), pageFilter.asPageable())
                        .map(order -> orderFactory.createOrderDto(order, EDetail.LESS))
        );
    }

    @Transactional(readOnly = true)
    public Response<OrderDto> getById(Long id, String token) {
        var userId = jwtUtil.getUserId(token);
        var role = jwtUtil.getUserRole(token);
        var validationResult = orderValidator.validate(userId, role, id);
        if (validationResult.isNotValid()) {
            return Response.notOk(validationResult.getMessage(), EErrorCode.BAD_REQUEST);
        }

        var maybeOrder = orderRepository.findById(id);
        if (maybeOrder.isEmpty()) {
            return Response.notOk(messageSource.getMessage("validation.order.not_found"), EErrorCode.NOT_FOUND);
        }

        return Response.ok(orderFactory.createOrderDto(maybeOrder.get(), EDetail.MORE));
    }

    @Transactional
    public Response<OrderDto> createOrder(String token, OrderCreateDto body) {
        return Response.ok(null);
    }

    @Transactional
    public Response<OrderDto> updateOrder(Long id, String token, OrderUpdateDto body) {
        return Response.ok(null);
    }
}
