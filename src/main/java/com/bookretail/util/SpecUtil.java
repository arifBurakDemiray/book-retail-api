package com.bookretail.util;

import org.springframework.data.jpa.domain.Specification;

public class SpecUtil {
    
    private SpecUtil() {
    }

    public static <T> Specification<T> and(Specification<T> first, Specification<T> second) {
        if (first == null) {
            return second;
        } else {
            return first.and(second);
        }
    }

    public static <T> Specification<T> or(Specification<T> first, Specification<T> second) {
        if (first == null) {
            return second;
        } else {
            return first.or(second);
        }
    }
}
