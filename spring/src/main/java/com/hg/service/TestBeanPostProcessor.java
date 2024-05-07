package com.hg.service;

import com.spring.BeanPostProcessor;

/**
 * @ClassName TestBeanPostProcessor
 * @Author dhg
 * @Version 1.0
 * @Date 2024/5/7 18:25
 * @Description:
 *   全局性质的，所以根据自己的需求来使用
 */
public class TestBeanPostProcessor implements BeanPostProcessor {
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) {
        // 做自己想做的事情

        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) {
        return null;
    }
}
