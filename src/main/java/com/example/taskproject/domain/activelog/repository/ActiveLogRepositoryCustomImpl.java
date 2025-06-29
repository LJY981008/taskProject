package com.example.taskproject.domain.activelog.repository;

import com.example.taskproject.common.dto.LogRequestDto;
import com.example.taskproject.common.entity.ActiveLog;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.example.taskproject.common.entity.QActiveLog.activeLog;
import static org.springframework.util.StringUtils.hasText;

@Repository
@RequiredArgsConstructor
public class ActiveLogRepositoryCustomImpl implements ActiveLogRepositoryCustom{
    private final JPAQueryFactory queryFactory;

    @Override
    public Page<ActiveLog> findActiveLogsDynamic(LogRequestDto logRequest, Pageable pageable){
        BooleanBuilder builder = buildConditions(logRequest);

        List<ActiveLog> content = queryFactory
                .selectFrom(activeLog)
                .where(builder)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(convert(logRequest, pageable))
                .fetch();

        return PageableExecutionUtils.getPage(
                content,
                pageable,
                () -> queryFactory.selectFrom(activeLog).where(builder).fetchCount());
    }

    private OrderSpecifier<?>[] convert(LogRequestDto logRequest, Pageable pageable){
        if(pageable.getSort().isSorted()){
            return pageable.getSort().stream()
                    .map(this::mapOrder)
                    .toArray(OrderSpecifier[]::new);
        }

        if(logRequest.isSortByTime()) return new OrderSpecifier[]{activeLog.createTime.desc()};
        else return new OrderSpecifier[]{activeLog.activityType.desc()};
    }

    private OrderSpecifier<?> mapOrder(Sort.Order order){
        String property = order.getProperty();
        boolean isAscend = order.isAscending();

        if(property.equals("createTime"))
            return isAscend ? activeLog.createTime.asc() : activeLog.createTime.desc();
        else if(property.equals("activityType"))
            return isAscend ? activeLog.activityType.asc() : activeLog.activityType.desc();

        return activeLog.createTime.desc();
    }

    private BooleanBuilder buildConditions(LogRequestDto logRequest){
        BooleanBuilder builder = new BooleanBuilder();

        if(logRequest.getUserId() != null) builder.and(activeLog.userId.eq(logRequest.getUserId()));
        if(hasText(logRequest.getActivityType())) builder.and(activeLog.activityType.eq(logRequest.getActivityType()));
        if(logRequest.getTargetId() != null) builder.and(activeLog.targetId.eq(logRequest.getTargetId()));
        if(logRequest.getStartDate() != null) builder.and(activeLog.createTime.goe(logRequest.getStartDate().atStartOfDay()));
        if(logRequest.getEndDate() != null) builder.and(activeLog.createTime.loe(logRequest.getEndDate().atTime(23, 59, 59)));

        return builder;
    }
}
