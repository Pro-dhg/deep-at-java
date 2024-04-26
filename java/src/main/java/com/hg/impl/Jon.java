package com.hg.impl;

import com.hg.Person;

/**
 * @ClassName Jon
 * @Author dhg
 * @Version 1.0
 * @Date 2024/4/26 10:00
 * @Description:
 */
public class Jon implements Person {
    @Override
    public void eat() {
        System.out.println("Jon can eat something");
    }

    @Override
    public void sleep() {
        System.out.println("Jon can sleep");
    }
}
