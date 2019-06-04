//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.xxf.arch.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build.VERSION;
import android.telephony.TelephonyManager;
import android.util.Log;


/**
 * 来自环信
 */
public class NetUtils {
    private static final String TAG = "net";
    private static final int LOW_SPEED_UPLOAD_BUF_SIZE = 1024;
    private static final int HIGH_SPEED_UPLOAD_BUF_SIZE = 10240;
    private static final int MAX_SPEED_UPLOAD_BUF_SIZE = 102400;
    private static final int LOW_SPEED_DOWNLOAD_BUF_SIZE = 2024;
    private static final int HIGH_SPEED_DOWNLOAD_BUF_SIZE = 30720;
    private static final int MAX_SPEED_DOWNLOAD_BUF_SIZE = 102400;

    public NetUtils() {
    }

    public static boolean hasNetwork(Context var0) {
        if (var0 != null) {
            try {
                ConnectivityManager var1 = (ConnectivityManager)
                        var0.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo var2 = var1.getActiveNetworkInfo();
                return var2 != null ? var2.isAvailable() : false;
            } catch (Throwable e) {
                e.printStackTrace();
            }

        }
        return false;
    }

    public static boolean hasDataConnection(Context var0) {
        try {
            ConnectivityManager var1 = (ConnectivityManager)
                    var0.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo var2 = var1.getNetworkInfo(1);
            if (var2 != null && var2.isAvailable() && var2.isConnected()) {
                Log.d("net", "has wifi connection");
                return true;
            } else {
                var2 = var1.getNetworkInfo(0);
                if (var2 != null && var2.isAvailable() && var2.isConnected()) {
                    Log.d("net", "has mobile connection");
                    return true;
                } else {
                    if (VERSION.SDK_INT >= 13) {
                        var2 = var1.getNetworkInfo(9);
                        if (var2 != null && var2.isAvailable() && var2.isConnected()) {
                            Log.d("net", "has ethernet connection");
                            return true;
                        }
                    }

                    Log.d("net", "no data connection");
                    return false;
                }
            }
        } catch (Exception var3) {
            return false;
        }
    }

    public static boolean isWifiConnection(Context var0) {
        try {
            ConnectivityManager var1 = (ConnectivityManager)
                    var0.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo var2 = var1.getNetworkInfo(1);
            if (var2 != null && var2.isAvailable() && var2.isConnected()) {
                Log.d("net", "wifi is connected");
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean isMobileConnection(Context var0) {
        try {
            ConnectivityManager var1 = (ConnectivityManager) var0.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo var2 = var1.getNetworkInfo(0);
            if (var2 != null && var2.isAvailable() && var2.isConnected()) {
                Log.d("net", "mobile is connected");
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean isEthernetConnection(Context var0) {
        try {
            if (VERSION.SDK_INT < 13) {
                return false;
            } else {
                ConnectivityManager var1 = (ConnectivityManager) var0.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo var2 = var1.getNetworkInfo(9);
                if (var2 != null && var2.isAvailable() && var2.isConnected()) {
                    Log.d("net", "ethernet is connected");
                    return true;
                } else {
                    return false;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;

    }

    public static int getUploadBufSize(Context var0) {
        try {
            ConnectivityManager var1 = (ConnectivityManager) var0.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo var2 = var1.getActiveNetworkInfo();
            return var2 != null && var2.getType() == 1 ? 102400 : (VERSION.SDK_INT >= 13 && var2 != null && var2.getType() == 9 ? 102400 : (var2 == null && isConnectionFast(var2.getType(), var2.getSubtype()) ? 10240 : 1024));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    public static int getDownloadBufSize(Context var0) {
        try {
            ConnectivityManager var1 = (ConnectivityManager) var0.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo var2 = var1.getActiveNetworkInfo();
            return var2 != null && var2.getType() == 1 ? 102400 : (VERSION.SDK_INT >= 13 && var2 != null && var2.getType() == 9 ? 102400 : (var2 == null && isConnectionFast(var2.getType(), var2.getSubtype()) ? 30720 : 2024));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    private static boolean isConnectionFast(int var0, int var1) {
        if (var0 == 1) {
            return true;
        } else if (VERSION.SDK_INT >= 13 && var0 == 9) {
            return true;
        } else {
            if (var0 == 0) {
                switch (var1) {
                    case 1:
                        return false;
                    case 2:
                        return false;
                    case 3:
                        return true;
                    case 4:
                        return false;
                    case 5:
                        return true;
                    case 6:
                        return true;
                    case 7:
                        return false;
                    case 8:
                        return true;
                    case 9:
                        return true;
                    case 10:
                        return true;
                    default:
                        if (VERSION.SDK_INT >= 11 && (var1 == 14 || var1 == 13)) {
                            return true;
                        }

                        if (VERSION.SDK_INT >= 9 && var1 == 12) {
                            return true;
                        }

                        if (VERSION.SDK_INT >= 8 && var1 == 11) {
                            return false;
                        }
                }
            }

            return false;
        }
    }

    public static String getNetworkType(Context var0) {
        try {
            ConnectivityManager var1 = (ConnectivityManager) var0.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo var2 = var1.getActiveNetworkInfo();
            if (var2 != null && var2.isAvailable()) {
                int var3 = var2.getType();
                if (VERSION.SDK_INT >= 13 && var3 == 9) {
                    return "ETHERNET";
                } else if (var3 == 1) {
                    return "WIFI";
                } else {
                    TelephonyManager var4 = (TelephonyManager) var0.getSystemService(Context.TELEPHONY_SERVICE);
                    switch (var4.getNetworkType()) {
                        case 1:
                        case 2:
                        case 4:
                        case 7:
                        case 11:
                            return "2G";
                        case 3:
                        case 5:
                        case 6:
                        case 8:
                        case 9:
                        case 10:
                        case 12:
                        case 14:
                        case 15:
                            return "3G";
                        case 13:
                            return "4G";
                        default:
                            return "unkonw network";
                    }
                }
            } else {
                return "no network";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "no network";

    }
}
