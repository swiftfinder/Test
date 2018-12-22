package com.snappwish.base_core.permission;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jinjin on 2017/6/11.
 */

public class PermissionUtils {

    private PermissionUtils() {
        throw new UnsupportedOperationException("can not instantiated!");
    }

    public static boolean isOverM() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return true;
        } else {
            return false;
        }
    }

    // 执行授权成功方法
    public static void executeSuccessMethod(Object object, int requestCode) {
        Method[] methods = object.getClass().getDeclaredMethods();
        for (Method method : methods) {
            PermissionSuccess success = method.getAnnotation(PermissionSuccess.class);
            if (success != null && success.requestCode() == requestCode) {
                executeMethod(method, object);
            }
        }
    }

    private static void executeMethod(Method method, Object object) {
        try {
            method.setAccessible(true);
            method.invoke(object);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 获取没有授权的权限
    public static List<String> getDeniedPermission(Object object, String[] requestPermissions) {
        List<String> deniedPermissions = new ArrayList<>();
        for (String requestPermission : requestPermissions) {
            // 遍历获取到没有授权的权限
            if (ContextCompat.checkSelfPermission(getActivity(object), requestPermission)
                    == PackageManager.PERMISSION_DENIED) {
                deniedPermissions.add(requestPermission);
            }
        }
        return deniedPermissions;
    }

    public static Activity getActivity(Object object) {
        if (object instanceof Activity) {
            return (Activity) object;
        } else {
            return ((Fragment) object).getActivity();
        }
    }

    // 执行授权拒绝方法
    public static void executeFailureMethod(Object object, int requestCode) {
        Method[] methods = object.getClass().getDeclaredMethods();
        for (Method method : methods) {
            PermissionFailure success = method.getAnnotation(PermissionFailure.class);
            if (success != null && success.requestCode() == requestCode) {
                executeMethod(method, object);
            }
        }
    }
}
