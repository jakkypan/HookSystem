package com.tencent.qa.hook.Test;

import java.lang.reflect.Proxy;

public class Test {
    public static void main(String[] args) {
        Apple apple = new Apple();
        FruitInvocationHandler handler = new FruitInvocationHandler(apple);
        Fruit newApple = (Fruit) Proxy.newProxyInstance(
                handler.getClass().getClassLoader(),
                apple.getClass().getInterfaces(),
                handler);
        String name = newApple.getName();
        System.out.println(newApple.getClass().getName() + ",name = " + name);
    }
}
