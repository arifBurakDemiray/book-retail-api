package com.bookretail.service;

import com.bookretail.config.security.JwtUtil;
import com.bookretail.factory.StatisticTestFactory;
import com.bookretail.factory.UserTestFactory;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.AuthenticationException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ContextConfiguration(classes = {StatisticService.class})
@ExtendWith(SpringExtension.class)
public class StatisticServiceTest {

    @Autowired
    private StatisticService statisticService;

    @MockBean
    private JwtUtil jwtUtil;

    @MockBean
    private MonthlyStatisticService monthlyStatisticService;

    @Nested
    class GetMonthlyStatistics_Method_Test_Cases {

        @Test
        void GetMonthlyStatistics_Success() throws AuthenticationException {
            //given
            var user = UserTestFactory.createSuccessTestUser_USER();
            var result = Collections.singletonList(StatisticTestFactory.createStatisticDto());
            var token = UserTestFactory.token_USER;

            when(jwtUtil.getUserId(anyString())).thenReturn(user.getId());
            when(monthlyStatisticService.getMonthlyStatistics(anyLong())).thenReturn(result);

            //when
            var actualResult = statisticService.getMonthlyStatistics(token);
            var data = actualResult.getData();

            //then
            assertEquals(1, data.size());
            assertEquals(data.get(0).getMonth(), result.get(0).getMonth());
            assertTrue(actualResult.isOk());

        }
    }
}
