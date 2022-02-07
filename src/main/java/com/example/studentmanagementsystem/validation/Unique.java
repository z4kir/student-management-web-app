package com.example.studentmanagementsystem.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = UniqueConstraintValidator.class)
@Target({ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Unique {

    String message() default "is already given";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String field();
}
