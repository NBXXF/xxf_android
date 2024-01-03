package com.xxf.media.preview.model.url;

import androidx.annotation.DrawableRes;

import java.io.Serializable;

/**
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * date createTime：2021/9/13
 * Description : 路径参数模型
 */
public class ImageUrl implements Serializable {

    private String url;

    public String getUrl() {
        return url;
    }

    public ImageUrl(String url) {
        this.url = url;
    }

    /**
     * 占位图 id>0 有效
     */
    @DrawableRes
    public int placeholderResourceId;

    /**
     * 加载错误的图片  id>0 有效
     */
    @DrawableRes
    public int errorResourceId;
}
