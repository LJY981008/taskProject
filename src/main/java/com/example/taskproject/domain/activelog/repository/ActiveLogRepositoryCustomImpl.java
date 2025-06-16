package com.example.taskproject.domain.activelog.repository;

import com.example.taskproject.common.dto.LogRequestDto;
import com.example.taskproject.common.entity.ActiveLog;
import com.example.taskproject.common.entity.QActiveLog;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.ComparableExpressionBase;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

import static com.example.taskproject.common.entity.QActiveLog.activeLog;
@Repository
@RequiredArgsConstructor
public class ActiveLogRepositoryCustomImpl implements ActiveLogRepositoryCustom{
    private final JPAQueryFactory queryFactory;

    @Override
    public Page<ActiveLog> findActiveLogsDynamic(LogRequestDto logRequest, Pageable pageable){
        BooleanBuilder builder = new BooleanBuilder();

        if(logRequest.getUserId() != null) builder.and(activeLog.userId.eq(logRequest.getUserId()));
        if(logRequest.getActivityType() != null && !logRequest.getActivityType().isEmpty()) builder.and(activeLog.activityType.eq(logRequest.getActivityType()));
        if(logRequest.getTargetId() != null) builder.and(activeLog.targetId.eq(logRequest.getTargetId()));
        if(logRequest.getStartDate() != null) builder.and(activeLog.createTime.goe(logRequest.getStartDate()));
        if(logRequest.getEndDate() != null) builder.and(activeLog.createTime.loe(logRequest.getEndDate()));

        List<ActiveLog> content = queryFactory
                .selectFrom(activeLog)
                .where(builder)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(convert(pageable.getSort()))
                .fetch();

        return PageableExecutionUtils.getPage(content, pageable, () -> queryFactory.selectFrom(activeLog).where(builder).fetchCount());
    }

    private OrderSpecifier<?>[] convert(Sort sort){
        return sort.stream()
                .map(order -> {
                    if(order.getProperty().equals("createTime")) return order.isAscending() ? activeLog.createTime.asc() : activeLog.createTime.desc();
                    else if(order.getProperty().equals("activityType")) return order.isAscending() ? activeLog.activityType.asc() : activeLog.activityType.desc();
                    return activeLog.createTime.desc();
                })
                .toArray(OrderSpecifier[]::new);
    }
}
