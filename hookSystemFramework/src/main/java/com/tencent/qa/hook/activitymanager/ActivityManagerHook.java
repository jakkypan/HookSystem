package com.tencent.qa.hook.activitymanager;

import android.util.Log;

import com.tencent.qa.hook.base.BaseBinderHookHandler;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;

/**
 * Created by richarddu on 2018/6/21.
 */

public class ActivityManagerHook extends BaseActivityManagerHook {

    private static final String TAG = ActivityManagerHook.class.getSimpleName();

    private Class<? extends InvocationHandler> mCustomInvocationHandler;

    public ActivityManagerHook() {
    }

    public ActivityManagerHook(Class<? extends InvocationHandler> clazz) {
        this.mCustomInvocationHandler = clazz;
    }

    @Override
    public InvocationHandler getActivityManagerProxyHandler(Object instance) {

        Log.d(TAG, "getActivityManagerProxyHandler mCustomInvocationHandler is " + mCustomInvocationHandler);

        if (mCustomInvocationHandler != null) {
            Constructor[] ctors = mCustomInvocationHandler.getConstructors();
            for (Constructor constructor : ctors) {
                Class<?>[] clazz = constructor.getParameterTypes();
                if (clazz.length == 1) {
                    try {
                        return mCustomInvocationHandler.getConstructor(clazz).newInstance(new Object[]{instance});
                    } catch (InstantiationException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        return new DefaultActivityManagerProxyHandler(instance);
    }
}
