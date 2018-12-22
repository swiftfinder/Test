package com.snappwish.base_core.permission;

import android.app.Activity;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;

import java.util.List;

/**
 * Created by jinjin on 2017/6/11.
 */

public class PermissionHelper {

    private Object mObject;
    private int mRequestCode;
    private String[] mRequestPermissions;

    private PermissionHelper(Object object) {
        this.mObject = object;
    }

    public static PermissionHelper with(Activity activity) {
        return new PermissionHelper(activity);
    }

    public static PermissionHelper with(Fragment fragment) {
        return new PermissionHelper(fragment);
    }

    public PermissionHelper requestCode(int requestCode) {
        this.mRequestCode = requestCode;
        return this;
    }

    public PermissionHelper permissions(String... permissions) {
        this.mRequestPermissions = permissions;
        return this;
    }

    public void request() {
        // 判断版本是否在6.0以上
        if (!PermissionUtils.isOverM()) {
            // 直接执行方法
            PermissionUtils.executeSuccessMethod(mObject, mRequestCode);
            return;
        }

        // 6.0以上
        // 判断是否已经授予权限
        List<String> deniedPermission = PermissionUtils.getDeniedPermission(mObject, mRequestPermissions);

        if (deniedPermission.size() == 0) {
            // 都授权过
            PermissionUtils.executeSuccessMethod(mObject, mRequestCode);
        } else {
            // 没有授予，请求权限
            if (mObject instanceof Activity) {
                ActivityCompat.requestPermissions(PermissionUtils.getActivity(mObject),
                        deniedPermission.toArray(new String[deniedPermission.size()]), mRequestCode);
            } else if (mObject instanceof Fragment) {
                ((Fragment) mObject).requestPermissions(deniedPermission.toArray(new String[deniedPermission.size()]), mRequestCode);
            }
        }

    }

    public static void requestPermission(Activity activity, int requestCode, String... permissions) {
        PermissionHelper.with(activity).requestCode(requestCode).permissions(permissions).request();
    }

    // 权限请求回调
    public static void onRequestPermissionsResult(Object object, int requestCode, String[] permissions) {
        List<String> deniedPermission = PermissionUtils.getDeniedPermission(object, permissions);
        if (deniedPermission.size() == 0) {
            // 全部授权
            PermissionUtils.executeSuccessMethod(object, requestCode);
        } else {
            // 拒绝授权
            PermissionUtils.executeFailureMethod(object, requestCode);
        }
    }
}
