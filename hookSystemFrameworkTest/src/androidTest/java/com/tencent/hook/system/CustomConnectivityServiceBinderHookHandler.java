package com.tencent.hook.system;

import android.os.IBinder;
import android.util.Log;

import com.tencent.qa.hook.base.BaseBinderHookHandler;

import java.lang.reflect.Method;

/**
 * Created by richarddu on 2018/6/21.
 */

// method list:
// https://github.com/klinker41/android-smsmms/blob/master/library/src/main/java/android/net/IConnectivityManager.java
public class CustomConnectivityServiceBinderHookHandler extends BaseBinderHookHandler {

    private static final String TAG = "ConnectivityServiceHook";

    public CustomConnectivityServiceBinderHookHandler(IBinder base, Class<?> stubClass) {
        super(base, stubClass);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        Log.d(TAG, "args length is " + args.length + ",method is " + method.getName());

        if ("prepareVpn".equals(method.getName())) {
            return false;
        }

        return method.invoke(base, args);
    }
}
