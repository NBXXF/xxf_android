package com.xxf.view.utils

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.content.ClipData
import android.content.ClipboardManager
import android.content.ComponentName
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.text.TextUtils
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.activity.result.ActivityResult
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LifecycleOwner
import com.xxf.activityresult.isOk
import com.xxf.activityresult.startActivityForResult
import com.xxf.application.application
import com.xxf.fileprovider.FileProvider7
import com.xxf.fileprovider.FileProvider7.getUriForFile
import com.xxf.permission.requestPermission
import com.xxf.permission.transformer.RxPermissionTransformer
import com.xxf.utils.FileUtils
import com.xxf.utils.IntentUtils
import com.xxf.utils.UriUtils
import com.xxf.view.exception.FileNotMatchTypeException
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableSource
import io.reactivex.rxjava3.functions.Function
import io.reactivex.rxjava3.schedulers.Schedulers
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.util.Arrays
import java.util.Locale
import java.util.concurrent.Callable
import java.util.concurrent.TimeUnit

/**
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * @Description  相机,分享,键盘,粘贴板
 */
object SystemUtils {
    /**
     * 私有 仅限内部链接application
     * @return
     */
    private fun getLinkedApplication(): Application {
        return application
    }

    /**
     * 常见分享组件
     */
    val SHARE_QQ_FRIEND_COMPONENT =
        ComponentName("com.tencent.mobileqq", "com.tencent.mobileqq.activity.JumpActivity")

    /**
     * 注意微信朋友圈不支持纯文本
     */
    val SHARE_WECHAT_FRIEND_COMPONENT =
        ComponentName("com.tencent.mm", "com.tencent.mm.ui.tools.ShareImgUI")
    val SHARE_WECHAT_CIRCLE_COMPONENT =
        ComponentName("com.tencent.mm", "com.tencent.mm.ui.tools.ShareToTimeLineUI")
    val SHARE_WEIBO_FRIEND_COMPONENT =
        ComponentName("com.sina.weibo", "com.sina.weibo.weiyou.share.WeiyouShareDispatcher")
    val SHARE_WEIBO_CIRCLE_COMPONENT =
        ComponentName("com.sina.weibo", "com.sina.weibo.composerinde.ComposerDispatchActivity")


    /**
     * 拍摄视频
     *
     * @param context
     * @param bundle  控制市场 和质量 默认 0.5 时长10分钟
     * MediaStore.EXTRA_VIDEO_QUALITY  表示录制视频的质量，从 0-1，越大表示质量越好，同时视频也越大
     * MediaStore.EXTRA_DURATION_LIMIT  单位ms
     * @return
     */
    fun takeVideoUri(context: FragmentActivity, bundle: Bundle?): Observable<Uri> {
        return context.requestPermission(Manifest.permission.CAMERA)
            .compose(RxPermissionTransformer(context, Manifest.permission.CAMERA))
            .flatMap {
                val mIntent = Intent(MediaStore.ACTION_VIDEO_CAPTURE)
                //画质0.5
                mIntent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0.5)
                //设置时长
                mIntent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, TimeUnit.MINUTES.toMillis(10))
                bundle?.let { it1 -> mIntent.putExtras(it1) }
                context.startActivityForResult(mIntent)
                    .flatMap { activityResult ->
                        if (!activityResult.isOk) {
                            Observable.empty()
                        } else Observable.just(
                            activityResult.data!!.data!!
                        )
                    }
            }
    }

    /**
     * 拍摄视频
     *
     * @param context
     * @param bundle  控制市场 和质量
     * MediaStore.EXTRA_VIDEO_QUALITY  表示录制视频的质量，从 0-1，越大表示质量越好，同时视频也越大
     * MediaStore.EXTRA_DURATION_LIMIT  单位ms
     * @return
     */
    fun takeVideo(context: FragmentActivity, bundle: Bundle?): Observable<String> {
        return takeVideoUri(context, bundle)
            .observeOn(Schedulers.io())
            .map { uri -> UriUtils.getPath(context, uri)!! }
            .observeOn(AndroidSchedulers.mainThread())
    }

    /**
     * 调用系统拍照
     * 自动请求权限 没有权限报异常 [PermissionDeniedException]
     *
     * @param context
     * @param cropBuilder 裁切
     * @return
     */
    @SuppressLint("MissingPermission")
    fun takePhoto(
        context: FragmentActivity,
        cropBuilder: PathCropIntentBuilder?
    ): Observable<String> {
        return context.requestPermission(
            Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
            .compose(
                RxPermissionTransformer(
                    context!!,
                    Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
            )
            .flatMap {
                val picFile = File(
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM),
                    System.currentTimeMillis().toString() + ".png"
                )
                if (!picFile.exists()) {
                    picFile.createNewFile()
                }
                context.startActivityForResult(
                    IntentUtils.getCaptureIntent(picFile),
                )
                    .flatMap { activityResult ->
                        if (!activityResult.isOk) {
                            Observable.empty()
                        } else Observable.just(picFile.absolutePath)
                    }
            }
            .flatMap(object : Function<String, ObservableSource<String>> {
                @Throws(Throwable::class)
                override fun apply(s: String): ObservableSource<String> {
                    if (cropBuilder != null) {
                        cropBuilder.inputImgFile(File(s))
                        return context.startActivityForResult(
                            cropBuilder.build())
                            .flatMap { activityResult ->
                                if (!activityResult.isOk) {
                                    Observable.empty()
                                } else Observable.just(cropBuilder.outPutPath)
                            }
                    }
                    return Observable.just(s)
                }
            })
            .subscribeOn(AndroidSchedulers.mainThread())
    }

    /**
     * 选择相片
     * 自动请求权限 没有权限报异常 [PermissionDeniedException]
     *
     * @param context
     * @return
     */
    fun selectAlbum(
        context: FragmentActivity,
        cropBuilder: PathCropIntentBuilder?
    ): Observable<String> {
        return context.requestPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
            .compose(RxPermissionTransformer(context, Manifest.permission.READ_EXTERNAL_STORAGE))
            .flatMap(object : Function<Boolean, ObservableSource<String>> {
                @SuppressLint("MissingPermission")
                @Throws(Exception::class)
                override fun apply(aBoolean: Boolean): ObservableSource<String> {
                    return context.startActivityForResult(IntentUtils.getPickImageFromGalleryIntent())
                        .flatMap { activityResult ->
                            if (!activityResult.isOk) {
                                Observable.empty()
                            } else Observable.just(
                                UriUtils.getPath(
                                    context,
                                    activityResult.data!!.data
                                )!!
                            )
                        }
                }
            })
            .flatMap(object : Function<String, ObservableSource<String>> {
                @Throws(Throwable::class)
                override fun apply(s: String): ObservableSource<String> {
                    if (cropBuilder != null) {
                        cropBuilder.inputImgFile(File(s))
                        return context.startActivityForResult(
                            cropBuilder.build()
                        )
                            .flatMap { activityResult ->
                                if (!activityResult.isOk) {
                                    Observable.empty()
                                } else Observable.just(cropBuilder.outPutPath)
                            }
                    }
                    return Observable.just(s)
                }
            })
            .subscribeOn(AndroidSchedulers.mainThread())
    }

    /**
     * 保存图片到相册
     * 自动请求权限 没有权限报异常 [PermissionDeniedException]
     *
     * @param context
     * @param picName 是name 不说full path
     * @param bmp
     * @return
     */
    @JvmStatic
    fun saveImageToAlbum(
        context: FragmentActivity,
        picName: String?,
        bmp: Bitmap
    ): Observable<File> {
        return context.requestPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            .compose(RxPermissionTransformer(context, Manifest.permission.WRITE_EXTERNAL_STORAGE))
            .flatMap(object : Function<Boolean, ObservableSource<File>> {
                @Throws(Exception::class)
                override fun apply(aBoolean: Boolean): ObservableSource<File> {
                    return Observable
                        .fromCallable {
                            // 其次把文件插入到系统图库
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                                val values = ContentValues()
                                values.put(MediaStore.MediaColumns.DISPLAY_NAME, picName)
                                values.put(
                                    MediaStore.MediaColumns.MIME_TYPE,
                                    FileUtils.getMimeType(picName)
                                )
                                values.put(
                                    MediaStore.MediaColumns.RELATIVE_PATH,
                                    Environment.DIRECTORY_DCIM
                                )
                                val contentResolver = context.contentResolver
                                val uri = contentResolver.insert(
                                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                                    values
                                ) ?: throw RuntimeException("图片保存失败")
                                val fos = contentResolver.openOutputStream(uri)
                                bmp.compress(Bitmap.CompressFormat.PNG, 100, fos)
                                fos?.flush()
                                fos?.close()
                                File(
                                    UriUtils.getPath(
                                        getLinkedApplication(),
                                        uri
                                    )
                                )
                            } else {
                                val appDir =
                                    getLinkedApplication().getExternalFilesDir(
                                        Environment.DIRECTORY_PICTURES
                                    )
                                if (!appDir!!.exists()) {
                                    appDir.mkdir()
                                } //文件的名称设置为 系统时间.jpg
                                val file = File(appDir, picName)
                                try {
                                    val fos = FileOutputStream(file)
                                    bmp.compress(Bitmap.CompressFormat.PNG, 100, fos)
                                    fos.flush()
                                    fos.close()
                                } catch (e: FileNotFoundException) {
                                    e.printStackTrace()
                                } catch (e: IOException) {
                                    e.printStackTrace()
                                }
                                MediaScannerConnection.scanFile(
                                    context,
                                    arrayOf(file.absolutePath),
                                    arrayOf(FileUtils.getMimeType(file.absolutePath))
                                ) { path, uri -> }
                                //锤子8.1 必须下面这种扫描方式
                                MediaStore.Images.Media.insertImage(
                                    context.contentResolver,
                                    file.absolutePath,
                                    picName,
                                    null
                                )
                                context.sendBroadcast(
                                    Intent(
                                        Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                                        Uri.fromFile(file)
                                    )
                                )
                                file
                            }
                        }
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                }
            })
    }

    /**
     * 获取文件的uri 但是不是真实的文件路径
     * 建议 UriUtils.getPath(activity, uri); 但是对于android 10 大文件拷贝较慢  2G==3s拷贝时间
     *
     * @param activity
     * @param mimeTypes
     * @return
     */
    fun selectFileUri(activity: FragmentActivity, mimeTypes: Array<String>?): Observable<Uri> {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.type = "*/*"
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
        return activity.requestPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            .compose(
                RxPermissionTransformer(
                    activity,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
            )
            .flatMap(object : Function<Boolean, ObservableSource<Uri>> {
                @Throws(Throwable::class)
                override fun apply(aBoolean: Boolean): ObservableSource<Uri> {
                    return activity.startActivityForResult(intent)
                        .flatMap(object : Function<ActivityResult, ObservableSource<Uri>> {
                            @Throws(Throwable::class)
                            override fun apply(activityResult: ActivityResult): ObservableSource<Uri> {
                                return if (!activityResult.isOk) {
                                    Observable.empty()
                                } else Observable.just(
                                    activityResult.data!!.data!!
                                )
                            }
                        })
                        .observeOn(AndroidSchedulers.mainThread())
                }
            })
    }

    /**
     * 选择文件
     * Intent.ACTION_OPEN_DOCUMENT 只 支持 图片 文档 视频 音频 意图[在锤子手机上不能选择pdf]
     * 注意！！ 华为mate20x【【Intent.ACTION_GET_CONTENT 有bug 不能选择具体类型】】 目前没有很好的解决方案 建议自己做文件选择器
     *
     * @param activity
     * @param mimeTypes {"image/ *", "text/ *"}; 每个元素只能是单一的
     * @return
     */
    @JvmOverloads
    fun selectFile(
        activity: FragmentActivity,
        mimeTypes: Array<String> = arrayOf("*/*")
    ): Observable<String> {
        return selectFileUri(activity, mimeTypes)
            .observeOn(Schedulers.io())
            .map { uri -> (UriUtils.getPath(activity, uri))!! }
            .map(object : Function<String, String> {
                @Throws(Throwable::class)
                override fun apply(s: String): String {
                    /**
                     * fix:华为手机【【Intent.ACTION_GET_CONTENT 】 还能跳转到文件管理器选择其他类型的文件
                     */
                    val mimeTypeMap = HashMap<String, HashSet<String>?>()
                    for (mimeType: String in mimeTypes) {
                        val split = mimeType.split("/".toRegex()).dropLastWhile { it.isEmpty() }
                            .toTypedArray()
                        if (mimeTypeMap[split[0]] == null) {
                            mimeTypeMap[split[0]] = HashSet()
                        }
                        mimeTypeMap[split[0]]!!.add(split[1])
                    }
                    if (mimeTypeMap["*"] != null) {
                        return s
                    }
                    val fileMimeType = FileUtils.getMimeType(s)
                    val fileMimeTypeArray =
                        fileMimeType!!.split("/".toRegex()).dropLastWhile { it.isEmpty() }
                            .toTypedArray()
                    val supportTypes = mimeTypeMap[fileMimeTypeArray[0]]
                    if (supportTypes != null && (supportTypes.contains("*") || supportTypes.contains(
                            fileMimeTypeArray[1]
                        ))
                    ) {
                        return s
                    }
                    throw FileNotMatchTypeException(
                        "file not match type " + Arrays.toString(
                            mimeTypes
                        )
                    )
                }
            })
            .observeOn(AndroidSchedulers.mainThread())
    }

    /**
     * 批量选择文件
     *
     * @param activity
     * @return
     */
    fun selectMultipleFileUri(activity: FragmentActivity): Observable<List<Uri>> {
        val intent = Intent(Intent.ACTION_GET_CONTENT) //意图：文件浏览器
        intent.type = "*/*" //无类型限制
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true) //关键！多选参数
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        return activity.requestPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            .compose(
                RxPermissionTransformer(
                    activity,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
            )
            .flatMap(object : Function<Boolean, ObservableSource<List<Uri>>> {
                @Throws(Throwable::class)
                override fun apply(aBoolean: Boolean): ObservableSource<List<Uri>> {
                    return activity.startActivityForResult(intent)
                        .flatMap(object :
                            Function<ActivityResult, ObservableSource<List<Uri>>> {
                            @Throws(Throwable::class)
                            override fun apply(activityResult: ActivityResult): ObservableSource<List<Uri>> {
                                if (!activityResult.isOk) {
                                    return Observable.empty()
                                }
                                if (activityResult.data!!.data != null) {
                                    //单次点击未使用多选的情况
                                    val uris = ArrayList<Uri>()
                                    activityResult.data!!.data?.let { uris.add(it) }
                                    return Observable.just(uris)
                                }
                                //长按使用多选的情况
                                val clipData = activityResult.data!!.clipData
                                val uris = ArrayList<Uri>()
                                if (clipData != null) {
                                    for (i in 0 until clipData.itemCount) {
                                        val itemAt = clipData.getItemAt(i)
                                        if (itemAt != null && itemAt.uri != null) {
                                            uris.add(itemAt.uri)
                                        }
                                    }
                                }
                                return Observable.just(uris)
                            }
                        })
                        .observeOn(AndroidSchedulers.mainThread())
                }
            })
    }

    /**
     * 批量选择文件
     * @param activity
     * @return
     */
    fun selectMultipleFile(activity: FragmentActivity): Observable<List<String>> {
        return selectMultipleFileUri(activity)
            .observeOn(Schedulers.io())
            .map { uris ->
                val paths: MutableList<String> = ArrayList()
                for (i in uris.indices) {
                    UriUtils.getPath(activity, uris[i])?.let { paths.add(it) }
                }
                paths
            }
    }

    /**
     * 隐藏软键盘
     *
     * @param act
     */
    fun hideSoftKeyBoard(act: Activity) {
        try {
            val imm = act.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            if (imm.isActive) {
                var view = act.currentFocus
                if (view == null) {
                    //dialogfragment 直接使用有问题
                    view = act.window.peekDecorView()
                }
                imm.hideSoftInputFromWindow(view!!.windowToken, 0)
            }
        } catch (e: Exception) {
        }
    }

    /**
     * 隐藏软键盘
     *
     * @param act
     */
    @JvmStatic
    fun hideSoftKeyBoard(act: Activity, clearFocus: Boolean) {
        try {
            val imm = act.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            if (imm.isActive) {
                var view = act.currentFocus
                if (view == null) {
                    //dialogfragment 直接使用有问题
                    view = act.window.peekDecorView()
                }
                imm.hideSoftInputFromWindow(view!!.windowToken, 0)
            }
            if (clearFocus) {
                val currentFocus = act.currentFocus
                currentFocus?.clearFocus()
            }
        } catch (e: Exception) {
        }
    }

    /**
     * 隐藏软键盘2
     *
     * @param act
     */
    fun hideSoftKeyBoard(act: Context, v: View) {
        try {
            val imm = act.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            if (imm.isActive) {
                imm.hideSoftInputFromWindow(v.windowToken, 0)
            }
        } catch (e: Exception) {
        }
    }

    /**
     * 隐藏软键盘2
     *
     * @param act
     */
    fun hideSoftKeyBoard(act: Context, v: View?, clearFocus: Boolean) {
        try {
            if (v != null && clearFocus) {
                v.clearFocus()
            }
            val imm = act.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(v!!.windowToken, 0)
        } catch (e: Exception) {
        }
    }

    /**
     * 显示软键盘
     *
     * @param act
     */
    fun showSoftKeyBoard(act: Activity) {
        try {
            val imm = act.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(act.currentFocus, InputMethodManager.SHOW_IMPLICIT)
        } catch (e: Exception) {
        }
    }

    /**
     * 显示软键盘2
     *
     * @param act
     */
    fun showSoftKeyBoard(act: Context, v: View?) {
        try {
            if (v != null) {
                v.requestFocus()
                v.post(Runnable {
                    if (v != null) {
                        v.requestFocus()
                    }
                })
            }
            val imm = act.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(v, InputMethodManager.SHOW_IMPLICIT)
        } catch (e: Exception) {
        }
    }

    /**
     * 显示软键盘2
     *
     * @param act
     */
    fun showSoftKeyBoardForce(act: Context, v: View?) {
        try {
            if (v != null) {
                v.requestFocus()
                v.post(Runnable {
                    if (v != null) {
                        v.requestFocus()
                    }
                })
            }
            val imm = act.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(v, InputMethodManager.SHOW_FORCED)
        } catch (e: Exception) {
        }
    }

    /**
     * 复制到粘贴板
     *
     * @param lable
     * @param charSequence
     */
    fun copyTextToClipboard(lable: String?, charSequence: CharSequence) {
        val cmb =
            getLinkedApplication().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                ?: return
        //将内容放入粘贴管理器,在别的地方长按选择"粘贴"即可
        cmb.setPrimaryClip(ClipData.newPlainText(lable, charSequence))
    }

    /**
     * 复制到粘贴板
     *
     * @param charSequence
     */
    fun copyTextToClipboard(charSequence: CharSequence) {
        val cmb =
            getLinkedApplication().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                ?: return
        //将内容放入粘贴管理器,在别的地方长按选择"粘贴"即可
        cmb.setPrimaryClip(ClipData.newPlainText("text", charSequence))
    }

    val textFromClipboard: CharSequence?
        /**
         * 获取粘贴板内容
         *
         * @return
         */
        get() {
            val cmb: ClipboardManager = getLinkedApplication().getSystemService(
                Context.CLIPBOARD_SERVICE
            ) as ClipboardManager
            if (cmb != null) {
                if (cmb.hasPrimaryClip()) {
                    if (cmb.primaryClip!!.itemCount > 0) {
                        return cmb.primaryClip!!.getItemAt(0).text
                    }
                }
            }
            return null
        }

    /**
     * 监听软键盘是否点击了搜索／下一步／发送／完成／回车按钮
     * （在TextView.OnEditorActionListener的 onEditorAction(TextView v, int actionId, KeyEvent event)方法监听）
     *
     * @param actionId
     * @param event
     * @return
     */
    fun isSearchClick(actionId: Int, event: KeyEvent?): Boolean {
        return actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_NEXT || actionId == EditorInfo.IME_ACTION_SEND || actionId == EditorInfo.IME_ACTION_DONE || event != null && KeyEvent.KEYCODE_ENTER == event.keyCode && KeyEvent.ACTION_DOWN == event.action
    }

    /**
     * 分享文本 或者链接
     *
     * @param context
     * @param text          分享文本
     * @param componentName 指定 包名 空 会调用系统选择面板,否则调用指定的app  有
     *   SystemUtils.SHARE_QQ_FRIEND_COMPONENT
     *   SystemUtils.SHARE_WECHAT_FRIEND_COMPONENT
     *   SystemUtils.SHARE_WECHAT_CIRCLE_COMPONENT
     *   SystemUtils.SHARE_WEIBO_FRIEND_COMPONENT
     *   SystemUtils.SHARE_WEIBO_CIRCLE_COMPONENT
     * @return
     */
    fun shareText(
        context: Context,
        text: String?,
        componentName: ComponentName?
    ): Observable<ActivityResult> {
        val sendIntent = Intent()
        sendIntent.action = Intent.ACTION_SEND
        sendIntent.putExtra(Intent.EXTRA_TEXT, text)
        sendIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        try {
            val scheme = Uri.parse(text).scheme
            if (TextUtils.equals(scheme, "http") || TextUtils.equals(scheme, "https")) {
                sendIntent.type = "text/html"
            } else {
                sendIntent.type = "text/plain"
            }
        } catch (e: Throwable) {
            e.printStackTrace()
            sendIntent.type = "text/plain"
        }
        //微信必须支持这个
        sendIntent.putExtra(Intent.EXTRA_TITLE, "")
        sendIntent.putExtra("Kdescription", if (!TextUtils.isEmpty(text)) text else "")
        var chooser = sendIntent
        if (componentName != null) {
            //微信朋友圈 只支持图片
//            if (componentName.equals(SHARE_WECHAT_CIRCLE_COMPONENT)) {
//                PackageInfo installAppInfo = getInstallAppInfo(context, componentName.getPackageName());
//                Drawable drawable = installAppInfo.applicationInfo.loadIcon(context.getPackageManager());
//                File file = new File(context.getFilesDir(), "launcher.png");
//                BitmapUtils.INSTANCE.bitmapToFile(BitmapUtils.INSTANCE.drawableToBitmap(drawable), file);
//                Uri uriForFile = FileProvider7.INSTANCE.getUriForFile(context, file);
//                sendIntent.putExtra(Intent.EXTRA_STREAM, uriForFile);
//            }
            sendIntent.component = componentName
            chooser = sendIntent
        } else {
            chooser = Intent.createChooser(sendIntent, "share text")
        }
        return if (context is LifecycleOwner) {
            context.startActivityForResult(chooser)
        } else {
            val finalChooser = chooser
            Observable
                .fromCallable<ActivityResult> {
                    context.startActivity(finalChooser)
                    ActivityResult(Activity.RESULT_OK, Intent())
                }
        }
    }

    /**
     * 分享文件
     *
     * @param context
     * @param filePath      文件
     * 内部处理了 uri权限
     *    如果文件是私有目录一定要传递  authority 或者 if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
     * StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
     * StrictMode.setVmPolicy(builder.build());
     * }
     * @param componentName 指定 包名 空 会调用系统选择面板,否则调用指定的app
     *  有
     *      *   SystemUtils.SHARE_QQ_FRIEND_COMPONENT
     *      *   SystemUtils.SHARE_WECHAT_FRIEND_COMPONENT
     *      *   SystemUtils.SHARE_WECHAT_CIRCLE_COMPONENT
     *      *   SystemUtils.SHARE_WEIBO_FRIEND_COMPONENT
     *      *   SystemUtils.SHARE_WEIBO_CIRCLE_COMPONENT
     * @return
     */
    fun shareFile(
        context: Context,
        filePath: String?,
        componentName: ComponentName?
    ): Observable<ActivityResult> {
        return Observable.defer<ActivityResult> {
            val file = File(filePath)
            val intent = Intent(Intent.ACTION_SEND)
            val uri: Uri=FileProvider7.getUriForFile(getLinkedApplication(),FileUtils.getFileByPath(filePath))
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
            intent.putExtra(Intent.EXTRA_STREAM, uri)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            var fileType = FileUtils.getMimeType(filePath)
            if (TextUtils.isEmpty(fileType)) {
                val contentResolver = context.contentResolver
                if (contentResolver != null) {
                    fileType = contentResolver.getType(uri)
                }
            }
            if (TextUtils.isEmpty(fileType)) {
                fileType = "file/*"
            }
            intent.setDataAndType(uri, fileType)
            var chooser = intent
            if (componentName != null) {
                intent.component = componentName
                chooser = intent
            } else {
                chooser = Intent.createChooser(intent, "share text")
            }
            applyProviderPermission(context, intent, uri)
            if (context is LifecycleOwner) {
                context.startActivityForResult(chooser)
            } else {
                val finalChooser = chooser
                Observable
                    .fromCallable<ActivityResult> {
                        context.startActivity(finalChooser)
                        ActivityResult(Activity.RESULT_OK, Intent())
                    }
            }
        }
    }

    /**
     * 赋予保留服务权限
     *
     * @param context
     * @param intent
     * @param uri
     */
    fun applyProviderPermission(context: Context, intent: Intent?, uri: Uri?) {
        val resInfoList = context.packageManager
            .queryIntentActivities(intent!!, PackageManager.MATCH_DEFAULT_ONLY)
        for (resolveInfo in resInfoList) {
            val packageName = resolveInfo.activityInfo.packageName
            context.grantUriPermission(
                packageName,
                uri,
                Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION
            )
        }
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
    fun sendEmail(
        context: Context,
        email: String,
        subject: String?,
        content: String?,
        chooserAppTitle: String?
    ): Observable<ActivityResult> {
        return Observable.defer<ActivityResult> { // 必须明确使用mailto前缀来修饰邮件地址,如果使用
            // intent.putExtra(Intent.EXTRA_EMAIL, email)，结果将匹配不到任何应用
            val prefix = "mailto:"
            var uri: Uri? = null
            if (!TextUtils.isEmpty(email) && email.lowercase(Locale.getDefault())
                    .startsWith(prefix)
            ) {
                uri = Uri.parse(email)
            } else {
                uri = Uri.parse(prefix + email)
            }
            val emailArr = arrayOf(email)
            val intent = Intent(Intent.ACTION_SENDTO, uri)
            intent.putExtra(Intent.EXTRA_CC, emailArr) // 抄送人
            intent.putExtra(Intent.EXTRA_SUBJECT, subject) // 主题
            intent.putExtra(Intent.EXTRA_TEXT, content) // 正文
            val chooser = Intent.createChooser(intent, chooserAppTitle)
            if (context is LifecycleOwner) {
                context.startActivityForResult(chooser)
            } else {
                Observable
                    .fromCallable<ActivityResult>(Callable<ActivityResult> {
                        context.startActivity(chooser)
                        ActivityResult(Activity.RESULT_OK, Intent())
                    })
            }
        }
    }

    class PathCropIntentBuilder(var outPutPath: String) : CropIntentBuilder() {
        init {
            val file = File(outPutPath)
            if (!file.exists()) {
                try {
                    file.createNewFile()
                } catch (e: Throwable) {
                    e.printStackTrace()
                }
            }
            outputFile(file)
        }
    }

    open class CropIntentBuilder {
        private val mCropIntent = Intent("com.android.camera.action.CROP")

        init {
            //默认值 不能修改!!!!!
            mCropIntent.putExtra("crop", true)
            mCropIntent.putExtra("outputX", 320)
            mCropIntent.putExtra("outputY", 320)
            mCropIntent.putExtra("scale", true)
            mCropIntent.putExtra("aspectX", 1)
            mCropIntent.putExtra("aspectY", 1)
            mCropIntent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString())
            mCropIntent.putExtra("noFaceDetection", true)
            mCropIntent.putExtra("scale", true)
            mCropIntent.putExtra("scaleUpIfNeeded", true)
        }

        fun inputImgUri(inImgUri: Uri?): CropIntentBuilder {
            mCropIntent.setDataAndType(inImgUri, "image/*")
            return this
        }

        fun inputImgFile(inImgFile: File?): CropIntentBuilder {
            val inImgUri = getUriForFile(getLinkedApplication(), inImgFile)
            mCropIntent.setDataAndType(inImgUri, "image/*")
            return this
        }

        fun aspectXY(x: Int, y: Int): CropIntentBuilder {
            mCropIntent.putExtra("aspectX", x)
            mCropIntent.putExtra("aspectY", y)
            return this
        }

        fun outputXY(x: Int, y: Int): CropIntentBuilder {
            mCropIntent.putExtra("outputX", x)
            mCropIntent.putExtra("outputY", y)
            return this
        }

        fun outputBitmap(): CropIntentBuilder {
            mCropIntent.putExtra("return-data", true)
            return this
        }

        fun outputUri(outputImgUri: Uri?): CropIntentBuilder {
            mCropIntent.putExtra("return-data", false)
            mCropIntent.putExtra("output", outputImgUri)
            mCropIntent.clipData = ClipData.newRawUri(null, outputImgUri)
            mCropIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION)
            return this
        }

        fun outputFile(outputImgFile: File?): CropIntentBuilder {
            mCropIntent.putExtra("return-data", false)
            val outImgUri = getUriForFile(getLinkedApplication(), outputImgFile)
            mCropIntent.putExtra("output", outImgUri)
            mCropIntent.clipData = ClipData.newRawUri(null, outImgUri)
            mCropIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION)
            return this
        }

        fun build(): Intent {
            return mCropIntent
        }
    }
}