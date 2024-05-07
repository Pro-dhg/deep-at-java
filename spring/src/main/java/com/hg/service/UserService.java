package com.hg.service;

import com.spring.*;

/**
 * @ClassName UserService
 * @Author dhg
 * @Version 1.0
 * @Date 2024/4/26 10:27
 * @Description:
 *  scope:
 *     - prototype :原型，那么获取的bean每一个都是新的
 *     - singleton :单例，那么获取的bean都是同一个
 */

@Component("userService")
@Scope("prototype")
public class UserService implements BeanNameAware, InitializingBean {

    @Autowired
    private OrderService orderService ;

    private String beanName ;

    public void test(){
        System.out.println(orderService);
        System.out.println(beanName);
    }

    @Override
    public void setBeanName(String name) {
        this.beanName = name ;
    }

    @Override
    public void afterPropertiesSett() throws Exception {
        // 做自己想做的事情
        // 赋值、或者操作都可以
        System.out.println("******");
    }
}
