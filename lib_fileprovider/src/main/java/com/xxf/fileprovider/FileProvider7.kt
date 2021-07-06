package com.xxf.fileprovider;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;

import androidx.core.content.FileProvider;

import java.io.File;
import java.util.List;


/**
 * public void takePhotoNoCompress(View view) {
 * Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
 * if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
 * String filename = new SimpleDateFormat("yyyyMMdd-HHmmss", Locale.CHINA)
 * .format(new Date()) + ".png";
 * File file = new File(Environment.getExternalStorageDirectory(), filename);
 * mCurrentPhotoPath = file.getAbsolutePath();
 * // 仅需改变这一行
 * Uri fileUri = FileProvider7.getUriForFile(this, file);
 * <p>
 * takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
 * startActivityForResult(takePictureIntent, REQUEST_CODE_TAKE_PHOTO);
 * }
 * }
 * <p>
 * 示例二 安装apk
 * public void installApk(View view) {
 * File file = new File(Environment.getExternalStorageDirectory(),
 * "testandroid7-debug.apk");
 * Intent intent = new Intent(Intent.ACTION_VIEW);
 * // 仅需改变这一行
 * FileProvider7.setIntentDataAndType(this,
 * intent, "application/vnd.android.package-archive", file, true);
 * startActivity(intent);
 * }
 */
public class FileProvider7 {

    public static String getAuthority(Context context) {
        return context.getPackageName() + ".android7.fileprovider";
    }

    public static Uri getUriForFile(Context context, File file) {
        Uri fileUri = null;
        if (Build.VERSION.SDK_INT >= 24) {
            fileUri = getUriForFile24(context, file);
        } else {
            fileUri = Uri.fromFile(file);
        }
        return fileUri;
    }


    public static Uri getUriForFile24(Context context, File file) {
        Uri fileUri = FileProvider.getUriForFile(context,
                context.getPackageName() + ".android7.fileprovider",
                file);
        return fileUri;
    }


    public static void setIntentDataAndType(Context context,
                                            Intent intent,
                                            String type,
                                            File file,
                                            boolean writeAble) {
        if (Build.VERSION.SDK_INT >= 24) {
            intent.setDataAndType(getUriForFile(context, file), type);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            if (writeAble) {
                intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            }
        } else {
            intent.setDataAndType(Uri.fromFile(file), type);
        }
    }


    public static void setIntentData(Context context,
                                     Intent intent,
                                     File file,
                                     boolean writeAble) {
        if (Build.VERSION.SDK_INT >= 24) {
            intent.setData(getUriForFile(context, file));
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            if (writeAble) {
                intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            }
        } else {
            intent.setData(Uri.fromFile(file));
        }
    }


    public static void grantPermissions(Context context, Intent intent, Uri uri, boolean writeAble) {

        int flag = Intent.FLAG_GRANT_READ_URI_PERMISSION;
        if (writeAble) {
            flag |= Intent.FLAG_GRANT_WRITE_URI_PERMISSION;
        }
        intent.addFlags(flag);
        List<ResolveInfo> resInfoList = context.getPackageManager()
                .queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        for (ResolveInfo resolveInfo : resInfoList) {
            String packageName = resolveInfo.activityInfo.packageName;
            context.grantUriPermission(packageName, uri, flag);
        }
    }


}
