package com.bookretail.specification;

import com.bookretail.model.Order;
import net.kaczmarzyk.spring.data.jpa.domain.Equal;
import net.kaczmarzyk.spring.data.jpa.domain.GreaterThanOrEqual;
import net.kaczmarzyk.spring.data.jpa.domain.LessThanOrEqual;
import net.kaczmarzyk.spring.data.jpa.web.annotation.And;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Spec;
import org.springframework.data.jpa.domain.Specification;

@And({
        @Spec(path = "status", params = "type", spec = Equal.class),
        @Spec(path = "updatedAt", params = "startTime", spec = GreaterThanOrEqual.class),
        @Spec(path = "updatedAt", params = "endTime", spec = LessThanOrEqual.class),
})
public interface OrderFilterSpec extends Specification<Order> {
}
