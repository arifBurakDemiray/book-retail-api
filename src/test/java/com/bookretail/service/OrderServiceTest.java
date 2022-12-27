package com.bookretail.service;


import com.bookretail.config.security.JwtUtil;
import com.bookretail.dto.PageFilter;
import com.bookretail.dto.ServiceResponse;
import com.bookretail.dto.order.OrderCreateDto;
import com.bookretail.dto.order.OrderUpdateDto;
import com.bookretail.enums.EDetail;
import com.bookretail.enums.EOrderStatus;
import com.bookretail.enums.ERole;
import com.bookretail.factory.BookTestFactory;
import com.bookretail.factory.OrderFactory;
import com.bookretail.factory.OrderTestFactory;
import com.bookretail.factory.UserTestFactory;
import com.bookretail.repository.BookRepository;
import com.bookretail.repository.OrderRepository;
import com.bookretail.repository.UserRepository;
import com.bookretail.validator.OrderValidator;
import com.bookretail.validator.ValidationResult;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.AuthenticationException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ContextConfiguration(classes = {OrderService.class})
@ExtendWith(SpringExtension.class)
public class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    @MockBean
    private OrderRepository orderRepository;

    @MockBean
    private OrderFactory orderFactory;

    @MockBean
    private OrderValidator orderValidator;

    @MockBean
    private JwtUtil jwtUtil;

    @MockBean
    private MessageSourceAccessor messageSource;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private BookRepository bookRepository;

    @Nested
    class CreateOrder_Method_Test_Cases {

        @Test
        void CreateOrder_Success() throws AuthenticationException {
            //given
            var request = OrderTestFactory.createOrderCreateDto();
            var book = BookTestFactory.createBook();
            book.setBookDetail(BookTestFactory.createBookDetail(book));
            var user = UserTestFactory.createSuccessTestUser_USER();
            user.setMoney(1000.0);
            var order = OrderTestFactory.createOrder(book, user);
            var result = OrderTestFactory.createOrderDto(EDetail.MORE);
            var token = UserTestFactory.token_USER;
            var validation = mock(ValidationResult.class);
            //when
            when(jwtUtil.getUserId(any())).thenReturn(user.getId());
            when(userRepository.getById(any())).thenReturn(user);
            when(orderValidator.validate(any(), any())).thenReturn(validation);
            when(validation.isNotValid()).thenReturn(false);
            when(orderFactory.createOrder(any(OrderCreateDto.class), any(), any())).thenReturn(order);
            when(bookRepository.getById(any())).thenReturn(book);
            when(orderRepository.save(any())).thenReturn(order);
            when(userRepository.save(any())).thenReturn(user);
            when(orderFactory.createOrderDto(any(), any())).thenReturn(result);

            var response = orderService.createOrder(token, request);

            //then
            assertTrue(response.isOk());

            verify(orderFactory).createOrder(any(OrderCreateDto.class), any(), any());
            verify(bookRepository).getById(any());
            verify(orderRepository).save(any());
            verify(userRepository).save(any());
            verify(orderFactory).createOrderDto(any(), any());
        }


        @Test
        void CreateOrder_Fails_ValidationFails() throws AuthenticationException {
            //given
            var request = OrderTestFactory.createOrderCreateDto();
            var book = BookTestFactory.createBook();
            book.setBookDetail(BookTestFactory.createBookDetail(book));
            var user = UserTestFactory.createSuccessTestUser_USER();

            var token = UserTestFactory.token_USER;
            var validation = mock(ValidationResult.class);
            //when
            when(jwtUtil.getUserId(any())).thenReturn(user.getId());
            when(userRepository.getById(any())).thenReturn(user);
            when(orderValidator.validate(any(), any())).thenReturn(validation);
            when(messageSource.getMessage((String) any())).thenReturn("Message");
            when(validation.getMessage()).thenReturn("Message");
            when(validation.isNotValid()).thenReturn(true);


            var response = orderService.createOrder(token, request);

            //then
            assertNull(response.getData());
            assertFalse(response.isOk());

            verify(orderValidator).validate(any(), any());
            verify(validation).isNotValid();
            verify(validation).getMessage();
        }

    }

    @Nested
    class GelAllOrders_Method_Test_Cases {

        @Test
        void GetAllOrders_Success() throws AuthenticationException {
            //given
            var user = UserTestFactory.createSuccessTestUser_USER();
            var token = UserTestFactory.token_USER;
            var book = BookTestFactory.createBook();
            var order = OrderTestFactory.createOrder(book, user);
            var result = OrderTestFactory.createOrderDto(EDetail.MORE);
            var page = new PageImpl<>(Collections.singletonList(order));
            //when
            when(jwtUtil.getUserId(any())).thenReturn(user.getId());
            when(jwtUtil.getUserRole(any())).thenReturn(ERole.stringValueOf(user.getRole()));
            when(userRepository.getById(any())).thenReturn(user);
            when(orderRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(page);
            when(orderFactory.createOrderDto(any(), any())).thenReturn(result);

            var response = orderService.getAll(token, Specification.where(null), new PageFilter(1, 0, null, null));

            //then
            assertTrue(response.isOk());

            verify(orderRepository).findAll(any(Specification.class), any(Pageable.class));
            verify(orderFactory).createOrderDto(any(), any());
        }

        @Test
        void GetAllOrders_Fails_NegativePage() throws AuthenticationException {
            //given
            var user = UserTestFactory.createSuccessTestUser_USER();
            var token = UserTestFactory.token_USER;
            var book = BookTestFactory.createBook();
            var order = OrderTestFactory.createOrder(book, user);
            var result = OrderTestFactory.createOrderDto(EDetail.MORE);
            var page = new PageImpl<>(Collections.singletonList(order));
            var pageFiler = new PageFilter(-1, 0, null, null);
            //when
            when(jwtUtil.getUserId(any())).thenReturn(user.getId());
            when(jwtUtil.getUserRole(any())).thenReturn(ERole.stringValueOf(user.getRole()));
            when(userRepository.getById(any())).thenReturn(user);
            //then
            assertThrows(IllegalArgumentException.class, () -> orderService.getAll(token, Specification.where(null), pageFiler));

            verify(orderRepository, times(0)).findAll(any(Specification.class), any(Pageable.class));
        }

    }

    @Nested
    class GelById_Method_Test_Cases {

        @Test
        void GetById_Success() throws AuthenticationException {
            //given
            var user = UserTestFactory.createSuccessTestUser_USER();
            var token = UserTestFactory.token_USER;
            var book = BookTestFactory.createBook();
            var order = OrderTestFactory.createOrder(book, user);
            var result = OrderTestFactory.createOrderDto(EDetail.MORE);
            var validation = mock(ValidationResult.class);
            //when
            when(orderValidator.validate(any(Long.class), any(String.class), any(Long.class))).thenReturn(validation);
            when(validation.isNotValid()).thenReturn(false);
            when(orderRepository.findById(any())).thenReturn(Optional.of(order));
            when(jwtUtil.getUserId(any())).thenReturn(user.getId());
            when(jwtUtil.getUserRole(any())).thenReturn(ERole.stringValueOf(user.getRole()));
            when(userRepository.getById(any())).thenReturn(user);
            when(orderRepository.getById(any())).thenReturn(order);
            when(orderFactory.createOrderDto(any(), any())).thenReturn(result);

            var response = orderService.getById(1L, token);

            //then
            assertTrue(response.isOk());

            verify(orderRepository, times(1)).findById(any());
            verify(orderFactory).createOrderDto(any(), any());
        }

        @Test
        void GetById_Fails_ValidationFails() throws AuthenticationException {
            //given
            var user = UserTestFactory.createSuccessTestUser_USER();
            var token = UserTestFactory.token_USER;
            var book = BookTestFactory.createBook();
            var order = OrderTestFactory.createOrder(book, user);
            var result = OrderTestFactory.createOrderDto(EDetail.MORE);
            var validation = mock(ValidationResult.class);
            //when
            when(jwtUtil.getUserId(any())).thenReturn(user.getId());
            when(jwtUtil.getUserRole(any())).thenReturn(ERole.stringValueOf(user.getRole()));
            when(userRepository.getById(any())).thenReturn(user);
            when(orderValidator.validate(any(Long.class), any(String.class), any(Long.class))).thenReturn(validation);
            when(validation.isNotValid()).thenReturn(true);
            when(validation.getMessage()).thenReturn("Message");
            when(messageSource.getMessage((String) any())).thenReturn("Message");

            var response = orderService.getById(1L, token);

            //then
            assertFalse(response.isOk());

        }

        @Test
        void GetById_Fails_NotFound() throws AuthenticationException {
            //given
            var user = UserTestFactory.createSuccessTestUser_SYSADMIN();
            var token = UserTestFactory.token_USER;
            var book = BookTestFactory.createBook();
            var order = OrderTestFactory.createOrder(book, user);
            var result = OrderTestFactory.createOrderDto(EDetail.MORE);
            var validation = mock(ValidationResult.class);
            //when
            when(jwtUtil.getUserId(any())).thenReturn(user.getId());
            when(jwtUtil.getUserRole(any())).thenReturn(ERole.stringValueOf(user.getRole()));
            when(userRepository.getById(any())).thenReturn(user);
            when(orderValidator.validate(any(Long.class), any(String.class), any(Long.class))).thenReturn(validation);
            when(validation.isNotValid()).thenReturn(false);
            when(orderRepository.findById(any())).thenReturn(Optional.empty());
            when(messageSource.getMessage((String) any())).thenReturn("Message");

            var response = orderService.getById(1L, token);

            //then
            assertFalse(response.isOk());

        }

    }

    @Nested
    class UpdateOrder_Method_Test_Cases {
        @Test
        void UpdateOrder_Fails_ValidationFails() throws AuthenticationException {
            //given
            var user = UserTestFactory.createSuccessTestUser_USER();
            var token = UserTestFactory.token_USER;
            var book = BookTestFactory.createBook();
            var order = OrderTestFactory.createOrder(book, user);
            var result = OrderTestFactory.createOrderDto(EDetail.MORE);
            var validation = mock(ValidationResult.class);
            var validation2 = mock(ValidationResult.class);
            //when
            when(jwtUtil.getUserId(any())).thenReturn(user.getId());
            when(jwtUtil.getUserRole(any())).thenReturn(ERole.stringValueOf(user.getRole()));
            when(userRepository.getById(any())).thenReturn(user);
            when(orderValidator.validate(any(Long.class), any(String.class), any(Long.class))).thenReturn(validation);
            when(validation.isNotValid()).thenReturn(false);
            when(orderValidator.validate(any(OrderUpdateDto.class), any(), any())).thenReturn(validation2);
            when(validation2.isNotValid()).thenReturn(true);
            when(validation2.getMessage()).thenReturn("Message");
            when(messageSource.getMessage((String) any())).thenReturn("Message");

            var response = orderService.updateOrder(1L, token, new OrderUpdateDto(EOrderStatus.APPROVED));

            //then
            assertFalse(response.isOk());

        }

        @Test
        void UpdateOrder_Fails_NotFound() throws AuthenticationException {
            //given
            var user = UserTestFactory.createSuccessTestUser_SYSADMIN();
            var token = UserTestFactory.token_USER;
            var book = BookTestFactory.createBook();
            var order = OrderTestFactory.createOrder(book, user);
            var result = OrderTestFactory.createOrderDto(EDetail.MORE);
            var validation = mock(ValidationResult.class);
            //when
            when(jwtUtil.getUserId(any())).thenReturn(user.getId());
            when(jwtUtil.getUserRole(any())).thenReturn(ERole.stringValueOf(user.getRole()));
            when(userRepository.getById(any())).thenReturn(user);
            when(orderValidator.validate(any(Long.class), any(String.class), any(Long.class))).thenReturn(validation);
            when(validation.isNotValid()).thenReturn(false);
            when(orderRepository.findById(any())).thenReturn(Optional.empty());
            when(messageSource.getMessage((String) any())).thenReturn("Message");

            var response = orderService.updateOrder(1L, token, new OrderUpdateDto(EOrderStatus.APPROVED));

            //then
            assertFalse(response.isOk());

        }

        @Test
        void UpdateOrder_Success() throws AuthenticationException {
            //given
            var user = UserTestFactory.createSuccessTestUser_SYSADMIN();
            var token = UserTestFactory.token_USER;
            var book = BookTestFactory.createBook();
            var order = OrderTestFactory.createOrder(book, user);
            var result = OrderTestFactory.createOrderDto(EDetail.MORE);
            var validation = mock(ValidationResult.class);
            var validation2 = mock(ValidationResult.class);
            var serviceRsp = mock(ServiceResponse.class);
            //when
            when(jwtUtil.getUserId(any())).thenReturn(user.getId());
            when(jwtUtil.getUserRole(any())).thenReturn(ERole.stringValueOf(user.getRole()));
            when(userRepository.getById(any())).thenReturn(user);
            when(orderValidator.validate(any(Long.class), any(String.class), any(Long.class))).thenReturn(validation);
            when(validation.isNotValid()).thenReturn(false);
            when(orderRepository.findById(any())).thenReturn(Optional.of(order));
            when(orderValidator.validate(any(OrderUpdateDto.class), any(), any())).thenReturn(validation2);
            when(validation2.isNotValid()).thenReturn(false);
            when(orderRepository.save(any())).thenReturn(order);
            when(orderFactory.createOrderDto(any(), any())).thenReturn(result);

            var response = orderService.updateOrder(1L, token, new OrderUpdateDto(EOrderStatus.APPROVED));

            //then
            assertTrue(response.isOk());

        }

        @Test
        void UpdateOrder_Success_Canceled() throws AuthenticationException {
            //given
            var user = UserTestFactory.createSuccessTestUser_SYSADMIN();
            user.setMoney(1000.0);
            var token = UserTestFactory.token_USER;
            var book = BookTestFactory.createBook();
            book.setBookDetail(BookTestFactory.createBookDetail(book));
            var order = OrderTestFactory.createOrder(book, user);
            var result = OrderTestFactory.createOrderDto(EDetail.MORE);
            var validation = mock(ValidationResult.class);
            var validation2 = mock(ValidationResult.class);
            var serviceRsp = mock(ServiceResponse.class);
            //when
            when(jwtUtil.getUserId(any())).thenReturn(user.getId());
            when(jwtUtil.getUserRole(any())).thenReturn(ERole.stringValueOf(user.getRole()));
            when(userRepository.getById(any())).thenReturn(user);
            when(orderValidator.validate(any(Long.class), any(String.class), any(Long.class))).thenReturn(validation);
            when(validation.isNotValid()).thenReturn(false);
            when(orderRepository.findById(any())).thenReturn(Optional.of(order));
            when(orderValidator.validate(any(OrderUpdateDto.class), any(), any())).thenReturn(validation2);
            when(validation2.isNotValid()).thenReturn(false);
            when(bookRepository.save(any())).thenReturn(book);
            when(orderRepository.save(any())).thenReturn(order);
            when(orderFactory.createOrderDto(any(), any())).thenReturn(result);

            var response = orderService.updateOrder(1L, token, new OrderUpdateDto(EOrderStatus.CANCELLED));

            //then
            assertTrue(response.isOk());

        }
    }
}
