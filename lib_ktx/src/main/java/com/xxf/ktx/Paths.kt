@file:Suppress("unused")

package com.xxf.ktx

import android.content.Context
import android.os.Environment


/********************************外部私有文件夹开始***************************************/
/**
 * 读取/sdcard/Android/data/app包名/cache目录。没有创建。卸载删除,需要读写权限的
 */
inline val Context.externalCacheDirPath: String?
    get() = externalCacheDir?.absolutePath

/**
 * 读取/sdcard/Android/data/app包名/files目录。没有创建。卸载删除,需要读写权限的
 */
inline val Context.externalFilesDirPath: String?
    get() = getExternalFilesDir(null)?.absolutePath

/**
 * 读取/sdcard/Android/data/app包名/Pictures。没有创建。卸载删除,需要读写权限的
 */
inline val Context.externalPicturesDirPath: String?
    get() = getExternalFilesDir(Environment.DIRECTORY_PICTURES)?.absolutePath

/**
 * 读取/sdcard/Android/data/app包名/Movies。没有创建。卸载删除,需要读写权限的
 */
inline val Context.externalMoviesDirPath: String?
    get() = getExternalFilesDir(Environment.DIRECTORY_MOVIES)?.absolutePath

/**
 * 读取/sdcard/Android/data/app包名/Download。没有创建。卸载删除,需要读写权限的
 */
inline val Context.externalDownloadsDirPath: String?
    get() = getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)?.absolutePath

/**
 * 读取/sdcard/Android/data/app包名/Documents。没有创建。卸载删除,需要读写权限的
 */
inline val Context.externalDocumentsDirPath: String?
    get() = getFileStreamPath(Environment.DIRECTORY_DOCUMENTS)?.absolutePath

/**
 * 读取/sdcard/Android/data/app包名/Music。没有创建。卸载删除,需要读写权限的
 */
inline val Context.externalMusicDirPath: String?
    get() = getExternalFilesDir(Environment.DIRECTORY_MUSIC)?.absolutePath

/**
 * 读取/sdcard/Android/data/app包名/Podcasts。没有创建。卸载删除,需要读写权限的
 */
inline val Context.externalPodcastsDirPath: String?
    get() = getExternalFilesDir(Environment.DIRECTORY_PODCASTS)?.absolutePath

/**
 * 读取/sdcard/Android/data/app包名/Ringtones。没有创建。卸载删除,需要读写权限的
 */
inline val Context.externalRingtonesDirPath: String?
    get() = getExternalFilesDir(Environment.DIRECTORY_RINGTONES)?.absolutePath

/**
 * 读取/sdcard/Android/data/app包名/Alarms。没有创建。卸载删除,需要读写权限的
 */
inline val Context.externalAlarmsDirPath: String?
    get() = getExternalFilesDir(Environment.DIRECTORY_ALARMS)?.absolutePath

/**
 * 读取/sdcard/Android/data/app包名/Notifications。没有创建。卸载删除,需要读写权限的
 */
inline val Context.externalNotificationsDirPath: String?
    get() = getExternalFilesDir(Environment.DIRECTORY_NOTIFICATIONS)?.absolutePath
/********************************外部私有文件夹结束***************************************/


/********************************外部公共文件夹开始***************************************/
/**
 * 可以获取外部存储的根目录/sdcard。可能有下面的结果。这个方法在android10废弃，取代的是Context的getExternalFilesDir(String type)
 * 需要读写权限的
 */
inline val Context.externalPublicRootDirPath: String?
    get() = Environment.getExternalStorageDirectory().absolutePath

/**
 * 可以获取外部存储的根目录/sdcard/Pictures。可能有下面的结果。这个方法在android10废弃，取代的是Context的getExternalFilesDir(String type)
 * 需要读写权限的
 */
inline val Context.externalPublicPicturesDirPath: String?
    get() = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).absolutePath

/**
 * 可以获取外部存储的根目录/sdcard/Movies。可能有下面的结果。这个方法在android10废弃，取代的是Context的getExternalFilesDir(String type)
 * 需要读写权限的
 */
inline val Context.externalPublicMoviesDirPath: String?
    get() = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES)?.absolutePath

/**
 * 可以获取外部存储的根目录/sdcard/Download。可能有下面的结果。这个方法在android10废弃，取代的是Context的getExternalFilesDir(String type)
 * 需要读写权限的
 */
inline val Context.externalPublicDownloadsDirPath: String?
    get() = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)?.absolutePath

/**
 * 可以获取外部存储的根目录/sdcard/Documents。可能有下面的结果。这个方法在android10废弃，取代的是Context的getExternalFilesDir(String type)
 * 需要读写权限的
 */
inline val Context.externalPublicDocumentsDirPath: String?
    get() = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)?.absolutePath

/**
 * 可以获取外部存储的根目录/sdcard/Music。可能有下面的结果。这个方法在android10废弃，取代的是Context的getExternalFilesDir(String type)
 * 需要读写权限的
 */
inline val Context.externalPublicMusicDirPath: String?
    get() = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC)?.absolutePath

/**
 * 可以获取外部存储的根目录/sdcard/Podcasts。可能有下面的结果。这个方法在android10废弃，取代的是Context的getExternalFilesDir(String type)
 * 需要读写权限的
 */
inline val Context.externalPublicPodcastsDirPath: String?
    get() = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PODCASTS)?.absolutePath

/**
 * 可以获取外部存储的根目录/sdcard/Ringtones。可能有下面的结果。这个方法在android10废弃，取代的是Context的getExternalFilesDir(String type)
 * 需要读写权限的
 */
inline val Context.externalPublicRingtonesDirPath: String?
    get() = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_RINGTONES)?.absolutePath

/**
 * 可以获取外部存储的根目录/sdcard/Alarms。可能有下面的结果。这个方法在android10废弃，取代的是Context的getExternalFilesDir(String type)
 * 需要读写权限的
 */
inline val Context.externalPublicAlarmsDirPath: String?
    get() = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_ALARMS)?.absolutePath

/**
 * 可以获取外部存储的根目录/sdcard/Notifications。可能有下面的结果。这个方法在android10废弃，取代的是Context的getExternalFilesDir(String type)
 * 需要读写权限的
 */
inline val Context.externalPublicNotificationsDirPath: String?
    get() = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_NOTIFICATIONS)?.absolutePath
/********************************外部公共文件夹结束***************************************/


/********************************内部私有文件夹开始***************************************/
/**
 * 获取/data/data/app包名/cache文件夹，默认已经存在。在这个文件夹下面的读写操作都不需要申请读写权限，因为默认已经有了。
 */
inline val Context.internalCacheDirPath: String
    get() = cacheDir.absolutePath

/**
 * 获取/data/data/app包名/files文件夹，默认已经存在。在这个文件夹下面的读写操作都不需要申请读写权限，因为默认已经有了。
 */
inline val Context.internalFileDirPath: String
    get() = filesDir.absolutePath

/**
 * 获取/data/data/app包名/files/Pictures，默认已经存在。在这个文件夹下面的读写操作都不需要申请读写权限，因为默认已经有了。
 */
inline val Context.internalPicturesDirPath: String?
    get() = getFileStreamPath(Environment.DIRECTORY_PICTURES)?.absolutePath

/**
 * 获取/data/data/app包名/files/Movies，默认已经存在。在这个文件夹下面的读写操作都不需要申请读写权限，因为默认已经有了。
 */
inline val Context.internalMoviesDirPath: String?
    get() = getFileStreamPath(Environment.DIRECTORY_MOVIES)?.absolutePath

/**
 * 获取/data/data/app包名/files/Download，默认已经存在。在这个文件夹下面的读写操作都不需要申请读写权限，因为默认已经有了。
 */
inline val Context.internalDownloadsDirPath: String?
    get() = getFileStreamPath(Environment.DIRECTORY_DOWNLOADS)?.absolutePath

/**
 * 获取/data/data/app包名/files/Documents，默认已经存在。在这个文件夹下面的读写操作都不需要申请读写权限，因为默认已经有了。
 */
inline val Context.internalDocumentsDirPath: String?
    get() = getFileStreamPath(Environment.DIRECTORY_DOCUMENTS)?.absolutePath

/**
 * 获取/data/data/app包名/files/Music，默认已经存在。在这个文件夹下面的读写操作都不需要申请读写权限，因为默认已经有了。
 */
inline val Context.internalMusicDirPath: String?
    get() = getFileStreamPath(Environment.DIRECTORY_MUSIC)?.absolutePath

/**
 * 获取/data/data/app包名/files/Podcasts，默认已经存在。在这个文件夹下面的读写操作都不需要申请读写权限，因为默认已经有了。
 */
inline val Context.internalPodcastsDirPath: String?
    get() = getFileStreamPath(Environment.DIRECTORY_PODCASTS)?.absolutePath

/**
 * 获取/data/data/app包名/files/Ringtones，默认已经存在。在这个文件夹下面的读写操作都不需要申请读写权限，因为默认已经有了。
 */
inline val Context.internalRingtonesDirPath: String?
    get() = getFileStreamPath(Environment.DIRECTORY_RINGTONES)?.absolutePath

/**
 * 获取/data/data/app包名/files/Alarms，默认已经存在。在这个文件夹下面的读写操作都不需要申请读写权限，因为默认已经有了。
 */
inline val Context.internalAlarmsDirPath: String?
    get() = getFileStreamPath(Environment.DIRECTORY_ALARMS)?.absolutePath

/**
 * 获取/data/data/app包名/files/Notifications，默认已经存在。在这个文件夹下面的读写操作都不需要申请读写权限，因为默认已经有了。
 */
inline val Context.internalNotificationsDirPath: String?
    get() = getFileStreamPath(Environment.DIRECTORY_NOTIFICATIONS)?.absolutePath

/********************************内部私有文件夹结束***************************************/


/**
 * 检查包含外部存储的卷是否可供读取和写入。
 */
inline val Context.isExternalStorageWritable: Boolean
    get() = Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED

/**
 * 检查包含外部存储的卷是否至少可供读取。
 */
inline val Context.isExternalStorageReadable: Boolean
    get() = Environment.getExternalStorageState() in setOf(
        Environment.MEDIA_MOUNTED,
        Environment.MEDIA_MOUNTED_READ_ONLY
    )

/**
 * 检查包含外部存储的卷是否可移动。
 */
inline val Context.isExternalStorageRemovable: Boolean
    get() = Environment.isExternalStorageRemovable()

/**
 * 外部sd卡是否挂载
 */
inline val Context.isExternalStorageEmulated: Boolean
    get() = Environment.isExternalStorageEmulated()
