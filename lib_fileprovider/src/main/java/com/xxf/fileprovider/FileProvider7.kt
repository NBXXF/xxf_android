package com.xxf.fileprovider

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.core.content.FileProvider
import java.io.File

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
 *
 *
 * takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
 * startActivityForResult(takePictureIntent, REQUEST_CODE_TAKE_PHOTO);
 * }
 * }
 *
 *
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
object FileProvider7 {
    fun getAuthority(context: Context): String {
        return context.packageName + ".android7.fileprovider"
    }

    fun getUriForFile(context: Context, file: File?): Uri? {
        var fileUri: Uri? = null
        fileUri = if (Build.VERSION.SDK_INT >= 24) {
            getUriForFile24(context, file)
        } else {
            Uri.fromFile(file)
        }
        return fileUri
    }

    fun getUriForFile24(context: Context, file: File?): Uri {
        return FileProvider.getUriForFile(context,
                context.packageName + ".android7.fileprovider",
                file!!)
    }

    fun setIntentDataAndType(context: Context,
                             intent: Intent,
                             type: String?,
                             file: File?,
                             writeAble: Boolean) {
        if (Build.VERSION.SDK_INT >= 24) {
            intent.setDataAndType(getUriForFile(context, file), type)
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            if (writeAble) {
                intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
            }
        } else {
            intent.setDataAndType(Uri.fromFile(file), type)
        }
    }

    fun setIntentData(context: Context,
                      intent: Intent,
                      file: File?,
                      writeAble: Boolean) {
        if (Build.VERSION.SDK_INT >= 24) {
            intent.data = getUriForFile(context, file)
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            if (writeAble) {
                intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
            }
        } else {
            intent.data = Uri.fromFile(file)
        }
    }

    fun grantPermissions(context: Context, intent: Intent, uri: Uri?, writeAble: Boolean) {
        var flag = Intent.FLAG_GRANT_READ_URI_PERMISSION
        if (writeAble) {
            flag = flag or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
        }
        intent.addFlags(flag)
        val resInfoList = context.packageManager
                .queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY)
        for (resolveInfo in resInfoList) {
            val packageName = resolveInfo.activityInfo.packageName
            context.grantUriPermission(packageName, uri, flag)
        }
    }
}