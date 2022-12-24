package com.bookretail.controller;

import com.bookretail.dto.Response;
import com.bookretail.dto.order.StatisticDto;
import com.bookretail.service.StatisticService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
@Tag(name = StatisticController.tag, description = StatisticController.description)
@RequestMapping(StatisticController.tag)
@PreAuthorize("isAuthenticated()")
public class StatisticController {

    public static final String description = "Statistics related endpoints.";
    public static final String tag = "statistic";

    private final StatisticService statisticService;

    @GetMapping
    public ResponseEntity<Response<List<StatisticDto>>> getMonthlyStatistics(
            @Parameter(hidden = true) @RequestHeader(name = HttpHeaders.AUTHORIZATION, required = false) String token) {
        return statisticService.getMonthlyStatistics(token).toResponseEntity();

    }
}
