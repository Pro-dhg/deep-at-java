package com.spring;

/**
 * @ClassName InitializingBean
 * @Author dhg
 * @Version 1.0
 * @Date 2024/5/7 18:08
 * @Description:
 *  初始化
 */
public interface InitializingBean {
    void afterPropertiesSett() throws Exception;
}
