package com.tencent.qa.hook.packagemanager;

import android.content.Context;
import android.util.Log;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;

/**
 * Created by richarddu on 2018/6/21.
 */

public class PackageManagerHook extends BasePackageManagerHook {

    private static final String TAG = PackageManagerHook.class.getSimpleName();

    private Class<? extends InvocationHandler> mCustomInvocationHandler;

    public PackageManagerHook() {
    }

    public PackageManagerHook(Class<? extends InvocationHandler> clazz) {
        this.mCustomInvocationHandler = clazz;
    }

    @Override
    public InvocationHandler getPackageManagerProxyHandler(Object pm, Context context) {

        Log.d(TAG, "getPackageManagerProxyHandler mCustomInvocationHandler is " + mCustomInvocationHandler);

        if (mCustomInvocationHandler != null) {
            Constructor[] ctors = mCustomInvocationHandler.getConstructors();
            for (Constructor constructor : ctors) {
                Class<?>[] clazz = constructor.getParameterTypes();
                if (clazz.length == 2) {
                    try {
                        return mCustomInvocationHandler.getConstructor(clazz).newInstance(new Object[]{pm, context});
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

        return new DefaultPackageManagerProxyHandler(pm, context);
    }
}
