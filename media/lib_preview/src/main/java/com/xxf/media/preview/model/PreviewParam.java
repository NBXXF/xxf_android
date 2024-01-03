package com.xxf.media.preview.model;

import com.xxf.media.preview.model.url.ImageUrl;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * date createTime：2021/9/13
 * Description ://预览主页面参数
 */
public class PreviewParam implements Serializable {
    public List<ImageUrl> urls;
    public int currentIndex;
    public String sharedElementName;
    public String userFragmentClass;

    public PreviewParam(List<ImageUrl> urls, int currentIndex, String sharedElementName) {
        this.urls = urls;
        this.currentIndex = currentIndex;
        this.sharedElementName = sharedElementName;
    }

    public PreviewParam(List<ImageUrl> urls, int currentIndex, String sharedElementName, String userFragmentClass) {
        this.urls = urls;
        this.currentIndex = currentIndex;
        this.sharedElementName = sharedElementName;
        this.userFragmentClass = userFragmentClass;
    }
}
