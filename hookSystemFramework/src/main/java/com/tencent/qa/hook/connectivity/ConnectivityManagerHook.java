package com.tencent.qa.hook.connectivity;

import android.os.IBinder;
import android.support.annotation.NonNull;
import android.util.Log;

import com.tencent.qa.hook.base.BaseBinderHookHandler;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * Created by richarddu on 2018/6/22.
 */

public class ConnectivityManagerHook extends BaseConnectivityManagerHook {

    private static final String TAG = ConnectivityManagerHook.class.getSimpleName();

    private Class mCustomInvocationHandler;

    public ConnectivityManagerHook() {
    }

    public ConnectivityManagerHook(@NonNull Class clazz) {
        this.mCustomInvocationHandler = clazz;
    }

    @NonNull
    @Override
    public BaseBinderHookHandler getBinderHookHandlerInstance(IBinder base, Class<?> stubClass) {

        Log.d(TAG, "getBinderHookHandlerInstance mCustomInvocationHandler is " + mCustomInvocationHandler);

        if (mCustomInvocationHandler != null) {
            Constructor[] ctors = mCustomInvocationHandler.getConstructors();
            for (Constructor constructor : ctors) {
                Class<?>[] clazz = constructor.getParameterTypes();
                if (clazz.length == 2) {
                    try {
                        return (BaseBinderHookHandler) mCustomInvocationHandler.getConstructor(clazz)
                                .newInstance(new Object[]{base, stubClass});
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

        return new DefaultConnectivityServiceBinderHookHandler(base, stubClass);
    }
}
