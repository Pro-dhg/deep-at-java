package com.hg.impl;

import com.hg.Person;

/**
 * @ClassName Aiden
 * @Author dhg
 * @Version 1.0
 * @Date 2024/4/26 10:01
 * @Description:
 */
public class Aiden implements Person {
    @Override
    public void eat() {
        System.out.println("Aiden can eat something");
    }

    @Override
    public void sleep() {
        System.out.println("Aiden can sleep");
    }
}
