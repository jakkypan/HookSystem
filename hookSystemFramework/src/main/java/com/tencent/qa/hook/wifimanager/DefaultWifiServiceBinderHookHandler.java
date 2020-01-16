package com.tencent.qa.hook.wifimanager;

import android.os.IBinder;
import android.util.Log;

import com.tencent.qa.hook.base.BaseBinderHookHandler;

import java.lang.reflect.Method;

/**
 * Created by richarddu on 2018/6/21.
 */

// method list:
// https://android.googlesource.com/platform/frameworks/base/+/refs/heads/master/wifi/java/android/net/wifi/IWifiManager.aidl
public class DefaultWifiServiceBinderHookHandler extends BaseBinderHookHandler {

    private static final String TAG = DefaultWifiServiceBinderHookHandler.class.getSimpleName();

    public DefaultWifiServiceBinderHookHandler(IBinder base, Class<?> stubClass) {
        super(base, stubClass);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        Log.d(TAG, "args is " + args + ",method is " + method.getName());

        return method.invoke(base, args);
    }

}
