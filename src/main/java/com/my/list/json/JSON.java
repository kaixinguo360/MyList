package com.my.list.json;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(JSONS.class)
public @interface JSON {
    Class<?> type() default void.class;
    String include() default "";
    String exclude() default "";
}
