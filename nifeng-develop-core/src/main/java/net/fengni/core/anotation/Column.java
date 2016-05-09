package net.fengni.core.anotation;

import net.fengni.core.CommonUtil;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by fengni on 2015/9/24.
 */

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Column {
    public String name() default CommonUtil.EMPTY_STRING;
}

