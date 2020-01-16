package com.tencent.hook.system;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

public class AccountManager {

    private final Context mContext;
    private final String mName;

    protected AccountManager(Context context, String name) {
        mContext = context.getApplicationContext();
        mName = name;
    }

    private List obtainPreferences() {

        return new ArrayList();
    }


}
