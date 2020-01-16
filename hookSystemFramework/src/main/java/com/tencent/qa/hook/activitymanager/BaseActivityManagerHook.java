package com.tencent.qa.hook.activitymanager;

import android.app.ActivityManager;
import android.os.Build;
import android.util.AndroidRuntimeException;
import android.util.Log;


import com.tencent.qa.hook.compat.ActivityManagerNativeCompat;
import com.tencent.qa.hook.compat.IActivityManagerCompat;
import com.tencent.qa.hook.compat.SingletonCompat;
import com.tencent.qa.hook.reflect.FieldUtils;
import com.tencent.qa.hook.reflect.Utils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.List;

/**
 * Created by richarddu on 2018/6/21.
 */

public abstract class BaseActivityManagerHook {

    private static final String TAG = BaseActivityManagerHook.class.getSimpleName();

    public void hooActivityManager() throws Exception {
        // https://blog.csdn.net/qi1017269990/article/details/78879512

        if (Build.VERSION.SDK_INT >= 26) {
            // https://android.googlesource.com/platform/frameworks/base/+/android-8.0.0_r27/core/java/android/app/ActivityManager.java
            Object singleton = FieldUtils.readStaticField(ActivityManager.class, "IActivityManagerSingleton");
            Object rawBinder = FieldUtils.readField(singleton, "mInstance");
            if (rawBinder == null) {
                SingletonCompat.get(singleton);
                rawBinder = FieldUtils.readField(singleton, "mInstance");
            }
            if (rawBinder != null) {
                Class<?> objClass = rawBinder.getClass();
                List<Class<?>> interfaces = Utils.getAllInterfaces(objClass);
                Class[] ifs = interfaces != null && interfaces.size() > 0 ? interfaces.toArray(new Class[interfaces.size()]) : new Class[0];
                Object proxiedActivityManager = Proxy.newProxyInstance(objClass.getClassLoader(), ifs, getActivityManagerProxyHandler(rawBinder));
                try {
                    FieldUtils.writeField(singleton, "mInstance", proxiedActivityManager);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        } else {
            // https://android.googlesource.com/platform/frameworks/base/+/android-7.1.1_r46/core/java/android/app/ActivityManagerNative.java
            Class cls = ActivityManagerNativeCompat.Class();
            Object rawBinder = FieldUtils.readStaticField(cls, "gDefault");
            if (rawBinder == null) {
                // gDefault的存储方式不同
                // https://android.googlesource.com/platform/frameworks/base/+/android-2.3.3_r1.1/core/java/android/app/ActivityManagerNative.java
                ActivityManagerNativeCompat.getDefault();
                rawBinder = FieldUtils.readStaticField(cls, "gDefault");
            }

            if (IActivityManagerCompat.isIActivityManager(rawBinder)) {

                Class<?> objClass = rawBinder.getClass();
                List<Class<?>> interfaces = Utils.getAllInterfaces(objClass);
                Class[] ifs = interfaces != null && interfaces.size() > 0 ? interfaces.toArray(new Class[interfaces.size()]) : new Class[0];
                Object proxiedActivityManager = Proxy.newProxyInstance(objClass.getClassLoader(), ifs, getActivityManagerProxyHandler(objClass));
                FieldUtils.writeStaticField(cls, "gDefault", proxiedActivityManager);
                Log.i(TAG, "Install ActivityManager Hook 1 old is" + rawBinder + " ,new is " + proxiedActivityManager);
            } else if (SingletonCompat.isSingleton(rawBinder)) {
                Object obj1 = FieldUtils.readField(rawBinder, "mInstance");
                if (obj1 == null) {
                    SingletonCompat.get(rawBinder);
                    obj1 = FieldUtils.readField(rawBinder, "mInstance");
                }
                List<Class<?>> interfaces = Utils.getAllInterfaces(obj1.getClass());
                Class[] ifs = interfaces != null && interfaces.size() > 0 ? interfaces.toArray(new Class[interfaces.size()]) : new Class[0];
                final Object object = Proxy.newProxyInstance(obj1.getClass().getClassLoader(), ifs, getActivityManagerProxyHandler(obj1));

                //这里先写一次，防止后面找不到Singleton类导致的挂钩子失败的问题。
                FieldUtils.writeField(rawBinder, "mInstance", object);

                //这里使用方式1，如果成功的话，会导致上面的写操作被覆盖。
                FieldUtils.writeStaticField(cls, "gDefault", new android.util.Singleton<Object>() {
                    @Override
                    protected Object create() {
                        return object;
                    }
                });

                Log.i(TAG, "Install ActivityManager Hook 2 old is " + obj1.toString() + ",new is " + object);
            } else {

                throw new AndroidRuntimeException("Can not install IActivityManagerNative hook");
            }
        }
    }

    /**
     * 获取ActivityManager动态代理实例对象
     *
     * @param instance
     * @return
     */
    public abstract InvocationHandler getActivityManagerProxyHandler(Object instance);
}
