package com.tencent.hook.system;

import android.content.ClipData;
import android.os.IBinder;
import android.util.Log;

import com.tencent.qa.hook.base.BaseBinderHookHandler;

import java.lang.reflect.Method;

/**
 * Created by richarddu on 2018/6/21.
 */

// method list:
// https://android.googlesource.com/platform/frameworks/base/+/0e2d281/core/java/android/content/IClipboard.aidl
public class CustomClipboardServiceBinderHookHandler extends BaseBinderHookHandler {

    private static final String TAG = CustomClipboardServiceBinderHookHandler.class.getSimpleName();

    public CustomClipboardServiceBinderHookHandler(IBinder base, Class<?> stubClass) {
        super(base, stubClass);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        // 把剪切版的内容替换为 "you are hooked"
        if ("getPrimaryClip".equals(method.getName())) {
            Log.d(TAG, "hook getPrimaryClip");
            return ClipData.newPlainText(null, "you are hooked");
            //method.invoke(base, args);
        }

        // 欺骗系统,使之认为剪切版上一直有内容
        if ("hasPrimaryClip".equals(method.getName())) {
            return true;
        }

        return method.invoke(base, args);
    }

}
