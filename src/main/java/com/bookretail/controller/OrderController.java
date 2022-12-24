package com.bookretail.controller;


import com.bookretail.dto.PageFilter;
import com.bookretail.dto.Response;
import com.bookretail.dto.order.OrderDto;
import com.bookretail.service.OrderService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<Response<Page<OrderDto>>> getAllOrdersForCustomer(
            PageFilter pageFilter,
            @Parameter(hidden = true) @RequestHeader(name = HttpHeaders.AUTHORIZATION, required = false) String token) {
        return orderService.getAll(token, pageFilter).toResponseEntity();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response<OrderDto>> getById(
            @PathVariable Long id,
            @Parameter(hidden = true) @RequestHeader(name = HttpHeaders.AUTHORIZATION, required = false) String token) {
        return orderService.getById(id, token).toResponseEntity();

    }
}
