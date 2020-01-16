package com.tencent.qa.hook.connectivity;

import android.content.Context;

import com.tencent.qa.hook.base.BaseHookSystemService;

/**
 * Created by richarddu on 2018/6/21.
 */

// https://android.googlesource.com/platform/frameworks/base/+/master/core/java/android/net/IConnectivityManager.aidl
// https://github.com/klinker41/android-smsmms/blob/master/library/src/main/java/android/net/IConnectivityManager.java
public abstract class BaseConnectivityManagerHook extends BaseHookSystemService {

    public void hookConnectivityManager() {
        try {
            hookSystemService(Context.CONNECTIVITY_SERVICE, "android.net.IConnectivityManager$Stub",
                    "android.net.IConnectivityManager");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
