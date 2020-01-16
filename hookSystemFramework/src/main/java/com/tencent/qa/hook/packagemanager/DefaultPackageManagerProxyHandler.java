package com.tencent.qa.hook.packagemanager;

import android.content.Context;
import android.util.Log;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * Created by richarddu on 2018/6/21.
 */
// method list
// https://android.googlesource.com/platform/frameworks/base/+/483f3b06ea84440a082e21b68ec2c2e54046f5a6/core/java/android/content/pm/IPackageManager.aidl
public class DefaultPackageManagerProxyHandler implements InvocationHandler {

    private static final String TAG = DefaultPackageManagerProxyHandler.class.getSimpleName();

    private Object mPm;
    private Context mBaseContext;

    public DefaultPackageManagerProxyHandler(Object pm, Context context) {
        this.mPm = pm;
        this.mBaseContext = context;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        Log.d(TAG, "args is " + args + ",method is " + method.getName());

        return method.invoke(mPm, args);
    }
}