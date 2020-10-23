package com.alibaba.android.arouter.facade.service;

import android.net.Uri;

import androidx.annotation.Nullable;

import com.alibaba.android.arouter.facade.template.IProvider;

/**
 * Preprocess your path
 *
 * @author Alex <a href="mailto:zhilong.liu@aliyun.com">Contact me.</a>
 * @version 1.0
 * @since 2016/12/9 16:48
 */
public interface PathReplaceService extends IProvider {

    /**
     * For normal path.
     *
     * @param path raw path
     */
    String forString(@Nullable String path);

    /**
     * For uri type.
     *
     * @param uri raw uri
     */
    Uri forUri(@Nullable Uri uri);
}
