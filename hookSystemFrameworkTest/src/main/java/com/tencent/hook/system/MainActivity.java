package com.tencent.hook.system;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.tencent.qa.hook.reflect.MethodUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * Created by richarddu on 2018/6/24.
 */

public class MainActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

//        int result = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);
//        if (result != PackageManager.PERMISSION_GRANTED) {
//            //如果应用之前请求过此权限但用户拒绝了请求，此方法将返回 true。
//            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_COARSE_LOCATION)) {
//                //这里可以写个对话框之类的项向用户解释为什么要申请权限，并在对话框的确认键后续再次申请权限
//            } else {
//                //申请权限，字符串数组内是一个或多个要申请的权限，1是申请权限结果的返回参数，在onRequestPermissionsResult可以得知申请结果
//                ActivityCompat.requestPermissions(this,
//                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
//            }
//        }

        TextView textView = (TextView) findViewById(R.id.mian_id);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AccountManager accountManager = getAccountManager();
                try {
                    List list = (List) MethodUtils.invokeMethod(accountManager,
                            "obtainPreferences", null);
                    Log.d("richard", "list is " + list);
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d("test", "onRequestPermissionsResult requestCode is " + requestCode);
    }

    private final static Singleton<LifePlayAccountManager, Context> sAccountManager = new Singleton<LifePlayAccountManager, Context>() {
        @Override
        protected LifePlayAccountManager create(Context context) {
            return new LifePlayAccountManager(context);
        }
    };

    public LifePlayAccountManager getAccountManager() {

        return sAccountManager.get(MainActivity.this.getApplicationContext());
    }
}
