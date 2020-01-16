package com.tencent.hook.system;

import android.net.wifi.WifiManager;
import android.os.IBinder;
import android.util.Log;

import com.tencent.qa.hook.base.BaseBinderHookHandler;

import java.lang.reflect.Method;

/**
 * Created by richarddu on 2018/6/21.
 */

// method list:
// https://android.googlesource.com/platform/frameworks/base/+/refs/heads/master/wifi/java/android/net/wifi/IWifiManager.aidl
public class CustomWifiServiceBinderHookHandler extends BaseBinderHookHandler {

    private static final String TAG = CustomWifiServiceBinderHookHandler.class.getSimpleName();

    public CustomWifiServiceBinderHookHandler(IBinder base, Class<?> stubClass) {
        super(base, stubClass);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        Log.d(TAG, "method name is " + method.getName());

        if ("getWifiEnabledState".equals(method.getName())) {
            return WifiManager.WIFI_STATE_DISABLED;
        }

        return method.invoke(base, args);
    }
}
