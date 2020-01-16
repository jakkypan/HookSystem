package com.tencent.qa.hook.base;

import android.os.IBinder;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * Created by richarddu on 2018/6/21.
 */

public abstract class BaseBinderHookHandler implements InvocationHandler {

    // 原始的Service对象 (IInterface)
    protected Object base;

    public BaseBinderHookHandler(IBinder base, Class<?> stubClass) {
        try {
            Method asInterfaceMethod = stubClass.getDeclaredMethod("asInterface", IBinder.class);
            // IClipboard.Stub.asInterface(base);
            this.base = asInterfaceMethod.invoke(null, base);
        } catch (Exception e) {
            throw new RuntimeException("hooked failed!");
        }
    }
}
