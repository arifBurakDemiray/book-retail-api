package com.bookretail.factory;

import com.bookretail.dto.order.StatisticDto;

public class StatisticTestFactory {

    private StatisticTestFactory() {
    }

    public static StatisticDto createStatisticDto() {
        return new StatisticDto("January", 1L, 1.0, 1L, 1L);
    }
}
