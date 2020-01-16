package com.tencent.qa.hook.base;

import android.os.IBinder;
import android.support.annotation.NonNull;
import android.util.Log;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Map;

/**
 * Created by richarddu on 2018/6/21.
 */

/**
 * 要达到修改系统服务的目的, 我们需要如下两步:
 * 1：首先需要伪造一个系统服务对象, 接下来就要想办法让 asInterface 能够返回我们的这个伪造对象而不是原始的系统服务对象;
 * 2：要让 getService 返回 IBinder 对象的 queryLocalInterface 方法直接返回我们伪造过的系统服务对象;
 * 所以, 我们需要伪造一个 IBinder 对象, 主要是修改它的 queryLocalInterface 方法, 让它返回我们伪造的系统服务对象;
 * 3：把这个伪造对象放置在 ServiceManager 的缓存 map 里面即可.
 * <p>
 * 由于接管了asInterface 这个方法的全部, 伪造过的这个系统服务对象不能是只拥有本地 Binder 对象的能力,还要有
 * Binder 代理对象操控驱动的能力;
 * <p>
 * <p>
 * ServiceManager:https://android.googlesource.com/platform/frameworks/base/+/master/core/java/android/os/ServiceManager.java
 */
public abstract class BaseHookSystemService {

    /**
     * @param serviceName     在ServiceManager中注册的名称
     * @param stubClass       Stub class name
     * @param iInterfaceClass interface class name
     * @throws Exception
     */
    public void hookSystemService(@NonNull final String serviceName,
                                  @NonNull final String stubClass,
                                  @NonNull final String iInterfaceClass) throws Exception {
        // ServiceManager.getService("*****");
        // 只不过 ServiceManager这个类是@hide的
        Class<?> serviceManager = Class.forName("android.os.ServiceManager");
        Method getService = serviceManager.getDeclaredMethod("getService", String.class);

        // ServiceManager里面管理的原始的Clipboard Binder对象
        // 一般来说这是一个Binder代理对象
        IBinder rawBinder = (IBinder) getService.invoke(null, serviceName);

        // Hook 掉这个Binder代理对象的 queryLocalInterface 方法
        // 然后在 queryLocalInterface 返回一个IInterface对象, hook掉我们感兴趣的方法即可.
        IBinder hookedBinder = (IBinder) Proxy.newProxyInstance(serviceManager.getClassLoader(),
                new Class<?>[]{IBinder.class},
                new BaseBinderProxyHookHandler(rawBinder, stubClass, iInterfaceClass) {
                    @Override
                    public BaseBinderHookHandler getBinderHookHandler(IBinder base, Class<?> stubClass) {
                        return getBinderHookHandlerInstance(base, stubClass);
                    }
                });

        // 把这个hook过的Binder代理对象放进ServiceManager的cache里面
        // 以后查询的时候 会优先查询缓存里面的Binder, 这样就会使用被修改过的Binder了
        Field cacheField = serviceManager.getDeclaredField("sCache");
        cacheField.setAccessible(true);
        Map<String, IBinder> cache = (Map) cacheField.get(null);
        cache.put(serviceName, hookedBinder);
    }

    @NonNull
    public abstract BaseBinderHookHandler getBinderHookHandlerInstance(IBinder base, Class<?> stubClass);
}
