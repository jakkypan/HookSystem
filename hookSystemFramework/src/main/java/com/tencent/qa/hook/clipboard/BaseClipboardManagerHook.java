package com.tencent.qa.hook.clipboard;

import android.content.Context;

import com.tencent.qa.hook.base.BaseHookSystemService;

/**
 * Created by richarddu on 2018/6/21.
 */

// https://android.googlesource.com/platform/frameworks/base/+/0e2d281/core/java/android/content/IClipboard.aidl
// https://github.com/amitv87/remote_access/blob/master/android/app/src/main/java/android/content/IClipboard.java
public abstract class BaseClipboardManagerHook extends BaseHookSystemService {

    public void hookClipboardManager() {
        try {
            hookSystemService(Context.CLIPBOARD_SERVICE, "android.content.IClipboard$Stub",
                    "android.content.IClipboard");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
