package com.tencent.qa.hook.Test;

public class Apple implements Fruit {
    @Override
    public String getName() {
        System.out.println("I am apple!");
        return "Apple";
    }
}