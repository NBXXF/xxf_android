package com.xxf.utils;

import android.content.ComponentName;
import android.content.Context;
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

import static android.Manifest.permission.CALL_PHONE;

import com.xxf.fileprovider.FileProvider7;

/**
 * @Author: XGod  xuanyouwu@163.com  17611639080
 * Date: 2/3/18 2:36 PM
 * Description: 意图 工具类
 */
public final class IntentUtils {
    /**
     * Return whether the intent is available.
     *
     * @param intent The intent.
     * @return {@code true}: yes<br>{@code false}: no
     */
    public static boolean isIntentAvailable(Context context, final Intent intent) {
        return context
                .getPackageManager()
                .queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY)
                .size() > 0;
    }

    /**
     * Return the intent of install app.
     * <p>Target APIs greater than 25 must hold
     * {@code <uses-permission android:name="android.permission.REQUEST_INSTALL_PACKAGES" />}</p>
     *
     * @param filePath The path of file.
     * @return the intent of install app
     */
    public static Intent getInstallAppIntent(Context context,final String filePath) {
        return getInstallAppIntent(context,FileUtils.getFileByPath(filePath));
    }

    /**
     * Return the intent of install app.
     * <p>Target APIs greater than 25 must hold
     * {@code <uses-permission android:name="android.permission.REQUEST_INSTALL_PACKAGES" />}</p>
     *
     * @param file The file.
     * @return the intent of install app
     */
    public static Intent getInstallAppIntent(Context context,final File file) {
        if (!FileUtils.isFileExists(context,file)) return null;
        Uri uri= FileProvider7.INSTANCE.getUriForFile(context,file);
        return getInstallAppIntent(uri);
    }

    /**
     * Return the intent of install app.
     * <p>Target APIs greater than 25 must hold
     * {@code <uses-permission android:name="android.permission.REQUEST_INSTALL_PACKAGES" />}</p>
     *
     * @param uri The uri.
     * @return the intent of install app
     */
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
     * Return the intent of uninstall app.
     * <p>Target APIs greater than 25 must hold
     * Must hold {@code <uses-permission android:name="android.permission.REQUEST_DELETE_PACKAGES" />}</p>
     *
     * @param pkgName The name of the package.
     * @return the intent of uninstall app
     */
    public static Intent getUninstallAppIntent(final String pkgName) {
        Intent intent = new Intent(Intent.ACTION_DELETE);
        intent.setData(Uri.parse("package:" + pkgName));
        return intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    }

    /**
     * Return the intent of launch app.
     *
     * @param pkgName The name of the package.
     * @return the intent of launch app
     */
    @Nullable
    public static Intent getLaunchAppIntent(Context context,final String pkgName) {
        String launcherActivity = getLauncherActivity(context,pkgName);
        if (TextUtils.isEmpty(launcherActivity)) return null;
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        intent.setClassName(pkgName, launcherActivity);
        return intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    }

    /**
     * Return the name of launcher activity.
     *
     * @param pkg The name of the package.
     * @return the name of launcher activity
     */
    public static String getLauncherActivity(Context context,@NonNull final String pkg) {
        if (TextUtils.isEmpty(pkg)) return "";
        Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        intent.setPackage(pkg);
        PackageManager pm = context.getPackageManager();
        List<ResolveInfo> info = pm.queryIntentActivities(intent, 0);
        if (info == null || info.size() == 0) {
            return "";
        }
        return info.get(0).activityInfo.name;
    }

    /**
     * Return the intent of launch app details settings.
     *
     * @param pkgName The name of the package.
     * @return the intent of launch app details settings
     */
    public static Intent getLaunchAppDetailsSettingsIntent(final String pkgName) {
        return getLaunchAppDetailsSettingsIntent(pkgName, false);
    }

    /**
     * Return the intent of launch app details settings.
     *
     * @param pkgName The name of the package.
     * @return the intent of launch app details settings
     */
    public static Intent getLaunchAppDetailsSettingsIntent(final String pkgName, final boolean isNewTask) {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + pkgName));
        return getIntent(intent, isNewTask);
    }

    /**
     * Return the intent of share text.
     *
     * @param content The content.
     * @return the intent of share text
     */
    public static Intent getShareTextIntent(final String content) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, content);
        intent = Intent.createChooser(intent, "");
        return getIntent(intent, true);
    }

    /**
     * Return the intent of share image.
     *
     * @param imagePath The path of image.
     * @return the intent of share image
     */
    public static Intent getShareImageIntent(final String imagePath) {
        return getShareTextImageIntent("", imagePath);
    }

    /**
     * Return the intent of share image.
     *
     * @param imageFile The file of image.
     * @return the intent of share image
     */
    public static Intent getShareImageIntent(final File imageFile) {
        return getShareTextImageIntent("", imageFile);
    }

    /**
     * Return the intent of share image.
     *
     * @param imageUri The uri of image.
     * @return the intent of share image
     */
    public static Intent getShareImageIntent(final Uri imageUri) {
        return getShareTextImageIntent("", imageUri);
    }

    /**
     * Return the intent of share image.
     *
     * @param content   The content.
     * @param imagePath The path of image.
     * @return the intent of share image
     */
    public static Intent getShareTextImageIntent(@Nullable final String content, final String imagePath) {
        return getShareTextImageIntent(content, FileUtils.getFileByPath(imagePath));
    }

    /**
     * Return the intent of share image.
     *
     * @param content   The content.
     * @param imageFile The file of image.
     * @return the intent of share image
     */
    public static Intent getShareTextImageIntent(@Nullable final String content, final File imageFile) {
        return getShareTextImageIntent(content, Uri.fromFile(imageFile));
    }

    /**
     * Return the intent of share image.
     *
     * @param content  The content.
     * @param imageUri The uri of image.
     * @return the intent of share image
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
     * Return the intent of share images.
     *
     * @param imagePaths The paths of images.
     * @return the intent of share images
     */
    public static Intent getShareImageIntent(final LinkedList<String> imagePaths) {
        return getShareTextImageIntent("", imagePaths);
    }

    /**
     * Return the intent of share images.
     *
     * @param images The files of images.
     * @return the intent of share images
     */
    public static Intent getShareImageIntent(final List<File> images) {
        return getShareTextImageIntent("", images);
    }

    /**
     * Return the intent of share images.
     *
     * @param uris The uris of image.
     * @return the intent of share image
     */
    public static Intent getShareImageIntent(final ArrayList<Uri> uris) {
        return getShareTextImageIntent("", uris);
    }

    /**
     * Return the intent of share images.
     *
     * @param content    The content.
     * @param imagePaths The paths of images.
     * @return the intent of share images
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
     * Return the intent of share images.
     *
     * @param content The content.
     * @param images  The files of images.
     * @return the intent of share images
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
     * Return the intent of share images.
     *
     * @param content The content.
     * @param uris    The uris of image.
     * @return the intent of share image
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
     * Return the intent of dial.
     *
     * @param phoneNumber The phone number.
     * @return the intent of dial
     */
    public static Intent getDialIntent(final String phoneNumber) {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneNumber));
        return getIntent(intent, true);
    }

    /**
     * Return the intent of call.
     * <p>Must hold {@code <uses-permission android:name="android.permission.CALL_PHONE" />}</p>
     *
     * @param phoneNumber The phone number.
     * @return the intent of call
     */
    @RequiresPermission(CALL_PHONE)
    public static Intent getCallIntent(final String phoneNumber) {
        Intent intent = new Intent("android.intent.action.CALL", Uri.parse("tel:" + phoneNumber));
        return getIntent(intent, true);
    }

    /**
     * Return the intent of send SMS.
     *
     * @param phoneNumber The phone number.
     * @param content     The content of SMS.
     * @return the intent of send SMS
     */
    public static Intent getSendSmsIntent(final String phoneNumber, final String content) {
        Uri uri = Uri.parse("smsto:" + phoneNumber);
        Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
        intent.putExtra("sms_body", content);
        return getIntent(intent, true);
    }

    /**
     * Return the intent of capture.
     *
     * @param outUri The uri of output.
     * @return the intent of capture
     */
    public static Intent getCaptureIntent(final Uri outUri) {
        return getCaptureIntent(outUri, false);
    }

    /**
     * Return the intent of capture.
     *
     * @param outUri    The uri of output.
     * @param isNewTask True to add flag of new task, false otherwise.
     * @return the intent of capture
     */
    public static Intent getCaptureIntent(final Uri outUri, final boolean isNewTask) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, outUri);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        return getIntent(intent, isNewTask);
    }

    private static Intent getIntent(final Intent intent, final boolean isNewTask) {
        return isNewTask ? intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK) : intent;
    }

//    /**
//     * 获取选择照片的 Intent
//     *
//     * @return
//     */
//    public static Intent getPickIntentWithGallery() {
//        Intent intent = new Intent(Intent.ACTION_PICK);
//        return intent.setType("image*//*");
//    }
//
//    /**
//     * 获取从文件中选择照片的 Intent
//     *
//     * @return
//     */
//    public static Intent getPickIntentWithDocuments() {
//        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//        return intent.setType("image*//*");
//    }
//
//
//    public static Intent buildImageGetIntent(final Uri saveTo, final int outputX, final int outputY, final boolean returnData) {
//        return buildImageGetIntent(saveTo, 1, 1, outputX, outputY, returnData);
//    }
//
//    public static Intent buildImageGetIntent(Uri saveTo, int aspectX, int aspectY,
//                                             int outputX, int outputY, boolean returnData) {
//        Intent intent = new Intent();
//        if (Build.VERSION.SDK_INT < 19) {
//            intent.setAction(Intent.ACTION_GET_CONTENT);
//        } else {
//            intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
//            intent.addCategory(Intent.CATEGORY_OPENABLE);
//        }
//        intent.setType("image*//*");
//        intent.putExtra("output", saveTo);
//        intent.putExtra("aspectX", aspectX);
//        intent.putExtra("aspectY", aspectY);
//        intent.putExtra("outputX", outputX);
//        intent.putExtra("outputY", outputY);
//        intent.putExtra("scale", true);
//        intent.putExtra("return-data", returnData);
//        intent.putExtra("outputFormat", Bitmap.CompressFormat.PNG.toString());
//        return intent;
//    }
//
//    public static Intent buildImageCropIntent(final Uri uriFrom, final Uri uriTo, final int outputX, final int outputY, final boolean returnData) {
//        return buildImageCropIntent(uriFrom, uriTo, 1, 1, outputX, outputY, returnData);
//    }
//
//    public static Intent buildImageCropIntent(Uri uriFrom, Uri uriTo, int aspectX, int aspectY,
//                                              int outputX, int outputY, boolean returnData) {
//        Intent intent = new Intent("com.android.camera.action.CROP");
//        intent.setDataAndType(uriFrom, "image*//*");
//        intent.putExtra("crop", "true");
//        intent.putExtra("output", uriTo);
//        intent.putExtra("aspectX", aspectX);
//        intent.putExtra("aspectY", aspectY);
//        intent.putExtra("outputX", outputX);
//        intent.putExtra("outputY", outputY);
//        intent.putExtra("scale", true);
//        intent.putExtra("return-data", returnData);
//        intent.putExtra("outputFormat", Bitmap.CompressFormat.PNG.toString());
//        return intent;
//    }
//
//    public static Intent buildImageCaptureIntent(final Uri uri) {
//        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
//        return intent;
//    }
}
