package com.example.taskproject.domain.user.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.ReportAsSingleViolation;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(
        validatedBy = {}
)
@ReportAsSingleViolation
@Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^A-Za-z0-9]).+$")
@Size(min = 8)
public @interface ValidPassword {
    String message() default "비밀번호는 8자 이상, 영문/숫자/특수문자를 포함해야 합니다.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
