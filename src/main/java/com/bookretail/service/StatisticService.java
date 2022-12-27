package com.bookretail.service;

import com.bookretail.config.security.JwtUtil;
import com.bookretail.dto.Response;
import com.bookretail.dto.order.StatisticDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class StatisticService {

    private final MonthlyStatisticService monthlyStatisticService;

    private final JwtUtil jwtUtil;

    @Transactional(readOnly = true)
    public Response<List<StatisticDto>> getMonthlyStatistics(String token) {

        var userId = jwtUtil.getUserId(token);
        var result = monthlyStatisticService.getMonthlyStatistics(userId);

        return Response.ok(result);
    }

}
