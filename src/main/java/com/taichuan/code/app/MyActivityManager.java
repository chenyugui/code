package com.taichuan.code.app;

import android.app.Activity;

import java.lang.ref.WeakReference;

/**
 * @author gui
 * @date 2020-11-09
 */
public class MyActivityManager {
    private static MyActivityManager sInstance = new MyActivityManager();
    private WeakReference<Activity> mCurrentActivityWeakRef;

    private MyActivityManager() {
    }

    public static MyActivityManager getInstance() {
        return sInstance;
    }

    public Activity getCurrentActivity() {
        Activity currentActivity = null;
        if (mCurrentActivityWeakRef != null) {
            currentActivity = mCurrentActivityWeakRef.get();
        }
        return currentActivity;
    }

    public void setCurrentActivity(Activity activity) {
        mCurrentActivityWeakRef = new WeakReference<>(activity);
    }
}
