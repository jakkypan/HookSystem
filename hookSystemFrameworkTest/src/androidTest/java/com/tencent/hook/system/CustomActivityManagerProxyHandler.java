package com.tencent.hook.system;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.util.Log;

import com.tencent.qa.hook.activitymanager.DefaultActivityManagerProxyHandler;

import java.lang.reflect.Method;

/**
 * Created by richarddu on 2018/6/21.
 */

// method list
// https://android.googlesource.com/platform/frameworks/base/+/c80f952/core/java/android/app/IActivityManager.java
public class CustomActivityManagerProxyHandler extends DefaultActivityManagerProxyHandler {

    private static final String TAG = CustomActivityManagerProxyHandler.class.getSimpleName();

    public CustomActivityManagerProxyHandler(Object instance) {
        super(instance);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        if ("startActivity".equals(method.getName())) {
            Intent intent = (Intent) args[2];
            Log.d(TAG, "args intent is " + intent);
            boolean test = intent.getBooleanExtra("test", false);
            Log.d(TAG, "args test is " + test);
        } else if ("checkPermission".equals(method.getName())) {
            String permission = (String) args[0];
            Log.d(TAG, "permission is " + permission);
            if (Manifest.permission.ACCESS_COARSE_LOCATION.equals(permission)) {
                return PackageManager.PERMISSION_DENIED;
            }
        }
        return method.invoke(mInstance, args);
    }
}
