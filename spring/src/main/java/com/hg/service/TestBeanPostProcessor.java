package com.hg.service;

import com.spring.BeanPostProcessor;
import com.spring.Component;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @ClassName TestBeanPostProcessor
 * @Author dhg
 * @Version 1.0
 * @Date 2024/5/7 18:25
 * @Description:
 *   全局性质的，所以根据自己的需求来使用
 */
@Component
public class TestBeanPostProcessor implements BeanPostProcessor {
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) {
        // 做自己想做的事情
        System.out.println("初始化前");
        if(beanName.equals("userService")){
            ((UserServiceImpl) bean).setBeanName("hg好帅");
        }

        return bean;
    }


    /**
     * 下面的逻辑，是模仿aop的执行逻辑
     * 实际上aop就是继承的BeanPostProcessor，而且是在初始化之后执行
     * @param bean
     * @param beanName
     * @return
     */
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) {
        System.out.println("初始化后");

        //这里可以判断对bean要不要进行aop，无非就是判断userService有没有对应的切点
        if (beanName.equals("userService")){
            //  生成代理对象
            Object proxyInstance = Proxy.newProxyInstance(TestBeanPostProcessor.class.getClassLoader(), bean.getClass().getInterfaces(), new InvocationHandler() {
                @Override
                public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                    System.out.println("代理逻辑"); // 找切点，也就是执行切点对应的那些方法，然后去执行
                    return method.invoke(bean,args);
                }
            });

            return proxyInstance;
        }
        return bean;
    }
}
