package com.nifeng.core.anotation;

import com.nifeng.core.CommonUtil;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by fengni on 2015/9/24.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public  @interface Entity {
    public String value() default CommonUtil.EMPTY_STRING;
}
