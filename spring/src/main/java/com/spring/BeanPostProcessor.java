package com.spring;

/**
 * @ClassName BeanPostProcessor
 * @Author dhg
 * @Version 1.0
 * @Date 2024/5/7 18:15
 * @Description: spring对外提供的扩展机制
 */
public interface BeanPostProcessor {

    Object postProcessBeforeInitialization(Object bean, String beanName);

    Object postProcessAfterInitialization(Object bean, String beanName);
}
