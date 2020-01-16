package com.tencent.qa.hook.wifimanager;

import android.content.Context;

import com.tencent.qa.hook.base.BaseHookSystemService;

/**
 * Created by richarddu on 2018/6/21.
 */

// https://android.googlesource.com/platform/frameworks/base/+/refs/heads/master/wifi/java/android/net/wifi/IWifiManager.aidl
// https://github.com/Orange-OpenSource/matos-profiles/blob/master/matos-android/src/main/java/android/net/wifi/IWifiManager.java
public abstract class BaseWifiManagerHook extends BaseHookSystemService {

    public void hookWifiManager() {
        try {
            hookSystemService(Context.WIFI_SERVICE, "android.net.wifi.IWifiManager$Stub",
                    "android.net.wifi.IWifiManager");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
