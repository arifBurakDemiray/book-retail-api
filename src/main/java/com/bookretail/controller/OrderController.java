package com.bookretail.controller;


import com.bookretail.dto.PageFilter;
import com.bookretail.dto.Response;
import com.bookretail.dto.order.OrderCreateDto;
import com.bookretail.dto.order.OrderDto;
import com.bookretail.dto.order.OrderUpdateDto;
import com.bookretail.enums.ERole;
import com.bookretail.service.OrderService;
import com.bookretail.specification.OrderFilterParams;
import com.bookretail.specification.OrderFilterSpec;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;

@RestController
@Tag(name = OrderController.tag, description = OrderController.description)
@RequestMapping(OrderController.tag)
@AllArgsConstructor
public class OrderController {

    public static final String description = "Order related endpoints.";
    public static final String tag = "order";

    private final OrderService orderService;

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    @OrderFilterParams
    public ResponseEntity<Response<Page<OrderDto>>> getAllOrders(
            PageFilter pageFilter,
            @Parameter(hidden = true) OrderFilterSpec spec,
            @Parameter(hidden = true) @RequestHeader(name = HttpHeaders.AUTHORIZATION, required = false) String token) {
        return orderService.getAll(token, spec, pageFilter).toResponseEntity();
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Response<OrderDto>> getById(
            @PathVariable Long id,
            @Parameter(hidden = true) @RequestHeader(name = HttpHeaders.AUTHORIZATION, required = false) String token) {
        return orderService.getById(id, token).toResponseEntity();

    }

    @PostMapping
    @RolesAllowed(ERole.USER)
    public ResponseEntity<Response<OrderDto>> createOrder(
            @RequestBody @Valid OrderCreateDto body,
            @Parameter(hidden = true) @RequestHeader(name = HttpHeaders.AUTHORIZATION, required = false) String token) {
        return orderService.createOrder(token, body).toResponseEntity();

    }

    @PatchMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Response<OrderDto>> updateOrder(
            @PathVariable Long id,
            @RequestBody @Valid OrderUpdateDto body,
            @Parameter(hidden = true) @RequestHeader(name = HttpHeaders.AUTHORIZATION, required = false) String token) {
        return orderService.updateOrder(id, token, body).toResponseEntity();

    }
}
