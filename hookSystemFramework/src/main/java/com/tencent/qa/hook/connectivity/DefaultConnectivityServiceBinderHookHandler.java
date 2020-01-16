package com.tencent.qa.hook.connectivity;

import android.os.IBinder;
import android.util.Log;

import com.tencent.qa.hook.base.BaseBinderHookHandler;

import java.lang.reflect.Method;

/**
 * Created by richarddu on 2018/6/21.
 */

// method list:
// https://github.com/klinker41/android-smsmms/blob/master/library/src/main/java/android/net/IConnectivityManager.java
public class DefaultConnectivityServiceBinderHookHandler extends BaseBinderHookHandler {

    private static final String TAG = "ConnectivityService";

    public DefaultConnectivityServiceBinderHookHandler(IBinder base, Class<?> stubClass) {
        super(base, stubClass);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        Log.d(TAG, "args is " + args + ",method is " + method.getName());

        return method.invoke(base, args);
    }
}
