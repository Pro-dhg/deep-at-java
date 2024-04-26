package com.spring;

/**
 * @ClassName BeanDefinition
 * @Author dhg
 * @Version 1.0
 * @Date 2024/4/26 14:52
 * @Description:
 */
public class BeanDefinition {


    // 当前bean的类型
    private Class clazz ;

    //作用域
    private String scope ;

    public Class getClazz() {
        return clazz;
    }

    public void setClazz(Class clazz) {
        this.clazz = clazz;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }
}
