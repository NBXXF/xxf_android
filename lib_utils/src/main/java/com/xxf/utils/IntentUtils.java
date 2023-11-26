package com.xxf.utils;

import android.Manifest;
import android.app.Application;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresPermission;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import com.xxf.application.ApplicationProviderKtKt;
import com.xxf.fileprovider.FileProvider7;

/**
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * @version 2.3.1
 * @Description  意图 工具类
 * @date createTime：2018/9/7
 */
public final class IntentUtils {

    /**
     * 私有 仅限内部链接application
     * @return
     */
    private static Application getLinkedApplication(){
       return ApplicationProviderKtKt.getApplication();
    }
    /**
     * 相册是否可用
     *
     * @param intent
     * @return
     */
    public static boolean isIntentAvailable(final Intent intent) {
        return getLinkedApplication()
                .getPackageManager()
                .queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY)
                .size() > 0;
    }


    /**
     * 安装apk
     *
     * @param filePath
     * @return
     */
    @RequiresPermission(Manifest.permission.REQUEST_INSTALL_PACKAGES)
    public static Intent getInstallAppIntent(final String filePath) {
        return getInstallAppIntent(FileUtils.getFileByPath(filePath));
    }

    /**
     * 安装apk
     *
     * @param file
     * @return
     */
    @RequiresPermission(Manifest.permission.REQUEST_INSTALL_PACKAGES)
    public static Intent getInstallAppIntent(final File file) {
        if (!FileUtils.isFileExists(getLinkedApplication(), file)) return null;
        Uri uri = FileProvider7.INSTANCE.getUriForFile(getLinkedApplication(), file);
        return getInstallAppIntent(uri);
    }

    /**
     * 安装apk
     *
     * @param uri
     * @return
     */
    @RequiresPermission(Manifest.permission.REQUEST_INSTALL_PACKAGES)
    public static Intent getInstallAppIntent(final Uri uri) {
        if (uri == null) return null;
        Intent intent = new Intent(Intent.ACTION_VIEW);
        String type = "application/vnd.android.package-archive";
        intent.setDataAndType(uri, type);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
        return intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    }

    /**
     * 卸载
     * @param pkgName
     * @return
     */
    @RequiresPermission(Manifest.permission.REQUEST_DELETE_PACKAGES)
    public static Intent getUninstallAppIntent(final String pkgName) {
        Intent intent = new Intent(Intent.ACTION_DELETE);
        intent.setData(Uri.parse("package:" + pkgName));
        return intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    }

    /**
     * 启动app
     * @param pkgName
     * @return
     */
    @Nullable
    public static Intent getLaunchAppIntent(final String pkgName) {
        String launcherActivity = getLauncherActivity(pkgName);
        if (TextUtils.isEmpty(launcherActivity)) return null;
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        intent.setClassName(pkgName, launcherActivity);
        return intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    }

    /**
     * app启动的主页面
     * @param pkgName
     * @return
     */
    public static String getLauncherActivity(@NonNull final String pkgName) {
        if (TextUtils.isEmpty(pkgName)) return "";
        Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        intent.setPackage(pkgName);
        PackageManager pm = getLinkedApplication().getPackageManager();
        List<ResolveInfo> info = pm.queryIntentActivities(intent, 0);
        if (info == null || info.size() == 0) {
            return "";
        }
        return info.get(0).activityInfo.name;
    }

    /**
     * 系统中设置中app设置详情页面
     * @param pkgName
     * @return
     */
    public static Intent getLaunchAppDetailsSettingsIntent(final String pkgName) {
        return getLaunchAppDetailsSettingsIntent(pkgName, false);
    }

    /**
     * 系统中设置中app设置详情页面
     * @param pkgName
     * @param isNewTask
     * @return
     */
    public static Intent getLaunchAppDetailsSettingsIntent(final String pkgName, final boolean isNewTask) {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + pkgName));
        return getIntent(intent, isNewTask);
    }

    /**
     * 跳转到系统设置页面
     *
     * @param isNewTask
     * @return
     */
    public static Intent getLaunchSettingsIntent(final boolean isNewTask) {
        return getIntent(new Intent(android.provider.Settings.ACTION_SETTINGS), isNewTask);
    }

    /**
     * 分享text
     * @param content
     * @return
     */
    public static Intent getShareTextIntent(final String content) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, content);
        intent = Intent.createChooser(intent, "");
        return getIntent(intent, true);
    }

    /**
     * 分享image
     * @param imagePath
     * @return
     */
    public static Intent getShareImageIntent(final String imagePath) {
        return getShareTextImageIntent("", imagePath);
    }

    /**
     * 分享image
     * @param imageFile
     * @return
     */
    public static Intent getShareImageIntent(final File imageFile) {
        return getShareTextImageIntent("", imageFile);
    }

    /**
     * 分享image
     * @param imageUri
     * @return
     */
    public static Intent getShareImageIntent(final Uri imageUri) {
        return getShareTextImageIntent("", imageUri);
    }

    /**
     * 分享text
     * @param content
     * @param imagePath
     * @return
     */
    public static Intent getShareTextImageIntent(@Nullable final String content, final String imagePath) {
        return getShareTextImageIntent(content, FileUtils.getFileByPath(imagePath));
    }

    /**
     * 分享image
     * @param content
     * @param imageFile
     * @return
     */
    public static Intent getShareTextImageIntent(@Nullable final String content, final File imageFile) {
        return getShareTextImageIntent(content, Uri.fromFile(imageFile));
    }

    /**
     * 分享text和文本
     * @param content
     * @param imageUri
     * @return
     */
    public static Intent getShareTextImageIntent(@Nullable final String content, final Uri imageUri) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT, content);
        intent.putExtra(Intent.EXTRA_STREAM, imageUri);
        intent.setType("image/*");
        intent = Intent.createChooser(intent, "");
        return getIntent(intent, true);
    }

    /**
     * 分享多张图片
     * @param imagePaths
     * @return
     */
    public static Intent getShareImageIntent(final LinkedList<String> imagePaths) {
        return getShareTextImageIntent("", imagePaths);
    }

    /**
     * 分享图多张片
     * @param images
     * @return
     */
    public static Intent getShareImageIntent(final List<File> images) {
        return getShareTextImageIntent("", images);
    }

    /**
     * 分享图多张片
     * @param uris
     * @return
     */
    public static Intent getShareImageIntent(final ArrayList<Uri> uris) {
        return getShareTextImageIntent("", uris);
    }

    /**
     * 分享文字和多张图
     * @param content
     * @param imagePaths
     * @return
     */
    public static Intent getShareTextImageIntent(@Nullable final String content,
                                                 final LinkedList<String> imagePaths) {
        List<File> files = new ArrayList<>();
        if (imagePaths != null) {
            for (String imagePath : imagePaths) {
                File file = FileUtils.getFileByPath(imagePath);
                if (file != null) {
                    files.add(file);
                }
            }
        }
        return getShareTextImageIntent(content, files);
    }

    /**
     * 分享文字和多张图
     * @param content
     * @param images
     * @return
     */
    public static Intent getShareTextImageIntent(@Nullable final String content, final List<File> images) {
        ArrayList<Uri> uris = new ArrayList<>();
        if (images != null) {
            for (File image : images) {
                Uri uri = Uri.fromFile(image);
                if (uri != null) {
                    uris.add(uri);
                }
            }
        }
        return getShareTextImageIntent(content, uris);
    }

    /**
     * 分享文字和多张图
     * @param content
     * @param uris
     * @return
     */
    public static Intent getShareTextImageIntent(@Nullable final String content, final ArrayList<Uri> uris) {
        Intent intent = new Intent(Intent.ACTION_SEND_MULTIPLE);
        intent.putExtra(Intent.EXTRA_TEXT, content);
        intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris);
        intent.setType("image/*");
        intent = Intent.createChooser(intent, "");
        return getIntent(intent, true);
    }

    /**
     * Return the intent of component.
     *
     * @param pkgName   The name of the package.
     * @param className The name of class.
     * @return the intent of component
     */
    public static Intent getComponentIntent(final String pkgName, final String className) {
        return getComponentIntent(pkgName, className, null, false);
    }

    /**
     * Return the intent of component.
     *
     * @param pkgName   The name of the package.
     * @param className The name of class.
     * @param isNewTask True to add flag of new task, false otherwise.
     * @return the intent of component
     */
    public static Intent getComponentIntent(final String pkgName,
                                            final String className,
                                            final boolean isNewTask) {
        return getComponentIntent(pkgName, className, null, isNewTask);
    }

    /**
     * Return the intent of component.
     *
     * @param pkgName   The name of the package.
     * @param className The name of class.
     * @param bundle    The Bundle of extras to add to this intent.
     * @return the intent of component
     */
    public static Intent getComponentIntent(final String pkgName,
                                            final String className,
                                            final Bundle bundle) {
        return getComponentIntent(pkgName, className, bundle, false);
    }

    /**
     * Return the intent of component.
     *
     * @param pkgName   The name of the package.
     * @param className The name of class.
     * @param bundle    The Bundle of extras to add to this intent.
     * @param isNewTask True to add flag of new task, false otherwise.
     * @return the intent of component
     */
    public static Intent getComponentIntent(final String pkgName,
                                            final String className,
                                            final Bundle bundle,
                                            final boolean isNewTask) {
        Intent intent = new Intent();
        if (bundle != null) intent.putExtras(bundle);
        ComponentName cn = new ComponentName(pkgName, className);
        intent.setComponent(cn);
        return getIntent(intent, isNewTask);
    }

    /**
     * Return the intent of shutdown.
     * <p>Requires root permission
     * or hold {@code android:sharedUserId="android.uid.system"},
     * {@code <uses-permission android:name="android.permission.SHUTDOWN" />}
     * in manifest.</p>
     *
     * @return the intent of shutdown
     */
    public static Intent getShutdownIntent() {
        Intent intent;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            intent = new Intent(Intent.ACTION_SHUTDOWN);
        } else {
            intent = new Intent("com.android.internal.intent.action.REQUEST_SHUTDOWN");
        }
        intent.putExtra("android.intent.extra.KEY_CONFIRM", false);
        return intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    }


    /**
     * 跳转到拨号界面
     * @param phoneNumber
     * @return
     */
    public static Intent getDialIntent(final String phoneNumber) {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneNumber));
        return getIntent(intent, true);
    }

    /**
     * 拨打电话
     * @param phoneNumber
     * @return
     */
    @RequiresPermission(Manifest.permission.CALL_PHONE)
    public static Intent getCallIntent(final String phoneNumber) {
        Intent intent = new Intent("android.intent.action.CALL", Uri.parse("tel:" + phoneNumber));
        return getIntent(intent, true);
    }

    /**
     * 发送短信
     * @param phoneNumber
     * @param content
     * @return
     */
    public static Intent getSendSmsIntent(final String phoneNumber, final String content) {
        Uri uri = Uri.parse("smsto:" + phoneNumber);
        Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
        intent.putExtra("sms_body", content);
        return getIntent(intent, true);
    }

    /**
     * 拍照
     * @param outUri
     * @return
     */
    public static Intent getCaptureIntent(final Uri outUri) {
        return getCaptureIntent(outUri, false);
    }

    /**
     * 拍照
     * @param outUri
     * @param isNewTask
     * @return
     */
    public static Intent getCaptureIntent(final Uri outUri, final boolean isNewTask) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, outUri);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        return getIntent(intent, isNewTask);
    }

    /**
     * 获取拍照意图
     *
     * @param oututFile
     * @return
     */
    @RequiresPermission(allOf = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA})
    public static Intent getCaptureIntent(File oututFile) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Uri uriForFile = FileProvider7.INSTANCE.getUriForFile(ApplicationProviderKtKt.getApplication(), oututFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uriForFile);
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        return intent;
    }

    /**
     * 相册选择意图
     *
     * @return
     */
    @RequiresPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
    public static Intent getPickImageFromGalleryIntent() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        return intent;
    }

    /**
     * 获取管理未知来源安装包
     * @param isNewTask
     * @return
     */
    public static Intent getManageUnknownAppSources(final boolean isNewTask) {
        Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES);
        return getIntent(intent, isNewTask);
    }


    private static Intent getIntent(final Intent intent, final boolean isNewTask) {
        return isNewTask ? intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK) : intent;
    }

}
