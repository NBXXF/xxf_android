package com.xxf.utils;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.os.Process;
import android.text.TextUtils;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Description 进程工具类
 *
 * @@Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * date createTime：16/6/23
 * version
 */
public final class ProcessUtils {

    /**
     * 缓存变量 提高复用效率
     */
    private static volatile String currentProcessName;

    private ProcessUtils() {
    }

    public static boolean isMainProcess(Context context) {
        if (context == null) {
            return false;
        }

        String packageName = context.getApplicationContext().getPackageName();
        String processName = ProcessUtils.getProcessName(context);
        return packageName.equals(processName);
    }

    public static String getProcessName(Context context) {
        if (!TextUtils.isEmpty(currentProcessName)) {
            return currentProcessName;
        }
        //28的新方式
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            currentProcessName = Application.getProcessName();
        }
        if (!TextUtils.isEmpty(currentProcessName)) {
            return currentProcessName;
        }

        currentProcessName = getProcessFromFile();
        if (!TextUtils.isEmpty(currentProcessName)) {
            return currentProcessName;
        }

        // 如果装了xposed一类的框架，上面可能会拿不到，回到遍历迭代的方式
        currentProcessName = getProcessNameByAM(context);

        return currentProcessName;
    }

    private static String getProcessFromFile() {
        BufferedReader reader = null;
        try {
            int pid = android.os.Process.myPid();
            String file = "/proc/" + pid + "/cmdline";
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "iso-8859-1"));
            int c;
            StringBuilder processName = new StringBuilder();
            while ((c = reader.read()) > 0) {
                processName.append((char) c);
            }
            return processName.toString();
        } catch (Exception e) {
            return null;
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static String getProcessNameByAM(Context context) {
        String processName = null;

        ActivityManager am = ((ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE));
        if (am == null) {
            return null;
        }

        while (true) {
            List<ActivityManager.RunningAppProcessInfo> plist = am.getRunningAppProcesses();
            if (plist != null) {
                for (ActivityManager.RunningAppProcessInfo info : plist) {
                    if (info.pid == android.os.Process.myPid()) {
                        processName = info.processName;

                        break;
                    }
                }
            }

            if (!TextUtils.isEmpty(processName)) {
                return processName;
            }

            try {
                Thread.sleep(100L); // take a rest and again
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }

    public static boolean isMainProcessLive(Context context) {
        if (context == null) {
            return false;
        }

        final String processName = context.getPackageName();
        ActivityManager am = ((ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE));
        if (am != null) {
            List<ActivityManager.RunningAppProcessInfo> plist = am.getRunningAppProcesses();
            if (plist != null) {
                for (ActivityManager.RunningAppProcessInfo info : plist) {
                    if (info.processName.equals(processName)) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    public static boolean isEmpty(Collection collection) {
        return collection == null || collection.isEmpty();
    }

    /**
     * 获取正在运行的进程列表
     *
     * @return
     */
    public static List<ActivityManager.RunningAppProcessInfo> getRunningAppProcesses(Context context) {
        try {
            ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            List<ActivityManager.RunningAppProcessInfo> runningAppProcesses = manager.getRunningAppProcesses();
            if (runningAppProcesses == null) {
                return Collections.emptyList();
            }
            return runningAppProcesses;
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

    /**
     * 根据进程名字匹配 是否还活着的进程id
     *
     * @param kProcessName
     * @return
     */
    public static List<ActivityManager.RunningAppProcessInfo> getProcess(Context context, String kProcessName) {
        List<ActivityManager.RunningAppProcessInfo> filterProcess = new ArrayList<>();
        List<ActivityManager.RunningAppProcessInfo> runningAppProcesses = getRunningAppProcesses(context);
        for (ActivityManager.RunningAppProcessInfo runningAppProcess : runningAppProcesses) {
            if (TextUtils.equals(runningAppProcess.processName, kProcessName)) {
                filterProcess.add(runningAppProcess);
            }
        }
        return filterProcess;
    }

    /**
     * 进程是否活着
     *
     * @param kPid
     * @return
     */
    public static boolean isLiving(Context context, int kPid) {
        List<ActivityManager.RunningAppProcessInfo> runningAppProcesses = getRunningAppProcesses(context);
        for (ActivityManager.RunningAppProcessInfo runningAppProcess : runningAppProcesses) {
            if (runningAppProcess.pid == kPid) {
                return true;
            }
        }
        return false;
    }

    /**
     * 进程是否活着
     *
     * @param kProcessName
     * @return
     */
    public static boolean isLiving(Context context, String kProcessName) {
        List<ActivityManager.RunningAppProcessInfo> runningAppProcesses = getRunningAppProcesses(context);
        for (ActivityManager.RunningAppProcessInfo runningAppProcess : runningAppProcesses) {
            if (TextUtils.equals(kProcessName, runningAppProcess.processName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 杀掉app所有进程
     */
    public static void waitKillAppComplete(Context context) {
        waitKillAppComplete(context, context.getPackageName());
        System.exit(0);
    }


    /**
     * 杀掉app所有进程
     *
     * @param kPackageName
     */
    public static void waitKillAppComplete(Context context, String kPackageName) {
        List<ActivityManager.RunningAppProcessInfo> runningAppProcesses = getRunningAppProcesses(context);
        for (ActivityManager.RunningAppProcessInfo runningAppProcess : runningAppProcesses) {
            String processName = runningAppProcess.processName;
            if (TextUtils.equals(kPackageName, processName)
                    || processName.startsWith(kPackageName)) {
                waitKillProcessComplete(context, runningAppProcess.pid);
            }
        }
    }

    /**
     * 等待彻底杀死
     *
     * @param kProcessName
     */
    public static void waitKillProcessComplete(Context context, String kProcessName) {
        if (!TextUtils.isEmpty(kProcessName)) {
            List<ActivityManager.RunningAppProcessInfo> processList = getProcess(context, kProcessName);
            for (ActivityManager.RunningAppProcessInfo process : processList) {
                if (process.pid >= 0) {
                    waitKillProcessComplete(context, process.pid);
                }
            }
        }
    }


    /**
     * 等待彻底杀死
     *
     * @param kPid
     */
    public static void waitKillProcessComplete(Context context, int kPid) {
        if (kPid >= 0) {
            try {
                int times = 0;
                while (true) {
                    times++;
                    try {
                        Process.killProcess(kPid);
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                    if (times > 40 || !isLiving(context, kPid)) {
                        break;
                    }
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        times = Integer.MAX_VALUE;
                    }
                }
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }
}
