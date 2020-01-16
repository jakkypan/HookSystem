package com.tencent.hook.system;

import android.Manifest;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.VpnService;
import android.net.wifi.WifiManager;
import android.support.test.filters.SmallTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.tencent.qa.hook.activitymanager.ActivityManagerHook;
import com.tencent.qa.hook.clipboard.ClipboardManagerHook;
import com.tencent.qa.hook.connectivity.ConnectivityManagerHook;
import com.tencent.qa.hook.packagemanager.PackageManagerHook;
import com.tencent.qa.hook.reflect.FieldUtils;
import com.tencent.qa.hook.wifimanager.WifiManagerHook;

import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import java.util.concurrent.CountDownLatch;

/**
 * Created by richarddu on 2018/6/24.
 */

@RunWith(AndroidJUnit4.class)
@SmallTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ExampleTest {
    @Rule
    public ActivityTestRule mActivityRule = new ActivityTestRule(MainActivity.class);

    @Before
    public void setUp() {

    }

    @After
    public void tearDown() {

    }

    @Test
    public void testClipManager() {
        ClipboardManagerHook clipboardManagerHook = new ClipboardManagerHook(CustomClipboardServiceBinderHookHandler.class);
        clipboardManagerHook.hookClipboardManager();

        ClipboardManager clipboardManager = (ClipboardManager) mActivityRule.getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
        Log.d("test", "hasPrimaryClip is " + clipboardManager.hasPrimaryClip());

        Log.d("test", "clipData is " + clipboardManager.getPrimaryClip());
    }

    @Test
    public void testWifiManager() {
        WifiManagerHook wifiManagerHook = new WifiManagerHook(CustomWifiServiceBinderHookHandler.class);
        wifiManagerHook.hookWifiManager();

        WifiManager wifiManager = (WifiManager) mActivityRule.getActivity().getSystemService(Context.WIFI_SERVICE);
        Log.d("test", "isWifiEnabled is " + wifiManager.isWifiEnabled());
    }

    @Test
    public void testConnectivityManager() {

        ConnectivityManagerHook connectivityManagerHook = new ConnectivityManagerHook(CustomConnectivityServiceBinderHookHandler.class);
        connectivityManagerHook.hookConnectivityManager();

        Intent intent = VpnService.prepare(mActivityRule.getActivity().getApplicationContext());
        Log.d("test", "intent is " + intent);

        if (intent != null) {
            mActivityRule.getActivity().startActivity(intent);
        }
    }

    @Test
    public void testStartActivity() {
        ActivityManagerHook activityManagerHook = new ActivityManagerHook(CustomActivityManagerProxyHandler.class);
        try {
            activityManagerHook.hooActivityManager();
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        Intent intent = new Intent(mActivityRule.getActivity(), SecondActivity.class);
        intent.putExtra("test", true);
        mActivityRule.getActivity().startActivity(intent);
    }

    @Test
    public void testPackageManager() {
        PackageManagerHook packageManagerHook = new PackageManagerHook(CustomPackageManagerProxyHandler.class);
        packageManagerHook.hookPackageManager(mActivityRule.getActivity());

        PackageManager packageManager = mActivityRule.getActivity().getPackageManager();
        try {
            packageManager.getPackageInfo(mActivityRule.getActivity().getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testCheckPermission() {
        ActivityManagerHook activityManagerHook = new ActivityManagerHook(CustomActivityManagerProxyHandler.class);
        try {
            activityManagerHook.hooActivityManager();
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        PackageManagerHook packageManagerHook = new PackageManagerHook(CustomPackageManagerProxyHandler.class);
        try {
            packageManagerHook.hookPackageManager(mActivityRule.getActivity());
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        int result = ContextCompat.checkSelfPermission(mActivityRule.getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION);
        Log.d("test", "testCheckPermission result is " + result);
        if (result != PackageManager.PERMISSION_GRANTED) {
            //如果应用之前请求过此权限但用户拒绝了请求，此方法将返回 true。
            if (ActivityCompat.shouldShowRequestPermissionRationale(mActivityRule.getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION)) {
                //这里可以写个对话框之类的项向用户解释为什么要申请权限，并在对话框的确认键后续再次申请权限
            } else {
                try {
                    boolean mHasCurrentPermissionsRequest = (boolean) FieldUtils.readField(mActivityRule.getActivity(), "mHasCurrentPermissionsRequest");
                    Log.d("test", "mHasCurrentPermissionsRequest result is " + mHasCurrentPermissionsRequest);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                //申请权限，字符串数组内是一个或多个要申请的权限，1是申请权限结果的返回参数，在onRequestPermissionsResult可以得知申请结果
                ActivityCompat.requestPermissions(mActivityRule.getActivity(),
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
            }
        }

        CountDownLatch countDownLatch = new CountDownLatch(1);
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
