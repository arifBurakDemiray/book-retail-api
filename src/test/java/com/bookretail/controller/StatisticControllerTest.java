package com.bookretail.controller;

import com.bookretail.dto.Response;
import com.bookretail.dto.order.StatisticDto;
import com.bookretail.service.StatisticService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.MessageSource;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class StatisticControllerTest {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @MockBean
    private StatisticService statisticService;

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
    class GetMonthlyStatistics_Endpoint_Test_Cases {

        @Test
        @WithMockUser("spring")
        void GetMonthlyStatistics_Returns200() throws Exception {
            //given
            List<StatisticDto> result = new ArrayList<>();

            //when
            when(statisticService.getMonthlyStatistics(any())).thenReturn(Response.ok(result));
            ResultActions actions = mockMvc.perform(
                    get("/statistic")).andDo(print());

            //then
            actions.andExpect(status().isOk());
            verify(statisticService).getMonthlyStatistics(any());
        }

        @Test
        void GetMonthlyStatistics_Returns401() throws Exception {
            //given
            //when
            when(messageSource.getMessage(any(), any(), any()))
                    .thenReturn("Message");
            ResultActions actions = mockMvc.perform(
                    get("/statistic")).andDo(print());

            //then
            actions.andExpect(status().is(401));
        }

        @Test
        @WithMockUser("spring")
        void GetMonthlyStatistics_Returns200_ContentType() throws Exception {
            //given
            List<StatisticDto> result = new ArrayList<>();

            //when
            when(statisticService.getMonthlyStatistics(any())).thenReturn(Response.ok(result));
            ResultActions actions = mockMvc.perform(
                    get("/statistic").contentType(MediaType.APPLICATION_FORM_URLENCODED)).andDo(print());

            //then
            actions.andExpect(status().isOk());
            verify(statisticService).getMonthlyStatistics(any());
        }


    }
}
