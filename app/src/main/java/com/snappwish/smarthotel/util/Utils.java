package com.snappwish.smarthotel.util;

import android.app.Activity;
import android.app.KeyguardManager;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.os.Build;
import android.os.PowerManager;
import android.view.View;
import android.widget.Toast;

import com.snappwish.smarthotel.ScreenOffAdminReceiver;

/**
 * Created by jinjin on 2019/1/18.
 * description:
 */
public class Utils {

    public static void hideBottomUIMenu(Activity context) {
        //隐藏虚拟按键，并且全屏
        if (Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) { // lower api
            View v = context.getWindow().getDecorView();
            v.setSystemUiVisibility(View.GONE);
        } else if (Build.VERSION.SDK_INT >= 19) {
            //for new api versions.
            View decorView = context.getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }

    /**
     * 唤醒手机屏幕并解锁
     */
    public static void wakeUpAndUnlock(Context context) {
        // 获取电源管理器对象
        PowerManager pm = (PowerManager) context
                .getSystemService(Context.POWER_SERVICE);
        boolean screenOn = pm.isScreenOn();
        if (!screenOn) {
            // 获取PowerManager.WakeLock对象,后面的参数|表示同时传入两个值,最后的是LogCat里用的Tag
            PowerManager.WakeLock wl = pm.newWakeLock(
                    PowerManager.ACQUIRE_CAUSES_WAKEUP |
                            PowerManager.SCREEN_BRIGHT_WAKE_LOCK, "bright");
            wl.acquire(10000); // 点亮屏幕
            wl.release(); // 释放
        }
        //        // 屏幕解锁
        KeyguardManager keyguardManager = (KeyguardManager) context
                .getSystemService(Context.KEYGUARD_SERVICE);
        KeyguardManager.KeyguardLock keyguardLock = keyguardManager.newKeyguardLock("unLock");
        //        // 屏幕锁定
        //        keyguardLock.reenableKeyguard();
        //        keyguardLock.disableKeyguard(); // 解锁
    }

    public static void screenOff(Context context) {
        DevicePolicyManager policyManager = (DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);
        ComponentName adminReceiver = new ComponentName(context, ScreenOffAdminReceiver.class);
        boolean admin = policyManager.isAdminActive(adminReceiver);
        if (admin) {
            //            isScreenOn = false;
            policyManager.lockNow();
        } else {
            Toast.makeText(context.getApplicationContext(), "没有设备管理权限", Toast.LENGTH_SHORT).show();
        }
    }
}
