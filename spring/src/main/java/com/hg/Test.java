package com.hg;

import com.spring.HGApplicationContext;

/**
 * @ClassName Test
 * @Author dhg
 * @Version 1.0
 * @Date 2024/4/26 10:22
 * @Description:
 */
public class Test {
    public static void main(String[] args) {
        HGApplicationContext applicationContext = new HGApplicationContext(AppConfig.class);

        // map <beanName,bean对象>
        Object userService = applicationContext.getBean("userService");
        System.out.println(userService);

        System.out.println(applicationContext.getBean("userService"));
        System.out.println(applicationContext.getBean("userService"));
        System.out.println(applicationContext.getBean("userService"));
    }
}
