package com.example.taskproject.common.aop;

import com.example.taskproject.common.annotation.Logging;
import com.example.taskproject.common.dto.AuthUserDto;
import com.example.taskproject.common.dto.TaskResponseDto;
import com.example.taskproject.common.enums.ActivityType;
import com.example.taskproject.domain.activelog.service.ActiveLogService;
import com.example.taskproject.domain.comment.dto.CommentResponseDto;
import com.example.taskproject.domain.user.dto.UserResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class ActivityLogAspect {
    private final ActiveLogService activeLogService;

    @Around("@annotation(logging)")
    public Object logActivity(ProceedingJoinPoint joinPoint, Logging logging) throws Throwable{
        Object result = joinPoint.proceed();

        try{
            Long userId = extractUserId(result, joinPoint);
            Long targetId = extractTargetId(result, joinPoint, userId);
            ActivityType activityType = logging.value();

            if(userId != null && targetId != null) activeLogService.logActivity(userId, activityType.name(), targetId);
            else log.warn("로그 기록 실패");
        } catch(Exception e){
            log.error("로그 기록 실패", e);
        }

        return result;
    }

    private Long extractUserId(Object result, ProceedingJoinPoint joinPoint){
        if(result instanceof UserResponse userResponse)
            return userResponse.getId();
        else if(result instanceof TaskResponseDto taskResponse)
            return taskResponse.getAssigneeId();
        else if(result instanceof CommentResponseDto commentResponse)
            return commentResponse.getUserId();
        else{
            for(Object arg : joinPoint.getArgs()){
                if(arg instanceof AuthUserDto authUser)
                    return authUser.getId();
            }
        }

        return null;
    }

    private Long extractTargetId(Object result, ProceedingJoinPoint joinPoint, Long userId){
        if(result instanceof UserResponse userResponse)
            return userResponse.getId();
        else if(result instanceof TaskResponseDto taskResponse)
            return taskResponse.getId();
        else if(result instanceof CommentResponseDto commentResponse)
            return commentResponse.getId();
        else{
            String methodName = joinPoint.getSignature().getName();
            Long param = null;

            for(Object arg : joinPoint.getArgs()){
                if(arg instanceof Long l && param == null) param = l;
            }

            if(methodName.contains("deleteComment") || methodName.contains("deleteTask"))
                return param;
            else if(methodName.contains("withdraw"))
                return userId;
        }

        return null;
    }
}
