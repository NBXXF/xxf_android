package com.xxf.media.album.repo

import android.Manifest
import android.provider.MediaStore
import androidx.core.content.ContentResolverCompat
import androidx.fragment.app.FragmentActivity
import com.xxf.media.album.internal.entity.Item
import com.xxf.permission.requestPermission
import com.xxf.permission.transformer.RxPermissionTransformer
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.*
import java.util.concurrent.Callable

/**
 * @Author: XGod  xuanyouwu@163.com  17611639080
 * Date: 3/8/21 6:52 PM
 * Description: 相册
 */
object AlbumService {
    // ===========================================================
    // === params for album ALL && showSingleMediaType: true ===
    private const val SELECTION_ALL_FOR_SINGLE_MEDIA_TYPE =
        (MediaStore.Files.FileColumns.MEDIA_TYPE + "=?"
                + " AND " + MediaStore.MediaColumns.SIZE + ">0")
    private val QUERY_URI = MediaStore.Files.getContentUri("external")
    private val PROJECTION = arrayOf(
        MediaStore.Files.FileColumns._ID,
        MediaStore.MediaColumns.DISPLAY_NAME,
        MediaStore.MediaColumns.MIME_TYPE,
        MediaStore.MediaColumns.SIZE,
        "duration"
    )

    // === params for album ALL && showSingleMediaType: false ===
    private const val SELECTION_ALL = ("(" + MediaStore.Files.FileColumns.MEDIA_TYPE + "=?"
            + " OR "
            + MediaStore.Files.FileColumns.MEDIA_TYPE + "=?)"
            + " AND " + MediaStore.MediaColumns.SIZE + ">0")
    private val SELECTION_ALL_ARGS = arrayOf(
        MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE.toString(),
        MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO.toString()
    )

    // ===============================================================
    private const val ORDER_BY = MediaStore.Images.Media.DATE_TAKEN + " DESC"


    /**
     * 获取相册所有图片和视频
     */
    fun getAlbum(context: FragmentActivity): Observable<List<Item>> {
        return Observable
            .defer<Boolean> {
                context.requestPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    .compose(
                        RxPermissionTransformer(
                            context,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                        )
                    )
            }
            .subscribeOn(AndroidSchedulers.mainThread())
            .flatMap {
                Observable
                    .fromCallable<List<Item>>(object : Callable<List<Item>> {
                        override fun call(): List<Item> {
                            val items: MutableList<Item> = ArrayList()
                            val selection = SELECTION_ALL
                            val selectionArgs = SELECTION_ALL_ARGS
                            val cursor = ContentResolverCompat.query(
                                context.getContentResolver(),
                                QUERY_URI, PROJECTION, selection, selectionArgs, ORDER_BY, null
                            );
                            try {
                                if (cursor != null) {
                                    while (cursor.moveToNext()) {
                                        items.add(Item.valueOf(cursor))
                                    }
                                }
                            } finally {
                                try {
                                    cursor?.close();
                                } catch (e: Throwable) {
                                    e.printStackTrace()
                                }
                            }
                            return items;
                        }

                    })
                    .subscribeOn(Schedulers.io());
            }
    }

    /**
     * 获取所有图片
     * 自动请求权限
     */
    fun getImages(context: FragmentActivity): Observable<List<Item>> {
        return context.requestPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            .compose(RxPermissionTransformer(context, Manifest.permission.WRITE_EXTERNAL_STORAGE))
            .subscribeOn(AndroidSchedulers.mainThread())
            .flatMap {
                Observable
                    .fromCallable<List<Item>>(object : Callable<List<Item>> {
                        override fun call(): List<Item> {
                            val items: MutableList<Item> = ArrayList()
                            val selection = SELECTION_ALL_FOR_SINGLE_MEDIA_TYPE
                            val selectionArgs =
                                arrayOf<String>(MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE.toString());
                            val cursor = ContentResolverCompat.query(
                                context.getContentResolver(),
                                QUERY_URI, PROJECTION, selection, selectionArgs, ORDER_BY, null
                            );
                            try {
                                if (cursor != null) {
                                    while (cursor.moveToNext()) {
                                        items.add(Item.valueOf(cursor))
                                    }
                                }
                            } finally {
                                try {
                                    cursor?.close();
                                } catch (e: Throwable) {
                                    e.printStackTrace()
                                }
                            }
                            return items;
                        }

                    })
                    .subscribeOn(Schedulers.io());
            }
    }

    /**
     * 获取所有视频
     * 自动请求权限
     */
    fun getVideos(context: FragmentActivity): Observable<List<Item>> {
        return Observable
            .defer<Boolean> {
                context.requestPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    .compose(
                        RxPermissionTransformer(
                            context,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                        )
                    )
            }
            .subscribeOn(AndroidSchedulers.mainThread())
            .flatMap {
                Observable
                    .fromCallable<List<Item>>(object : Callable<List<Item>> {
                        override fun call(): List<Item> {
                            val items: MutableList<Item> = ArrayList()
                            val selection = SELECTION_ALL_FOR_SINGLE_MEDIA_TYPE
                            val selectionArgs =
                                arrayOf<String>(MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO.toString());
                            val cursor = ContentResolverCompat.query(
                                context.getContentResolver(),
                                QUERY_URI, PROJECTION, selection, selectionArgs, ORDER_BY, null
                            );
                            try {
                                if (cursor != null) {
                                    while (cursor.moveToNext()) {
                                        items.add(Item.valueOf(cursor))
                                    }
                                }
                            } finally {
                                try {
                                    cursor?.close();
                                } catch (e: Throwable) {
                                    e.printStackTrace()
                                }
                            }
                            return items;
                        }
                    })
                    .subscribeOn(Schedulers.io());
            }
    }
}