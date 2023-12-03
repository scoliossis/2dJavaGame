package scoliosis.Options;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Inherited
@Retention(RetentionPolicy.RUNTIME)
public @interface Property {

    String parent() default "";

    String description() default "";

    String[] options() default {};

    String name();

    Type type();

    int max() default Integer.MAX_VALUE;

    int min() default 0;

    int step() default 1;


    enum Type {

        BOOLEAN,

        NUMBER,

        SELECT
    }
}
