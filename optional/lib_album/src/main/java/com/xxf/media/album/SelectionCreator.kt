/*
 * Copyright (C) 2014 nohana, Inc.
 * Copyright 2017 Zhihu Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an &quot;AS IS&quot; BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.xxf.media.album

import android.Manifest
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Build
import androidx.activity.result.ActivityResult
import androidx.annotation.IntDef
import androidx.annotation.RequiresApi
import androidx.annotation.StyleRes
import com.xxf.activityresult.isOk
import com.xxf.activityresult.startActivityForResult
import com.xxf.fileprovider.FileProvider7
import com.xxf.media.album.engine.ImageEngine
import com.xxf.media.album.filter.Filter
import com.xxf.media.album.internal.entity.CaptureStrategy
import com.xxf.media.album.internal.entity.SelectionSpec
import com.xxf.media.album.listener.OnCheckedListener
import com.xxf.media.album.listener.OnSelectedListener
import com.xxf.media.album.ui.AlbumActivity
import com.xxf.permission.requestPermission
import com.xxf.permission.transformer.RxPermissionTransformer
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableSource
import io.reactivex.rxjava3.functions.Supplier

/**
 * Fluent API for building media select specification.
 */
@Suppress("unused")
class SelectionCreator internal constructor(
    private val mMatisse: AlbumLauncher,
    mimeTypes: Set<MimeType?>,
    mediaTypeExclusive: Boolean
) {
    private val mSelectionSpec: SelectionSpec

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @IntDef(
        ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED,
        ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE,
        ActivityInfo.SCREEN_ORIENTATION_PORTRAIT,
        ActivityInfo.SCREEN_ORIENTATION_USER,
        ActivityInfo.SCREEN_ORIENTATION_BEHIND,
        ActivityInfo.SCREEN_ORIENTATION_SENSOR,
        ActivityInfo.SCREEN_ORIENTATION_NOSENSOR,
        ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE,
        ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT,
        ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE,
        ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT,
        ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR,
        ActivityInfo.SCREEN_ORIENTATION_USER_LANDSCAPE,
        ActivityInfo.SCREEN_ORIENTATION_USER_PORTRAIT,
        ActivityInfo.SCREEN_ORIENTATION_FULL_USER,
        ActivityInfo.SCREEN_ORIENTATION_LOCKED
    )
    @Retention(
        AnnotationRetention.SOURCE
    )
    internal annotation class ScreenOrientation

    /**
     * Constructs a new specification builder on the context.
     *
     * @param matisse   a requester context wrapper.
     * @param mimeTypes MIME type set to select.
     */
    init {
        mSelectionSpec = SelectionSpec.getCleanInstance()
        //默认增加授权
        mSelectionSpec.captureStrategy= CaptureStrategy(false,FileProvider7.getAuthority(mMatisse.activity!!))
        mSelectionSpec.mimeTypeSet = mimeTypes
        mSelectionSpec.mediaTypeExclusive = mediaTypeExclusive
        mSelectionSpec.orientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
    }

    /**
     * Whether to show only one media type if choosing medias are only images or videos.
     *
     * @param showSingleMediaType whether to show only one media type, either images or videos.
     * @return [SelectionCreator] for fluent API.
     * @see SelectionSpec.onlyShowImages
     * @see SelectionSpec.onlyShowVideos
     */
    fun showSingleMediaType(showSingleMediaType: Boolean): SelectionCreator {
        mSelectionSpec.showSingleMediaType = showSingleMediaType
        return this
    }

    /**
     * Theme for media selecting Activity.
     *
     *
     * There are two built-in themes:
     * 1. com.xxf.media.album.R.style.Matisse_Zhihu;
     * 2. com.xxf.media.album.R.style.Matisse_Dracula
     * you can define a custom theme derived from the above ones or other themes.
     *
     * @param themeId theme resource id. Default value is com.xxf.media.album.R.style.Matisse_Zhihu.
     * @return [SelectionCreator] for fluent API.
     */
    fun theme(@StyleRes themeId: Int): SelectionCreator {
        mSelectionSpec.themeId = themeId
        return this
    }

    /**
     * Show a auto-increased number or a check mark when user select media.
     *
     * @param countable true for a auto-increased number from 1, false for a check mark. Default
     * value is false.
     * @return [SelectionCreator] for fluent API.
     */
    fun countable(countable: Boolean): SelectionCreator {
        mSelectionSpec.countable = countable
        return this
    }

    /**
     * Maximum selectable count.
     *
     * @param maxSelectable Maximum selectable count. Default value is 1.
     * @return [SelectionCreator] for fluent API.
     */
    fun maxSelectable(maxSelectable: Int): SelectionCreator {
        require(maxSelectable >= 1) { "maxSelectable must be greater than or equal to one" }
        check(!(mSelectionSpec.maxImageSelectable > 0 || mSelectionSpec.maxVideoSelectable > 0)) { "already set maxImageSelectable and maxVideoSelectable" }
        mSelectionSpec.maxSelectable = maxSelectable
        return this
    }

    /**
     * Only useful when [SelectionSpec.mediaTypeExclusive] set true and you want to set different maximum
     * selectable files for image and video media types.
     *
     * @param maxImageSelectable Maximum selectable count for image.
     * @param maxVideoSelectable Maximum selectable count for video.
     * @return [SelectionCreator] for fluent API.
     */
    fun maxSelectablePerMediaType(
        maxImageSelectable: Int,
        maxVideoSelectable: Int
    ): SelectionCreator {
        require(!(maxImageSelectable < 1 || maxVideoSelectable < 1)) { "max selectable must be greater than or equal to one" }
        mSelectionSpec.maxSelectable = -1
        mSelectionSpec.maxImageSelectable = maxImageSelectable
        mSelectionSpec.maxVideoSelectable = maxVideoSelectable
        return this
    }

    /**
     * Add filter to filter each selecting item.
     *
     * @param filter [Filter]
     * @return [SelectionCreator] for fluent API.
     */
    fun addFilter(filter: Filter): SelectionCreator {
        if (mSelectionSpec.filters == null) {
            mSelectionSpec.filters = ArrayList()
        }
        requireNotNull(filter) { "filter cannot be null" }
        mSelectionSpec.filters.add(filter)
        return this
    }

    /**
     * Determines whether the photo capturing is enabled or not on the media grid view.
     *
     *
     * If this value is set true, photo capturing entry will appear only on All Media's page.
     *
     * @param enable Whether to enable capturing or not. Default value is false;
     * @return [SelectionCreator] for fluent API.
     */
    fun capture(enable: Boolean): SelectionCreator {
        mSelectionSpec.capture = enable
        return this
    }

    /**
     * Show a original photo check options.Let users decide whether use original photo after select
     *
     * @param enable Whether to enable original photo or not
     * @return [SelectionCreator] for fluent API.
     */
    fun originalEnable(enable: Boolean): SelectionCreator {
        mSelectionSpec.originalable = enable
        return this
    }

    /**
     * Determines Whether to hide top and bottom toolbar in PreView mode ,when user tap the picture
     *
     * @param enable
     * @return [SelectionCreator] for fluent API.
     */
    fun autoHideToolbarOnSingleTap(enable: Boolean): SelectionCreator {
        mSelectionSpec.autoHideToobar = enable
        return this
    }

    /**
     * Maximum original size,the unit is MB. Only useful when {link@originalEnable} set true
     *
     * @param size Maximum original size. Default value is Integer.MAX_VALUE
     * @return [SelectionCreator] for fluent API.
     */
    fun maxOriginalSize(size: Int): SelectionCreator {
        mSelectionSpec.originalMaxSize = size
        return this
    }

    /**
     * Capture strategy provided for the location to save photos including internal and external
     * storage and also a authority for [androidx.core.content.FileProvider].
     *
     * @param captureStrategy [CaptureStrategy], needed only when capturing is enabled.
     * @return [SelectionCreator] for fluent API.
     */
    fun captureStrategy(captureStrategy: CaptureStrategy?): SelectionCreator {
        mSelectionSpec.captureStrategy = captureStrategy
        return this
    }

    /**
     * Set the desired orientation of this activity.
     *
     * @param orientation An orientation constant as used in [ScreenOrientation].
     * Default value is [android.content.pm.ActivityInfo.SCREEN_ORIENTATION_PORTRAIT].
     * @return [SelectionCreator] for fluent API.
     * @see Activity.setRequestedOrientation
     */
    fun restrictOrientation(@ScreenOrientation orientation: Int): SelectionCreator {
        mSelectionSpec.orientation = orientation
        return this
    }

    /**
     * Set a fixed span count for the media grid. Same for different screen orientations.
     *
     *
     * This will be ignored when [.gridExpectedSize] is set.
     *
     * @param spanCount Requested span count.
     * @return [SelectionCreator] for fluent API.
     */
    fun spanCount(spanCount: Int): SelectionCreator {
        require(spanCount >= 1) { "spanCount cannot be less than 1" }
        mSelectionSpec.spanCount = spanCount
        return this
    }

    /**
     * Set expected size for media grid to adapt to different screen sizes. This won't necessarily
     * be applied cause the media grid should fill the view container. The measured media grid's
     * size will be as close to this value as possible.
     *
     * @param size Expected media grid size in pixel.
     * @return [SelectionCreator] for fluent API.
     */
    fun gridExpectedSize(size: Int): SelectionCreator {
        mSelectionSpec.gridExpectedSize = size
        return this
    }

    /**
     * Photo thumbnail's scale compared to the View's size. It should be a float value in (0.0,
     * 1.0].
     *
     * @param scale Thumbnail's scale in (0.0, 1.0]. Default value is 0.5.
     * @return [SelectionCreator] for fluent API.
     */
    fun thumbnailScale(scale: Float): SelectionCreator {
        require(!(scale <= 0f || scale > 1f)) { "Thumbnail scale must be between (0.0, 1.0]" }
        mSelectionSpec.thumbnailScale = scale
        return this
    }

    /**
     * Provide an image engine.
     *
     *
     * There are two built-in image engines:
     * 1. [com.xxf.media.album.engine.impl.GlideEngine]
     * 2. [com.xxf.media.album.engine.impl.PicassoEngine]
     * And you can implement your own image engine.
     *
     * @param imageEngine [ImageEngine]
     * @return [SelectionCreator] for fluent API.
     */
    fun imageEngine(imageEngine: ImageEngine?): SelectionCreator {
        mSelectionSpec.imageEngine = imageEngine
        return this
    }

    /**
     * Set listener for callback immediately when user select or unselect something.
     *
     *
     * It's a redundant API with [AlbumLauncher.obtainResult],
     * we only suggest you to use this API when you need to do something immediately.
     *
     * @param listener [OnSelectedListener]
     * @return [SelectionCreator] for fluent API.
     */
    fun setOnSelectedListener(listener: OnSelectedListener?): SelectionCreator {
        mSelectionSpec.onSelectedListener = listener
        return this
    }

    /**
     * Set listener for callback immediately when user check or uncheck original.
     *
     * @param listener [OnSelectedListener]
     * @return [SelectionCreator] for fluent API.
     */
    fun setOnCheckedListener(listener: OnCheckedListener?): SelectionCreator {
        mSelectionSpec.onCheckedListener = listener
        return this
    }

    /**
     * Start to select media and wait for result.
     *
     * @return
     */
    fun forResult(): Observable<AlbumResult> {
        val activity = mMatisse.activity ?: return Observable.error(NullPointerException("context is null"))
        val intent = Intent(activity, AlbumActivity::class.java)
        return Observable
            .defer<ActivityResult> {
                if (mSelectionSpec.capture) {
                    activity.requestPermission(
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA
                    )
                        .compose(
                            RxPermissionTransformer(
                                activity,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.CAMERA
                            )
                        )
                        .flatMap {
                            activity.startActivityForResult(intent)
                        }
                } else {
                    activity.requestPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .compose(
                            RxPermissionTransformer(
                                activity,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE
                            )
                        )
                        .flatMap {
                            activity.startActivityForResult(intent)
                        }
                }
            }.subscribeOn(AndroidSchedulers.mainThread())
            .flatMap { activityResult ->
                if (activityResult.isOk) {
                    val paths = AlbumLauncher.obtainPathResult(activityResult.data)
                    val uris = AlbumLauncher.obtainResult(activityResult.data)
                    val isOriginalState = AlbumLauncher.obtainOriginalState(activityResult.data)
                    return@flatMap Observable.just(AlbumResult(isOriginalState, uris, paths))
                } else {
                    return@flatMap Observable.empty()
                }
            }
    }

    fun showPreview(showPreview: Boolean): SelectionCreator {
        mSelectionSpec.showPreview = showPreview
        return this
    }
}