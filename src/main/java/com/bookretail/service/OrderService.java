package com.bookretail.service;

import com.bookretail.config.security.JwtUtil;
import com.bookretail.dto.PageFilter;
import com.bookretail.dto.Response;
import com.bookretail.dto.order.OrderDto;
import com.bookretail.factory.OrderFactory;
import com.bookretail.repository.OrderRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    private final OrderFactory orderFactory;

    private final JwtUtil jwtUtil;

    @Transactional(readOnly = true)
    public Response<Page<OrderDto>> getAll(String token, PageFilter pageFilter) {
        var userId = jwtUtil.getUserId(token);

        return Response.ok(orderRepository.findAll(orderFactory.getByUserId(userId), pageFilter.asPageable()).map(orderFactory::createOrderDto));
    }
}
