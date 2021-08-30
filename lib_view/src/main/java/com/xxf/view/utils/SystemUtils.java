package com.xxf.view.utils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresPermission;
import androidx.core.content.FileProvider;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LifecycleOwner;

import com.xxf.application.ApplicationProvider;
import com.xxf.arch.XXF;
import com.xxf.activityresult.ActivityResult;
import com.xxf.arch.utils.UriUtils;
import com.xxf.permission.PermissionDeniedException;
import com.xxf.permission.transformer.RxPermissionTransformer;
import com.xxf.utils.FileUtils;
import com.xxf.view.exception.FileNotMatchTypeException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.concurrent.Callable;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableSource;
import io.reactivex.rxjava3.functions.Function;
import io.reactivex.rxjava3.functions.Supplier;
import io.reactivex.rxjava3.schedulers.Schedulers;


/**
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * @Description
 */
public class SystemUtils {

    public static final int REQUEST_CODE_CAMERA = 59999;
    public static final int REQUEST_CODE_ALBUM = 59998;
    public static final int REQUEST_CODE_DOCUMENT = 59997;
    public static final int REQUEST_CODE_SHARE = 59996;
    public static final int REQUEST_CODE_CROP = 59995;


    /**
     * 获取拍照意图
     *
     * @param imageFile
     * @return
     */
    @RequiresPermission(allOf = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA})
    public static Intent getTakePhotoIntent(File imageFile) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (Build.VERSION.SDK_INT < 24) {
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(imageFile));
        } else {
            ContentValues contentValues = new ContentValues(1);
            contentValues.put("_data", imageFile.getAbsolutePath());
            Uri outImgUri = ApplicationProvider.applicationContext.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, outImgUri);
        }
        return intent;
    }

    /**
     * 相册意图
     *
     * @return
     */
    @RequiresPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
    public static Intent getAlbumIntent() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        return intent;
    }

    /**
     * 调用系统拍照
     * 自动请求权限 没有权限报异常 {@link PermissionDeniedException}
     *
     * @param context
     * @param cropBuilder 裁切
     * @return
     */
    public static Observable<String> takePhoto(final FragmentActivity context,
                                               @Nullable PathCropIntentBuilder cropBuilder) {
        return XXF.requestPermission(context, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .compose(new RxPermissionTransformer(context, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE))
                .flatMap(new Function<Boolean, ObservableSource<String>>() {
                    @SuppressLint("MissingPermission")
                    @Override
                    public ObservableSource<String> apply(Boolean storageCameraPermissionAllow) throws Exception {
                        File picFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), System.currentTimeMillis() + ".png");
                        if (!picFile.exists()) {
                            picFile.createNewFile();
                        }
                        return XXF.startActivityForResult(context, getTakePhotoIntent(picFile), REQUEST_CODE_CAMERA)
                                .flatMap(new Function<ActivityResult, ObservableSource<String>>() {
                                    @Override
                                    public ObservableSource<String> apply(ActivityResult activityResult) throws Throwable {
                                        if (!activityResult.isOk()) {
                                            return Observable.empty();
                                        }
                                        return Observable.just(picFile.getAbsolutePath());
                                    }
                                });
                    }
                })
                .flatMap(new Function<String, ObservableSource<String>>() {
                    @Override
                    public ObservableSource<String> apply(String s) throws Throwable {
                        if (cropBuilder != null) {
                            cropBuilder.inputImgFile(new File(s));
                            return XXF.startActivityForResult(context, cropBuilder.build(), REQUEST_CODE_CROP)
                                    .flatMap(new Function<ActivityResult, ObservableSource<String>>() {
                                        @Override
                                        public ObservableSource<String> apply(ActivityResult activityResult) throws Throwable {
                                            if (!activityResult.isOk()) {
                                                return Observable.empty();
                                            }
                                            return Observable.just(cropBuilder.outPutPath);
                                        }
                                    });
                        }
                        return Observable.just(s);
                    }
                })
                .subscribeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 选择相片
     * 自动请求权限 没有权限报异常 {@link PermissionDeniedException}
     *
     * @param context
     * @return
     */
    public static Observable<String> selectAlbum(final FragmentActivity context, @Nullable PathCropIntentBuilder cropBuilder) {
        return XXF.requestPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .compose(new RxPermissionTransformer(context, Manifest.permission.WRITE_EXTERNAL_STORAGE))
                .flatMap(new Function<Boolean, ObservableSource<String>>() {
                    @SuppressLint("MissingPermission")
                    @Override
                    public ObservableSource<String> apply(Boolean aBoolean) throws Exception {
                        return XXF.startActivityForResult(context, getAlbumIntent(), REQUEST_CODE_ALBUM)
                                .flatMap(new Function<ActivityResult, ObservableSource<String>>() {
                                    @Override
                                    public ObservableSource<String> apply(ActivityResult activityResult) throws Throwable {
                                        if (!activityResult.isOk()) {
                                            return Observable.empty();
                                        }
                                        return Observable.just(UriUtils.getPath(context, activityResult.getData().getData()));
                                    }
                                });
                    }
                })
                .flatMap(new Function<String, ObservableSource<String>>() {
                    @Override
                    public ObservableSource<String> apply(String s) throws Throwable {
                        if (cropBuilder != null) {
                            cropBuilder.inputImgFile(new File(s));
                            return XXF.startActivityForResult(context, cropBuilder.build(), REQUEST_CODE_CROP)
                                    .flatMap(new Function<ActivityResult, ObservableSource<String>>() {
                                        @Override
                                        public ObservableSource<String> apply(ActivityResult activityResult) throws Throwable {
                                            if (!activityResult.isOk()) {
                                                return Observable.empty();
                                            }
                                            return Observable.just(cropBuilder.outPutPath);
                                        }
                                    });
                        }
                        return Observable.just(s);
                    }
                })
                .subscribeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 保存图片到相册
     * 自动请求权限 没有权限报异常 {@link PermissionDeniedException}
     *
     * @param context
     * @param picName 是name 不说full path
     * @param bmp
     * @return
     */
    public static Observable<File> saveImageToAlbum(FragmentActivity context, String picName, Bitmap bmp) {
        return XXF.requestPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .compose(new RxPermissionTransformer(context, Manifest.permission.WRITE_EXTERNAL_STORAGE))
                .flatMap(new Function<Boolean, ObservableSource<File>>() {
                    @Override
                    public ObservableSource<File> apply(Boolean aBoolean) throws Exception {
                        return Observable
                                .fromCallable(new Callable<File>() {
                                    @Override
                                    public File call() throws Exception {
                                        File picFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), picName);
                                        FileOutputStream fos = new FileOutputStream(picFile);
                                        bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                                        fos.flush();
                                        fos.close();
                                        try {
                                            MediaStore.Images.Media.insertImage(context.getContentResolver(),
                                                    picFile.getAbsolutePath(), picName, null);
                                        } catch (FileNotFoundException e) {
                                            e.printStackTrace();
                                        }
                                        // 最后通知图库更新
                                        Uri localUri = Uri.fromFile(picFile);
                                        Intent localIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, localUri);
                                        context.sendBroadcast(localIntent);
                                        return picFile;
                                    }
                                })
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread());
                    }
                });
    }

    public static Observable<String> selectFile(final FragmentActivity activity) {
        return selectFile(activity, new String[]{"*/*"});
    }

    /**
     * 选择文件
     * Intent.ACTION_OPEN_DOCUMENT 只 支持 图片 文档 视频 音频 意图[在锤子手机上不能选择pdf]
     * 注意！！ 华为mate20x【【Intent.ACTION_GET_CONTENT 有bug 不能选择具体类型】】 目前没有很好的解决方案 建议自己做文件选择器
     *
     * @param activity
     * @param mimeTypes {"image/*", "text/*"}; 每个元素只能是单一的
     * @return
     */
    public static Observable<String> selectFile(final FragmentActivity activity, String[] mimeTypes) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("*/*");
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
        return XXF.requestPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .compose(new RxPermissionTransformer(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE))
                .flatMap(new Function<Boolean, ObservableSource<String>>() {
                    @Override
                    public ObservableSource<String> apply(Boolean aBoolean) throws Throwable {
                        return XXF.startActivityForResult(activity, intent, REQUEST_CODE_DOCUMENT)
                                .flatMap(new Function<ActivityResult, ObservableSource<String>>() {
                                    @Override
                                    public ObservableSource<String> apply(ActivityResult activityResult) throws Throwable {
                                        if (!activityResult.isOk()) {
                                            return Observable.empty();
                                        }
                                        return Observable.fromCallable(new Callable<String>() {
                                            @Override
                                            public String call() throws Exception {
                                                return UriUtils.getPath(activity, activityResult.getData().getData());
                                            }
                                        }).map(new Function<String, String>() {
                                            @Override
                                            public String apply(String s) throws Throwable {
                                                /**
                                                 * fix:华为手机【【Intent.ACTION_GET_CONTENT 】 还能跳转到文件管理器选择其他类型的文件
                                                 */
                                                HashMap<String, HashSet<String>> mimeTypeMap = new HashMap<>();
                                                for (String mimeType : mimeTypes) {
                                                    String[] split = mimeType.split("/");
                                                    if (mimeTypeMap.get(split[0]) == null) {
                                                        mimeTypeMap.put(split[0], new HashSet<>());
                                                    }
                                                    mimeTypeMap.get(split[0]).add(split[1]);
                                                }
                                                if (mimeTypeMap.get("*") != null) {
                                                    return s;
                                                }
                                                String fileMimeType = FileUtils.getMimeType(s);
                                                String[] fileMimeTypeArray = fileMimeType.split("/");
                                                HashSet<String> supportTypes = mimeTypeMap.get(fileMimeTypeArray[0]);
                                                if (supportTypes != null && (supportTypes.contains("*") || supportTypes.contains(fileMimeTypeArray[1]))) {
                                                    return s;
                                                }
                                                throw new FileNotMatchTypeException("file not match type " + Arrays.toString(mimeTypes));
                                            }
                                        }).subscribeOn(Schedulers.io());
                                    }
                                })
                                .observeOn(AndroidSchedulers.mainThread());
                    }
                });
    }

    /**
     * 隐藏软键盘
     *
     * @param act
     */
    public static void hideSoftKeyBoard(Activity act) {
        try {
            InputMethodManager imm = (InputMethodManager) act.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(act.getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {
        }
    }

    /**
     * 隐藏软键盘
     *
     * @param act
     */
    public static void hideSoftKeyBoard(Activity act, boolean clearFouces) {
        try {
            if (clearFouces) {
                View currentFocus = act.getCurrentFocus();
                if (currentFocus != null) {
                    currentFocus.clearFocus();
                }
            }
            InputMethodManager imm = (InputMethodManager) act.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(act.getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {
        }
    }

    /**
     * 隐藏软键盘2
     *
     * @param act
     */
    public static void hideSoftKeyBoard(Context act, View v) {
        try {
            InputMethodManager imm = (InputMethodManager) act.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        } catch (Exception e) {
        }
    }

    /**
     * 隐藏软键盘2
     *
     * @param act
     */
    public static void hideSoftKeyBoard(Context act, View v, boolean clearFouces) {
        try {
            if (v != null && clearFouces) {
                v.clearFocus();
            }
            InputMethodManager imm = (InputMethodManager) act.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        } catch (Exception e) {
        }
    }


    /**
     * 显示软键盘
     *
     * @param act
     */
    public static void showSoftKeyBoard(Activity act) {
        try {
            InputMethodManager imm = (InputMethodManager) act.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(act.getCurrentFocus(), InputMethodManager.SHOW_IMPLICIT);
        } catch (Exception e) {
        }
    }

    /**
     * 显示软键盘2
     *
     * @param act
     */
    public static void showSoftKeyBoard(Context act, View v) {
        try {
            if (v != null) {
                v.requestFocus();
            }
            InputMethodManager imm = (InputMethodManager) act.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(v, InputMethodManager.SHOW_IMPLICIT);
        } catch (Exception e) {
        }
    }
    /**
     * 显示软键盘2
     *
     * @param act
     */
    public static void showSoftKeyBoardForce(Context act, View v) {
        try {
            if (v != null) {
                v.requestFocus();
            }
            InputMethodManager imm = (InputMethodManager) act.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(v, InputMethodManager.SHOW_FORCED);
        } catch (Exception e) {
        }
    }


    /**
     * 复制到粘贴板
     *
     * @param lable
     * @param charSequence
     */
    public static void copyTextToClipboard(@Nullable String lable, @NonNull CharSequence charSequence) {
        ClipboardManager cmb = (ClipboardManager) ApplicationProvider.applicationContext.getSystemService(Context.CLIPBOARD_SERVICE);
        if (cmb == null) {
            return;
        }
        //将内容放入粘贴管理器,在别的地方长按选择"粘贴"即可
        cmb.setPrimaryClip(ClipData.newPlainText(lable, charSequence));
    }

    /**
     * 复制到粘贴板
     *
     * @param charSequence
     */
    public static void copyTextToClipboard(@NonNull CharSequence charSequence) {
        ClipboardManager cmb = (ClipboardManager) ApplicationProvider.applicationContext.getSystemService(Context.CLIPBOARD_SERVICE);
        if (cmb == null) {
            return;
        }
        //将内容放入粘贴管理器,在别的地方长按选择"粘贴"即可
        cmb.setPrimaryClip(ClipData.newPlainText("text", charSequence));
    }

    /**
     * 获取粘贴板内容
     *
     * @return
     */
    public static CharSequence getTextFromClipboard() {
        ClipboardManager cmb = (ClipboardManager) ApplicationProvider.applicationContext.getSystemService(Context.CLIPBOARD_SERVICE);
        if (cmb != null) {
            if (cmb.hasPrimaryClip()) {
                if (cmb.getPrimaryClip().getItemCount() > 0) {
                    return cmb.getPrimaryClip().getItemAt(0).getText();
                }
            }
        }
        return null;
    }

    /**
     * 监听软键盘是否点击了搜索／下一步／发送／完成／回车按钮
     * （在TextView.OnEditorActionListener的 onEditorAction(TextView v, int actionId, KeyEvent event)方法监听）
     *
     * @param actionId
     * @param event
     * @return
     */
    public static boolean isSearchClick(int actionId, KeyEvent event) {
        return actionId == EditorInfo.IME_ACTION_SEARCH
                || actionId == EditorInfo.IME_ACTION_NEXT
                || actionId == EditorInfo.IME_ACTION_SEND
                || actionId == EditorInfo.IME_ACTION_DONE
                || (event != null && KeyEvent.KEYCODE_ENTER == event.getKeyCode() && KeyEvent.ACTION_DOWN == event.getAction());
    }


    private static Uri queryMediaImageUri(File imageFile) {
        String filePath = imageFile.getAbsolutePath();
        Cursor cursor = ApplicationProvider.applicationContext.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new String[]{"_id"}, "_data=? ", new String[]{filePath}, (String) null);
        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor.getColumnIndex("_id"));
            Uri baseUri = Uri.parse("content://media/external/images/media");
            return Uri.withAppendedPath(baseUri, "" + id);
        } else if (imageFile.exists()) {
            ContentValues values = new ContentValues();
            values.put("_data", filePath);
            return ApplicationProvider.applicationContext.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        } else {
            return null;
        }
    }

    public static class PathCropIntentBuilder extends CropIntentBuilder {
        public String outPutPath;

        public PathCropIntentBuilder(String outPutPath) {
            this.outPutPath = outPutPath;
            File file = new File(outPutPath);
            if (!file.exists()) {
                try {
                    file.createNewFile();
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
            this.outputFile(file);
        }
    }

    public static class CropIntentBuilder {
        private Intent mCropIntent = new Intent("com.android.camera.action.CROP");

        public CropIntentBuilder() {
            //默认值 不能修改!!!!!
            this.mCropIntent.putExtra("crop", true);
            this.mCropIntent.putExtra("outputX", 320);
            this.mCropIntent.putExtra("outputY", 320);
            this.mCropIntent.putExtra("scale", true);
            this.mCropIntent.putExtra("aspectX", 1);
            this.mCropIntent.putExtra("aspectY", 1);
            this.mCropIntent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
            this.mCropIntent.putExtra("noFaceDetection", true);
            this.mCropIntent.putExtra("scale", true);
            this.mCropIntent.putExtra("scaleUpIfNeeded", true);
        }

        public CropIntentBuilder inputImgUri(Uri inImgUri) {
            this.mCropIntent.setDataAndType(inImgUri, "image/*");
            return this;
        }

        public CropIntentBuilder inputImgFile(File inImgFile) {
            Uri inImgUri = queryMediaImageUri(inImgFile);
            this.mCropIntent.setDataAndType(inImgUri, "image/*");
            return this;
        }

        public CropIntentBuilder aspectXY(int x, int y) {
            this.mCropIntent.putExtra("aspectX", x);
            this.mCropIntent.putExtra("aspectY", y);
            return this;
        }

        public CropIntentBuilder outputXY(int x, int y) {
            this.mCropIntent.putExtra("outputX", x);
            this.mCropIntent.putExtra("outputY", y);
            return this;
        }

        public CropIntentBuilder outputBitmap() {
            this.mCropIntent.putExtra("return-data", true);
            return this;
        }

        public CropIntentBuilder outputUri(Uri outputImgUri) {
            this.mCropIntent.putExtra("return-data", false);
            this.mCropIntent.putExtra("output", outputImgUri);
            return this;
        }

        public CropIntentBuilder outputFile(File outputImgFile) {
            this.mCropIntent.putExtra("return-data", false);
            this.mCropIntent.putExtra("output", queryMediaImageUri(outputImgFile));
            return this;
        }

        public Intent build() {
            return this.mCropIntent;
        }
    }

    /**
     * 分享文本 或者链接
     *
     * @param context
     * @param text        分享文本
     * @param packageName 指定 包名 空 会调用系统选择面板,否则调用指定的app
     * @return
     */
    public static Observable<ActivityResult> shareText(Context context, String text, @Nullable String packageName) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, text);
        sendIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        try {
            String scheme = Uri.parse(text).getScheme();
            if (TextUtils.equals(scheme, "http") || TextUtils.equals(scheme, "https")) {
                sendIntent.setType("text/html");
            } else {
                sendIntent.setType("text/plain");
            }
        } catch (Throwable e) {
            e.printStackTrace();
            sendIntent.setType("text/plain");
        }
        if (!TextUtils.isEmpty(packageName)) {
            sendIntent.setPackage(packageName);
        }
        Intent chooser = Intent.createChooser(sendIntent, "share text");
        if (context instanceof LifecycleOwner) {
            return XXF.startActivityForResult((LifecycleOwner) context, chooser, REQUEST_CODE_SHARE);
        } else {
            return Observable
                    .fromCallable(new Callable<ActivityResult>() {
                        @Override
                        public ActivityResult call() throws Exception {
                            context.startActivity(chooser);
                            return new ActivityResult(REQUEST_CODE_SHARE, Activity.RESULT_OK, new Intent());
                        }
                    });
        }
    }

    /**
     * 分享文件
     *
     * @param context
     * @param filePath    文件
     * @param authority   如果文件是私有目录一定要传递  authority 或者 if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
     *                    StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
     *                    StrictMode.setVmPolicy(builder.build());
     *                    }
     * @param packageName 指定 包名 空 会调用系统选择面板,否则调用指定的app
     * @return
     */
    public static Observable<ActivityResult> shareFile(Context context,
                                                       String filePath,
                                                       @Nullable String authority,
                                                       @Nullable String packageName) {
        return Observable.defer(new Supplier<ObservableSource<ActivityResult>>() {
            @Override
            public ObservableSource<ActivityResult> get() throws Throwable {
                File file = new File(filePath);
                Intent intent = new Intent(Intent.ACTION_SEND);
                Uri uri;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N && authority != null) {
                    uri = FileProvider.getUriForFile(context, authority, file);
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                } else {
                    uri = Uri.fromFile(file);
                }
                intent.putExtra(Intent.EXTRA_STREAM, uri);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                String fileType = FileUtils.getMimeType(filePath);
                if (TextUtils.isEmpty(fileType)) {
                    ContentResolver contentResolver = context.getContentResolver();
                    if (contentResolver != null) {
                        fileType = contentResolver.getType(uri);
                    }
                }
                if (TextUtils.isEmpty(fileType)) {
                    fileType = "file/*";
                }
                intent.setDataAndType(uri, fileType);
                if (!TextUtils.isEmpty(packageName)) {
                    intent.setPackage(packageName);
                }
                Intent chooser = Intent.createChooser(intent, file.getName());
                if (context instanceof LifecycleOwner) {
                    return XXF.startActivityForResult((LifecycleOwner) context, chooser, REQUEST_CODE_SHARE);
                } else {
                    return Observable
                            .fromCallable(new Callable<ActivityResult>() {
                                @Override
                                public ActivityResult call() throws Exception {
                                    context.startActivity(chooser);
                                    return new ActivityResult(REQUEST_CODE_SHARE, Activity.RESULT_OK, new Intent());
                                }
                            });
                }
            }
        });
    }

    /**
     * 发送邮件
     *
     * @param context
     * @param email           邮箱
     * @param subject         邮件主题
     * @param content         邮件正文
     * @param chooserAppTitle 多个邮件app 选择对话框标题
     * @return
     */
    public static Observable<ActivityResult> sendEmail(@NonNull Context context,
                                                       @NonNull String email,
                                                       @Nullable String subject,
                                                       @Nullable String content,
                                                       @Nullable String chooserAppTitle) {
        return Observable.defer(new Supplier<ObservableSource<ActivityResult>>() {

            @Override
            public ObservableSource<ActivityResult> get() throws Throwable {
                // 必须明确使用mailto前缀来修饰邮件地址,如果使用
                // intent.putExtra(Intent.EXTRA_EMAIL, email)，结果将匹配不到任何应用
                Uri uri = Uri.parse("mailto:" + email);
                String[] emailArr = {email};
                Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
                intent.putExtra(Intent.EXTRA_CC, emailArr); // 抄送人
                intent.putExtra(Intent.EXTRA_SUBJECT, subject); // 主题
                intent.putExtra(Intent.EXTRA_TEXT, content); // 正文
                Intent chooser = Intent.createChooser(intent, chooserAppTitle);
                if (context instanceof LifecycleOwner) {
                    return XXF.startActivityForResult((LifecycleOwner) context, chooser, REQUEST_CODE_SHARE);
                } else {
                    return Observable
                            .fromCallable(new Callable<ActivityResult>() {
                                @Override
                                public ActivityResult call() throws Exception {
                                    context.startActivity(chooser);
                                    return new ActivityResult(REQUEST_CODE_SHARE, Activity.RESULT_OK, new Intent());
                                }
                            });
                }
            }
        });
    }
}
