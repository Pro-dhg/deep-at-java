package com.hg.service;

import com.spring.Autowired;
import com.spring.Component;
import com.spring.Scope;

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

@Component("orderService")
@Scope("prototype")
public class OrderService {

}
