package com.spring;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @ClassName ComponentScan
 * @Author dhg
 * @Version 1.0
 * @Date 2024/4/26 10:24
 * @Description:
 *   扫描路径
 *   只能在类上面
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ComponentScan {

    // 指定扫描路径
    String value();
}
