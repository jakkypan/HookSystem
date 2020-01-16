package com.tencent.qa.hook.activitymanager;

import android.util.Log;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * Created by richarddu on 2018/6/21.
 */

// method list
// https://android.googlesource.com/platform/frameworks/base/+/c80f952/core/java/android/app/IActivityManager.java
public class DefaultActivityManagerProxyHandler implements InvocationHandler {

    private static final String TAG = DefaultActivityManagerProxyHandler.class.getSimpleName();

    protected Object mInstance;

    public DefaultActivityManagerProxyHandler(Object instance) {
        this.mInstance = instance;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        Log.d(TAG, "args is " + args + ",method is " + method.getName());

        return method.invoke(mInstance, args);
    }
}
