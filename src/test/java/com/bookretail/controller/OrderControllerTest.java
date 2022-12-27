package com.bookretail.controller;

import com.bookretail.dto.Response;
import com.bookretail.dto.order.OrderDto;
import com.bookretail.enums.ERole;
import com.bookretail.factory.OrderTestFactory;
import com.bookretail.factory.UserTestFactory;
import com.bookretail.service.OrderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
            var user = UserTestFactory.createSuccessTestUser_SYSADMIN();
            var order = OrderTestFactory.createOrderDto();

            Page<OrderDto> page = new PageImpl<>(Collections.singletonList(order));

            //when
            when(orderService.getAll(any(), any(), any())).thenReturn(Response.ok(page));
            ResultActions actions = mockMvc.perform(
                    get("/order")).andDo(print());


            //then
            actions.andExpect(status().isOk());
            actions.andExpect(jsonPath("$.data.content[0].id").value(order.getId()));
            verify(orderService).getAll(any(), any(), any());

        }

        @Test
        @WithMockUser(authorities = {ERole.USER})
        void GetAllOrders_Returns200_Empty_USER() throws Exception {

            //given
            var order = OrderTestFactory.createOrderDto();

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
        void GetAllOrders_Returns403() throws Exception {
            //given
            //when
            ResultActions actions = mockMvc.perform(
                    get("/order")).andDo(print());

            //then
            actions.andExpect(status().is(403));
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


}
