package com.xxf.wechat.model


/**
 *
 * title desc 有长度限制
 * https://developers.weixin.qq.com/doc/oplatform/Mobile_App/Share_and_Favorites/Android.html
 *
 * @param url  网页 视频 等访问地址
 * @param title 建议<=50
 * @param description 建议<=100
 * @param thumb 缩略图 注意 必须是glide支持的类型
 * @param clipboardText 同步到系统粘贴板的文字 (一般业务在分享的时候 会把信息同步到系统粘贴板,很多大厂这样干)
 */
data class ShareCardInfo(
    var url: String,
    var title: String,
    var description: String,
    var thumb: Any? = null,
    var clipboardText: String? = null
)