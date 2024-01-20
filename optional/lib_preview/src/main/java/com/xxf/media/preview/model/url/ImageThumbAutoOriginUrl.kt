package com.xxf.media.preview.model.url

/**
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * date createTime：2021/9/13
 * Description ://先缩略图然后自动加载原图
 * @param thumbUrl 缩略图
 * @param originaUrl 原图
 */
open class ImageThumbAutoOriginUrl(val thumbUrl: String, val originUrl: String) : ImageUrl(thumbUrl)