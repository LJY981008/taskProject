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

        Long userId = null;
        ActivityType activityType = logging.value();
        Long targetId = null;

        try{
            if(result != null){
                if(result instanceof UserResponse userResponse){
                    userId = userResponse.getId();
                    targetId = userResponse.getId();
                }
                else if(result instanceof TaskResponseDto taskResponse){
                    userId = taskResponse.getAssigneeId();
                    targetId = taskResponse.getId();
                }
                else if(result instanceof CommentResponseDto commentResponse){
                    userId = commentResponse.getUserId();
                    targetId = commentResponse.getId();
                }
                else if(result instanceof AuthUserDto authUser){
                    userId = authUser.getId();
                    targetId = authUser.getId();
                }
            }
            if(userId != null && targetId != null) activeLogService.logActivity(userId, activityType.name(), targetId);
            else log.warn("로그 기록 실패");
        } catch(Exception e){
            log.error("로그 기록 실패", e);
        }

        return result;
    }
}
