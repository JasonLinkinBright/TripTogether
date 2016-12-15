package com.jason.trip.util;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Version V1.0 <Trip客户端>
 * ClassName:PhoneUtils
 * Description:
 * Created by Jason on 16/11/22.
 */
public class PhoneUtils {

    /**
     * 获取当前客户端版本号
     *
     * @param context 上下文环境
     * @return 客户端的版本号
     */
    public static int getAppVersionCode(Context context) {
        int versionCode = -1;
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            versionCode = info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionCode;
    }

    /**
     * 获取当前APP版本描述
     *
     * @param context 上下文
     * @return 客户端描述
     */
    public static String getAppVersionName(Context context) {
        String versionName = "";
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            versionName = info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionName;
    }

    /**
     * 获取App安装包信息
     *
     * @return App安装包信息
     */
    public PackageInfo getPackageInfo(Context context) {
        PackageInfo info = null;
        try {
            info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace(System.err);
        }
        if (info == null) info = new PackageInfo();
        return info;
    }

    /**
     * 检测网络是否可用
     *
     * @return true - 网络已连接或正在连接
     * false - 网络未连接
     */
    public boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        return info != null && info.isConnectedOrConnecting();
    }

    /**
     * 判断当前设备是手机还是平板，代码来自 Google I/O App for Android
     *
     * @param context 上下文
     * @return 平板返回 True，手机返回 False
     */
    public static boolean isTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    /**
     * 获取手机IMEI
     *
     * @param context 上下文环境
     * @return 手机的IMEI
     */
    public static String getIMEI(Context context) {
        return ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
    }

    /**
     * 获取屏幕的宽度
     *
     * @param content 上下文环境
     * @return 屏幕的宽度
     */
    public static int getScreenWidth(Context content) {
        DisplayMetrics displayMetrics = content.getResources().getDisplayMetrics();
        return displayMetrics.widthPixels;
    }

    /**
     * 获取屏幕的高度
     *
     * @param content 上下文环境
     * @return 屏幕的高度
     */
    public static int getScreenHeight(Context content) {
        DisplayMetrics displayMetrics = content.getResources().getDisplayMetrics();
        return displayMetrics.heightPixels;
    }

    /**
     * 获取屏幕的密度
     *
     * @param content 上下文环境
     * @return 屏幕的密度
     */
    public static float getScreenDensity(Context content) {
        DisplayMetrics displayMetrics = content.getResources().getDisplayMetrics();
        return displayMetrics.density;
    }

    /**
     * 获取系统可用内存大小
     */
    public static int getAvalMem(Context context) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (manager != null) {
            return manager.getMemoryClass();
        }
        return -1;
    }

    /**
     * 获取唯一设备ID
     */
    @SuppressWarnings("deprecation")
    public static String getDeviceToken(Context context) {
        //23以上
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String deviceid = "" + tm.getDeviceId();
        String SimSerialNumber = "" + tm.getSimSerialNumber();
        String ANDROID_ID = "" + Settings.System.getString(context.getContentResolver(), Settings.System.ANDROID_ID);
        String SerialNumber = "" + android.os.Build.SERIAL;
        return deviceid + SimSerialNumber + ANDROID_ID + SerialNumber + "comcibqdzgandroid14";
    }

    /**
     * 获取唯一设备ID
     */
    public static String getDeviceToken2(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String deviceid = "" + tm.getDeviceId();
        String SerialNumber = "" + android.os.Build.SERIAL;
        return deviceid + SerialNumber + "comcibqdzgandroid14";
    }

    /**
     * MD5后的唯一设备ID
     **/
    public static String getMD5Token(Context context) {
        String src = getDeviceToken(context);
        byte[] hash;
        try {
            hash = MessageDigest.getInstance("MD5").digest(src.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Huh, MD5 should be supported?", e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Huh, UTF-8 should be supported?", e);
        }
        StringBuilder hex = new StringBuilder(hash.length * 2);
        for (byte b : hash) {
            if ((b & 0xFF) < 0x10)
                hex.append("0");
            hex.append(Integer.toHexString(b & 0xFF));
        }
        return hex.toString();
    }

}