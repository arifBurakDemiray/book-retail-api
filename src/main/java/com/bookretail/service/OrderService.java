package com.bookretail.service;

import com.bookretail.config.security.JwtUtil;
import com.bookretail.dto.PageFilter;
import com.bookretail.dto.Response;
import com.bookretail.dto.ServiceResponse;
import com.bookretail.dto.order.OrderCreateDto;
import com.bookretail.dto.order.OrderDto;
import com.bookretail.dto.order.OrderUpdateDto;
import com.bookretail.enums.EDetail;
import com.bookretail.enums.EErrorCode;
import com.bookretail.enums.EOrderStatus;
import com.bookretail.enums.ERole;
import com.bookretail.factory.OrderFactory;
import com.bookretail.model.Order;
import com.bookretail.model.User;
import com.bookretail.repository.BookRepository;
import com.bookretail.repository.OrderRepository;
import com.bookretail.repository.UserRepository;
import com.bookretail.util.SpecUtil;
import com.bookretail.validator.OrderValidator;
import com.bookretail.validator.coc.BookValidator;
import com.bookretail.validator.coc.UserBookValidator;
import lombok.AllArgsConstructor;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@AllArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    private final OrderFactory orderFactory;

    private final OrderValidator orderValidator;

    private final JwtUtil jwtUtil;

    private final MessageSourceAccessor messageSource;

    private final UserRepository userRepository;

    private final BookRepository bookRepository;

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

    private ServiceResponse<Order> getOrderById(Long id, User user) {
        var userId = user.getId();
        var role = ERole.stringValueOf(user.getRole());

        var validationResult = orderValidator.validate(userId, role, id);
        if (validationResult.isNotValid()) {
            return ServiceResponse.notOk(validationResult.getMessage());
        }

        var maybeOrder = orderRepository.findById(id);
        if (maybeOrder.isEmpty()) {
            return ServiceResponse.notOk(messageSource.getMessage("validation.order.not_found"));
        }

        return ServiceResponse.ok(maybeOrder.get());
    }

    @Transactional(readOnly = true)
    public Response<OrderDto> getById(Long id, String token) {
        var user = userRepository.getById(jwtUtil.getUserId(token));
        var result = getOrderById(id, user);

        if (result.isNotOk()) {
            return Response.notOk(result.getMessage(), EErrorCode.NOT_FOUND);
        }

        return Response.ok(orderFactory.createOrderDto(result.getData(), EDetail.MORE));
    }

    @Transactional
    public Response<OrderDto> createOrder(String token, OrderCreateDto body) {
        var user = userRepository.getById(jwtUtil.getUserId(token));
        var maybeBook = bookRepository.findById(body.getBookId());

        var coc = new BookValidator(new UserBookValidator(messageSource, user, maybeBook, body), messageSource, maybeBook);
        coc.validate();

        var book = bookRepository.getById(body.getBookId());
        book.getBookDetail().setStock(book.getBookDetail().getStock() - body.getQuantity());
        bookRepository.save(book);

        var order = orderFactory.createOrder(body, user, book);
        order = orderRepository.save(order);

        user.setMoney(user.getMoney() - (order.getCost()));
        userRepository.save(user);

        return Response.ok(orderFactory.createOrderDto(order, EDetail.MORE));
    }

    @Transactional
    public Response<OrderDto> updateOrder(Long id, String token, OrderUpdateDto body) {
        var user = userRepository.getById(jwtUtil.getUserId(token));
        var userRole = ERole.stringValueOf(user.getRole());
        var result = getOrderById(id, user);

        if (result.isNotOk()) {
            return Response.notOk(result.getMessage(), EErrorCode.NOT_FOUND);
        }

        var order = result.getData();
        var validation = orderValidator.validate(body, order, userRole);

        if (validation.isNotValid()) {
            return Response.notOk(validation.getMessage(), EErrorCode.BAD_REQUEST);
        }

        order.setStatus(body.getStatus());
        order.setUpdatedAt(new Date());
        order = orderRepository.save(order);

        if (body.getStatus().equals(EOrderStatus.CANCELLED)) {
            var book = order.getBook();
            book.getBookDetail().setStock(book.getBookDetail().getStock() + order.getQuantity());
            bookRepository.save(book);

            var buyer = order.getUser();
            buyer.setMoney(buyer.getMoney() + order.getCost());
            userRepository.save(buyer);
        }


        return Response.ok(orderFactory.createOrderDto(order, EDetail.MORE));
    }
}
