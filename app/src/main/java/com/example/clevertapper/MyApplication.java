package com.example.clevertapper;

import android.app.Application;

import com.clevertap.android.sdk.ActivityLifecycleCallback;

import io.branch.referral.Branch;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        ActivityLifecycleCallback.register(this);
        super.onCreate();

        Branch.enableDebugMode();
        Branch.getAutoInstance(MyApplication.this);
    }

}
