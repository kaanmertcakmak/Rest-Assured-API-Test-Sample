package com.example.book;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

/**
 * @since 7/5/2021
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {UniqueBookValidator.class})
public @interface UniqueBook {

    String message() default "Book data should be unique";

    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
