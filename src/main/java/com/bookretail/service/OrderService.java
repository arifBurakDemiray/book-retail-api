package com.bookretail.service;

import com.bookretail.config.security.JwtUtil;
import com.bookretail.dto.PageFilter;
import com.bookretail.dto.Response;
import com.bookretail.dto.order.OrderDto;
import com.bookretail.enums.EDetail;
import com.bookretail.enums.EErrorCode;
import com.bookretail.factory.OrderFactory;
import com.bookretail.repository.OrderRepository;
import com.bookretail.validator.OrderValidator;
import lombok.AllArgsConstructor;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.data.domain.Page;
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
    public Response<Page<OrderDto>> getAll(String token, PageFilter pageFilter) {
        var userId = jwtUtil.getUserId(token);
        var role = jwtUtil.getUserRole(token);

        return Response.ok(
                orderRepository
                        .findAll(orderFactory.getByUserId(userId, role), pageFilter.asPageable())
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
}
