package com.tencent.qa.hook.Test;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class FruitInvocationHandler implements InvocationHandler {
    Object target;

    public FruitInvocationHandler(Object target) {
        this.target = target;
    }

    @Override
    public Object invoke(Object o, Method apple, Object[] args) throws Throwable {
        if("getName".equals(apple.getName())){
            System.out.println("Let Apple introduce himself.");
            // 返回方法的执行结果
            Object result = apple.invoke(target, args);
            System.out.println("Thank you!");
            return result;
        }
        return null;
    }
}
