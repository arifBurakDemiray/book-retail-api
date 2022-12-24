package com.bookretail.service;

import com.bookretail.config.security.JwtUtil;
import com.bookretail.dto.Response;
import com.bookretail.dto.order.StatisticDto;
import com.bookretail.enums.EOrderStatus;
import com.bookretail.model.Order;
import com.bookretail.model.Order_;
import com.bookretail.model.User_;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

@Service
@AllArgsConstructor
public class StatisticService {
    @PersistenceContext
    private final EntityManager entityManager;

    private final JwtUtil jwtUtil;

    @Transactional(readOnly = true)
    public Response<List<StatisticDto>> getMonthlyStatistics(String token) {

        var userId = jwtUtil.getUserId(token);

        CriteriaBuilder queryBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<StatisticDto> criteriaQuery = queryBuilder.createQuery(StatisticDto.class);
        Root<Order> root = criteriaQuery.from(Order.class);

        var monthPath = queryBuilder.function("MONTHNAME", String.class, root.get(Order_.updatedAt));


        criteriaQuery.where(queryBuilder.equal(root.get(Order_.user).get(User_.id), userId));

        criteriaQuery.multiselect(
                monthPath,
                queryBuilder.sum(root.get(Order_.id)),
                queryBuilder.sum(queryBuilder.<Number>selectCase().when(
                        root.get(Order_.status).in(EOrderStatus.DELIVERED), root.get(Order_.quantity)).otherwise(0L)),
                queryBuilder.sum(queryBuilder.<Number>selectCase().when(
                        root.get(Order_.status).in(EOrderStatus.DELIVERED), root.get(Order_.cost)).otherwise(0.0)),
                queryBuilder.sum(queryBuilder.<Number>selectCase().when(
                        root.get(Order_.status).in(EOrderStatus.DELIVERED), 1L).otherwise(0L))
        );


        criteriaQuery.groupBy(monthPath);

        return Response.ok(entityManager.createQuery(criteriaQuery).getResultList());
    }

}
