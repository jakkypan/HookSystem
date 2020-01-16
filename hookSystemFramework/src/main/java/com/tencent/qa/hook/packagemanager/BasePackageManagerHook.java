package com.tencent.qa.hook.packagemanager;

import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

/**
 * Created by richarddu on 2018/6/21.
 */

public abstract class BasePackageManagerHook {

    private static final String TAG = BasePackageManagerHook.class.getSimpleName();

    private Context mBaseContext;
    private Object mProxyPm;
    private InvocationHandler mPackageManagerProxyHandler;

    public void hookPackageManager(Context context) {
        try {
            mBaseContext = context;
            PackageManager manager = mBaseContext.getPackageManager();
            if (mProxyPm == null) {
                Class ApplicationPackageManager = Class.forName("android.app.ApplicationPackageManager");
                Field field = ApplicationPackageManager.getDeclaredField("mPM");
                field.setAccessible(true);
                Object rawPm = field.get(manager);
                Class IPackageManagerClass = Class.forName("android.content.pm.IPackageManager");
                if (rawPm != null) {
                    if (mPackageManagerProxyHandler == null) {
                        mPackageManagerProxyHandler = getPackageManagerProxyHandler(rawPm, context);
                    }
                    if (mPackageManagerProxyHandler == null) {
                        throw new RuntimeException("Please init PackageManager InvocationHandler First");
                    }
                    mProxyPm = Proxy.newProxyInstance(mBaseContext.getClassLoader(), new Class[]{IPackageManagerClass}, mPackageManagerProxyHandler);
                }
                field.set(manager, mProxyPm);
            }
        } catch (Throwable e) {
            Log.d(TAG, "此处出错误了");
        }
    }

    /**
     * 子类实现动态代理的实例化
     *
     * @return
     */
    public abstract InvocationHandler getPackageManagerProxyHandler(Object pm, Context context);
}
