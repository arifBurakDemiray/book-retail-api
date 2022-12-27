package com.bookretail.controller;

import com.bookretail.dto.Response;
import com.bookretail.dto.order.OrderCreateDto;
import com.bookretail.dto.order.OrderDto;
import com.bookretail.dto.order.OrderUpdateDto;
import com.bookretail.enums.EDetail;
import com.bookretail.enums.EErrorCode;
import com.bookretail.enums.EOrderStatus;
import com.bookretail.enums.ERole;
import com.bookretail.factory.OrderTestFactory;
import com.bookretail.service.OrderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class OrderControllerTest {

    private final static String CONTENT_TYPE = "application/json";

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    @MockBean
    private MessageSource messageSource;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @Nested
    class GetAllOrders_Endpoint_Test_Cases {

        @Test
        @WithMockUser("spring")
        void GetAllOrders_Returns200() throws Exception {
            //given
            Page<OrderDto> page = new PageImpl<>(new ArrayList<>());

            //when
            when(orderService.getAll(any(), any(), any())).thenReturn(Response.ok(page));
            ResultActions actions = mockMvc.perform(
                    get("/order")).andDo(print());

            //then
            actions.andExpect(status().isOk());
            verify(orderService).getAll(any(), any(), any());
        }

        @Test
        @WithMockUser(authorities = {ERole.SYSADMIN})
        void GetAllOrders_Returns200_SYSADMIN() throws Exception {

            //given
            var order = OrderTestFactory.createOrderDto(EDetail.LESS);

            Page<OrderDto> page = new PageImpl<>(Collections.singletonList(order));

            //when
            when(orderService.getAll(any(), any(), any())).thenReturn(Response.ok(page));
            ResultActions actions = mockMvc.perform(
                    get("/order")).andDo(print());


            //then
            actions.andExpect(status().isOk());
            actions.andExpect(jsonPath("$.data.content[0].id").value(order.getId()));
            actions.andExpect(jsonPath("$.data.content[0].user").doesNotExist());
            verify(orderService).getAll(any(), any(), any());

        }

        @Test
        @WithMockUser(authorities = {ERole.USER})
        void GetAllOrders_Returns200_Empty_USER() throws Exception {

            //given
            var order = OrderTestFactory.createOrderDto(EDetail.LESS);

            Page<OrderDto> page = new PageImpl<>(new ArrayList<>());

            //when
            when(orderService.getAll(any(), any(), any())).thenReturn(Response.ok(page));
            ResultActions actions = mockMvc.perform(
                    get("/order")).andDo(print());


            //then
            actions.andExpect(status().isOk());
            actions.andExpect(jsonPath("$.data.content").isEmpty());
            verify(orderService).getAll(any(), any(), any());

        }

        @Test
        void GetAllOrders_Returns401() throws Exception {
            //given
            when(messageSource.getMessage(any(), any(), any()))
                    .thenReturn("Message");
            //when
            ResultActions actions = mockMvc.perform(
                    get("/order")).andDo(print());

            //then
            actions.andExpect(status().is(401));
        }

        @Test
        @WithMockUser("spring")
        void GetAllOrders_Returns500_PageInvalid() throws Exception {
            //given
            //when
            when(messageSource.getMessage(any(), any(), any()))
                    .thenReturn("Message");
            ResultActions actions = mockMvc.perform(
                            get("/order")
                                    .queryParam("page", "-1")
                                    .queryParam("page_size", "10"))
                    .andDo(print());

            //then
            actions.andExpect(status().isInternalServerError());
        }

    }

    @Nested
    class GetById_Endpoint_Test_Cases {
        @Test
        @WithMockUser("spring")
        void GetById_Returns200() throws Exception {
            //given
            var body = OrderTestFactory.createOrderDto(EDetail.MORE);
            var response = Response.ok(body);

            //when
            when(orderService.getById(any(), any())).thenReturn(response);
            var sBuilder = new StringBuilder("/order/");
            sBuilder.append(body.getId());

            ResultActions actions = mockMvc.perform(
                            get(sBuilder.toString()))
                    .andDo(print());

            //then
            verify(orderService).getById(any(), any());
            actions.andExpect(jsonPath("$.data.user").hasJsonPath());
            actions.andExpect(status().isOk());

        }

        @Test
        @WithMockUser(username = "spring", authorities = {ERole.USER})
        void GetById_Returns404_USER() throws Exception {
            //given
            var body = OrderTestFactory.createOrderDto(EDetail.MORE);

            var sBuilder = new StringBuilder("/order/");
            sBuilder.append(body.getId());
            Response<OrderDto> response = Response.notOk("Message", EErrorCode.NOT_FOUND);

            //when
            when(messageSource.getMessage(any(), any(), any()))
                    .thenReturn("Message");
            when(orderService.getById(any(), any())).thenReturn(response);
            sBuilder.append(body.getId());

            //when

            ResultActions actions = mockMvc.perform(
                            get(sBuilder.toString()))
                    .andDo(print());


            //then
            verify(orderService, times(1)).getById(any(), any());
            actions.andExpect(status().is(404));

        }

        @Test
        void GetById_Returns401() throws Exception {
            //given
            var body = OrderTestFactory.createOrderDto(EDetail.MORE);
            var sBuilder = new StringBuilder("/order/");
            sBuilder.append(body.getId());
            //when

            when(messageSource.getMessage(any(), any(), any()))
                    .thenReturn("Message");

            ResultActions actions = mockMvc.perform(
                            get(sBuilder.toString()))
                    .andDo(print());

            //then
            verify(orderService, times(0)).getById(anyLong(), anyString());
            actions.andExpect(status().is(401));

        }

        @Test
        @WithMockUser(authorities = {ERole.SYSADMIN})
        void GetById_Returns500() throws Exception {
            //given

            var sBuilder = new StringBuilder("/order/undefined");
            when(messageSource.getMessage(any(), any(), any()))
                    .thenReturn("Message");
            ResultActions actions = mockMvc.perform(
                            get(sBuilder.toString()))
                    .andDo(print());


            //then
            verify(orderService, times(0)).getById(anyLong(), anyString());
            actions.andExpect(status().is(500));

        }
    }

    @Nested
    class CreateOrder_Endpoint_Test_Cases {
        @Test
        @WithMockUser(value = "spring", authorities = {ERole.SYSADMIN})
        void CreateOrder_Returns401_SYSADMIN() throws Exception {

            //given
            var body = OrderTestFactory.createOrderCreateDto();
            //when
            when(messageSource.getMessage(any(), any(), any()))
                    .thenReturn("Message");
            ResultActions actions = mockMvc.perform(
                            post("/order")
                                    .contentType(CONTENT_TYPE)
                                    .content(objectMapper.writeValueAsString(body)))
                    .andDo(print());

            //then
            actions.andExpect(status().is(401));

        }

        @Test
        void CreateOrder_Returns401() throws Exception {

            //given
            var body = OrderTestFactory.createOrderCreateDto();

            //when
            when(messageSource.getMessage(any(), any(), any()))
                    .thenReturn("Message");

            ResultActions actions = mockMvc.perform(
                            post("/order")
                                    .contentType(CONTENT_TYPE)
                                    .content(objectMapper.writeValueAsString(body)))
                    .andDo(print());

            //then
            actions.andExpect(status().is(401));

        }


        @Test
        @WithMockUser(authorities = {ERole.USER})
        void CreateOrder_Returns200() throws Exception {

            //given
            var body = OrderTestFactory.createOrderCreateDto();
            var response = Response.ok(OrderTestFactory.createOrderDto(EDetail.MORE));

            //when
            when(orderService.createOrder(any(), any(OrderCreateDto.class))).thenReturn(response);

            ResultActions actions = mockMvc.perform(
                            post("/order")
                                    .contentType(CONTENT_TYPE)
                                    .content(objectMapper.writeValueAsString(body)))
                    .andDo(print());


            //then
            ArgumentCaptor<OrderCreateDto> captor = ArgumentCaptor.forClass(OrderCreateDto.class);
            verify(orderService).createOrder(any(), captor.capture());
            assertThat(captor.getValue().getBookId()).isEqualTo(body.getBookId());
            assertThat(captor.getValue().getQuantity()).isEqualTo(body.getQuantity());
            actions.andExpect(jsonPath("$.data.user").hasJsonPath());
            actions.andExpect(status().isOk());

        }

        @Test
        @WithMockUser(authorities = {ERole.USER})
        void CreateOrder_Returns400_Null() throws Exception {

            //given
            var body = OrderTestFactory.createOrderCreateDto();
            body.setBookId(null);
            body.setQuantity(null);

            //when
            ResultActions actions = mockMvc.perform(
                            post("/order")
                                    .contentType(CONTENT_TYPE)
                                    .content(objectMapper.writeValueAsString(body)))
                    .andDo(print());


            //then
            verify(orderService, times(0)).createOrder(any(), any(OrderCreateDto.class));

            actions.andExpect(mvcResult ->
                    assertThat(mvcResult.getResponse().getContentAsString()).contains("validation.generic.entity.not_null"));
            actions.andExpect(status().is(400));

        }

        @Test
        @WithMockUser(authorities = {ERole.USER})
        void CreateOrder_Returns400_NotPositive() throws Exception {
            //Successful post test
            //given
            var body = OrderTestFactory.createOrderCreateDto();
            body.setBookId(-1L);
            body.setQuantity(-1L);

            //when

            ResultActions actions = mockMvc.perform(
                            post("/order")
                                    .contentType(CONTENT_TYPE)
                                    .content(objectMapper.writeValueAsString(body)))
                    .andDo(print());


            verify(orderService, times(0)).createOrder(any(), any(OrderCreateDto.class));

            actions.andExpect(mvcResult ->
                    assertThat(mvcResult.getResponse().getContentAsString()).contains("validation.generic.number.positive"));
            actions.andExpect(status().is(400));

        }
    }

    @Nested
    class UpdateOrder_Endpoint_Test_Cases {
        @Test
        @WithMockUser(username = "spring", authorities = {ERole.SYSADMIN})
        void UpdateOrder_Returns404() throws Exception {
            //given
            var result = OrderTestFactory.createOrderDto(EDetail.MORE);
            var body = new OrderUpdateDto(EOrderStatus.APPROVED);

            var sBuilder = new StringBuilder("/order/");
            sBuilder.append(result.getId());
            Response<OrderDto> response = Response.notOk("Message", EErrorCode.NOT_FOUND);

            //when
            when(messageSource.getMessage(any(), any(), any()))
                    .thenReturn("Message");
            when(orderService.updateOrder(any(), any(), any(OrderUpdateDto.class))).thenReturn(response);

            //when

            ResultActions actions = mockMvc.perform(patch(sBuilder.toString())
                    .contentType(CONTENT_TYPE)
                    .content(objectMapper.writeValueAsString(body)));


            //then
            verify(orderService, times(1)).updateOrder(any(), any(), any(OrderUpdateDto.class));
            actions.andExpect(status().is(404));

        }

        @Test
        @WithMockUser(username = "spring", authorities = {ERole.USER})
        void UpdateOrder_Returns400_Approved_USER() throws Exception {
            //given
            var result = OrderTestFactory.createOrderDto(EDetail.MORE);
            var body = new OrderUpdateDto(EOrderStatus.APPROVED);

            var sBuilder = new StringBuilder("/order/");
            sBuilder.append(result.getId());
            Response<OrderDto> response = Response.notOk("Message", EErrorCode.BAD_REQUEST);

            //when
            when(messageSource.getMessage(any(), any(), any()))
                    .thenReturn("Message");
            when(orderService.updateOrder(any(), any(), any(OrderUpdateDto.class))).thenReturn(response);

            //when

            ResultActions actions = mockMvc.perform(patch(sBuilder.toString())
                    .contentType(CONTENT_TYPE)
                    .content(objectMapper.writeValueAsString(body)));


            //then
            verify(orderService, times(1)).updateOrder(any(), any(), any(OrderUpdateDto.class));
            actions.andExpect(status().is(400));

        }

        @Test
        @WithMockUser(username = "spring", authorities = {ERole.USER})
        void UpdateOrder_Returns400_Delivered_USER() throws Exception {
            //given
            var result = OrderTestFactory.createOrderDto(EDetail.MORE);
            var body = new OrderUpdateDto(EOrderStatus.DELIVERED);

            var sBuilder = new StringBuilder("/order/");
            sBuilder.append(result.getId());
            Response<OrderDto> response = Response.notOk("Message", EErrorCode.BAD_REQUEST);

            //when
            when(messageSource.getMessage(any(), any(), any()))
                    .thenReturn("Message");
            when(orderService.updateOrder(any(), any(), any(OrderUpdateDto.class))).thenReturn(response);

            //when

            ResultActions actions = mockMvc.perform(patch(sBuilder.toString())
                    .contentType(CONTENT_TYPE)
                    .content(objectMapper.writeValueAsString(body)));


            //then
            verify(orderService, times(1)).updateOrder(any(), any(), any(OrderUpdateDto.class));
            actions.andExpect(status().is(400));

        }

        @Test
        @WithMockUser(username = "spring")
        void UpdateOrder_Returns400_Pending() throws Exception {
            //given
            var result = OrderTestFactory.createOrderDto(EDetail.MORE);
            var body = new OrderUpdateDto(EOrderStatus.PENDING);

            var sBuilder = new StringBuilder("/order/");
            sBuilder.append(result.getId());
            Response<OrderDto> response = Response.notOk("Message", EErrorCode.BAD_REQUEST);

            //when
            when(messageSource.getMessage(any(), any(), any()))
                    .thenReturn("Message");
            when(orderService.updateOrder(any(), any(), any(OrderUpdateDto.class))).thenReturn(response);

            //when

            ResultActions actions = mockMvc.perform(patch(sBuilder.toString())
                    .contentType(CONTENT_TYPE)
                    .content(objectMapper.writeValueAsString(body)));


            //then
            verify(orderService, times(1)).updateOrder(any(), any(), any(OrderUpdateDto.class));
            actions.andExpect(status().is(400));

        }

        @Test
        @WithMockUser(username = "spring", authorities = {ERole.USER})
        void UpdateOrder_Returns200() throws Exception {
            //given
            var result = OrderTestFactory.createOrderDto(EDetail.MORE);
            var body = new OrderUpdateDto(EOrderStatus.CANCELLED);

            var sBuilder = new StringBuilder("/order/");
            sBuilder.append(result.getId());
            Response<OrderDto> response = Response.ok(result);

            //when
            when(messageSource.getMessage(any(), any(), any()))
                    .thenReturn("Message");
            when(orderService.updateOrder(any(), any(), any(OrderUpdateDto.class))).thenReturn(response);

            //when

            ResultActions actions = mockMvc.perform(patch(sBuilder.toString())
                    .contentType(CONTENT_TYPE)
                    .content(objectMapper.writeValueAsString(body)));


            //then
            verify(orderService, times(1)).updateOrder(any(), any(), any(OrderUpdateDto.class));
            actions.andExpect(jsonPath("$.data.status").value(body.getStatus().toString()))
                    .andExpect(status().is(200));

        }

        @Test
        void UpdateOrder_Returns401() throws Exception {
            //given
            var result = OrderTestFactory.createOrderDto(EDetail.MORE);
            var body = new OrderUpdateDto(EOrderStatus.APPROVED);

            var sBuilder = new StringBuilder("/order/");
            sBuilder.append(result.getId());

            //when
            when(messageSource.getMessage(any(), any(), any()))
                    .thenReturn("Message");
            //when

            ResultActions actions = mockMvc.perform(patch(sBuilder.toString())
                    .contentType(CONTENT_TYPE)
                    .content(objectMapper.writeValueAsString(body)));


            //then
            verify(orderService, times(0)).updateOrder(any(), any(), any(OrderUpdateDto.class));
            actions.andExpect(status().is(401));

        }

        @Test
        @WithMockUser(authorities = {ERole.SYSADMIN})
        void UpdateOrder_Returns500() throws Exception {
            //given

            var sBuilder = new StringBuilder("/order/undefined");
            //when
            when(messageSource.getMessage(any(), any(), any()))
                    .thenReturn("Message");

            ResultActions actions = mockMvc.perform(
                            patch(sBuilder.toString()))
                    .andDo(print());


            //then
            verify(orderService, times(0)).updateOrder(any(), any(), any(OrderUpdateDto.class));
            actions.andExpect(status().isInternalServerError());

        }

        @Test
        @WithMockUser(authorities = {ERole.USER})
        void CreateOrder_Returns400_Null() throws Exception {

            //given
            var result = OrderTestFactory.createOrderDto(EDetail.MORE);
            var body = new OrderUpdateDto(null);

            var sBuilder = new StringBuilder("/order/");
            sBuilder.append(result.getId());
            //when
            ResultActions actions = mockMvc.perform(
                            patch(sBuilder.toString())
                                    .contentType(CONTENT_TYPE)
                                    .content(objectMapper.writeValueAsString(body)))
                    .andDo(print());


            //then
            verify(orderService, times(0)).updateOrder(any(), any(), any(OrderUpdateDto.class));

            actions.andExpect(mvcResult ->
                    assertThat(mvcResult.getResponse().getContentAsString()).contains("validation.generic.entity.not_null"));
            actions.andExpect(status().is(400));

        }
    }

}
