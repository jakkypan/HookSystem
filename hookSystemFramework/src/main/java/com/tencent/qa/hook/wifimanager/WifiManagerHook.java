package com.tencent.qa.hook.wifimanager;

import android.net.wifi.WifiManager;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.util.Log;

import com.tencent.qa.hook.base.BaseBinderHookHandler;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * Created by richarddu on 2018/6/22.
 */

public class WifiManagerHook extends BaseWifiManagerHook {

    private static final String TAG = WifiManagerHook.class.getSimpleName();

    private Class mCustomInvocationHandler;

    public WifiManagerHook() {
    }

    public WifiManagerHook(@NonNull Class clazz) {
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

        return new DefaultWifiServiceBinderHookHandler(base, stubClass);
    }
}
