package com.tencent.qa.hook.compat;


import com.tencent.qa.hook.reflect.MethodUtils;

import java.lang.reflect.InvocationTargetException;

/**
 * Created by richarddu on 2018/6/22.
 */

public class ActivityManagerNativeCompat {

    private static Class sClass;

    public static Class Class() throws ClassNotFoundException {
        if (sClass == null) {
            sClass = Class.forName("android.app.ActivityManagerNative");
        }
        return sClass;
    }

    public static Object getDefault() throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        return MethodUtils.invokeStaticMethod(Class(), "getDefault");
    }
}
