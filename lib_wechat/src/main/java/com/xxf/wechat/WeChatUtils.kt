package com.xxf.wechat

import android.graphics.Bitmap
import com.tencent.mm.opensdk.modelmsg.*

/**
 * @Description: 微信工具类
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * @CreateDate: 2017/10/13 11:14
 */
object WeChatUtils {

    /**
     * @param scene 分享场景  会话还是朋友圈
     */
    fun buildShareLinkReq(
        webpageUrl: String,
        title: String,
        description: String,
        thumbBitmap: Bitmap? = null,
        scene: Int = SendMessageToWX.Req.WXSceneSession
    ): SendMessageToWX.Req {
        val wxWebpageObject = WXWebpageObject()
        wxWebpageObject.webpageUrl = webpageUrl
        val wxMediaMessage = WXMediaMessage(wxWebpageObject)
        wxMediaMessage.title = title
        wxMediaMessage.description = description
        if (thumbBitmap != null) {
            wxMediaMessage.setThumbImage(thumbBitmap)
        }
        val req: SendMessageToWX.Req = SendMessageToWX.Req()
        req.transaction = buildTransaction("webpage")
        req.message = wxMediaMessage
        req.scene = scene
        return req
    }


    /**
     * 分享图片
     */
    fun buildShareImageReq(
        image: Bitmap,
        thumbBitmap: Bitmap?,
        scene: Int = SendMessageToWX.Req.WXSceneSession
    ) {
        val wxImageObject = WXImageObject(image)
        val wxMediaMessage = WXMediaMessage()
        wxMediaMessage.mediaObject = wxImageObject
        if (thumbBitmap != null) {
            wxMediaMessage.setThumbImage(thumbBitmap)
        }
        val req = SendMessageToWX.Req()
        req.transaction = buildTransaction("image")
        req.message = wxMediaMessage
        req.scene = scene
    }

    /*
     * 分享文字
     */
    private fun buildShareTextReq(
        text: String,
        scene: Int = SendMessageToWX.Req.WXSceneSession
    ): SendMessageToWX.Req {
        //初始化一个WXTextObject对象
        val textObj = WXTextObject()
        textObj.text = text
        //用WXTextObject对象初始化一个WXMediaMessage对象
        val wxMediaMessage = WXMediaMessage()
        wxMediaMessage.mediaObject = textObj
        wxMediaMessage.description = text
        //构造一个Req
        val req = SendMessageToWX.Req()
        //transaction字段用于唯一标识一个请求
        req.transaction = buildTransaction("text")
        req.message = wxMediaMessage
        //发送的目标场景， 可以选择发送到会话 WXSceneSession 或者朋友圈 WXSceneTimeline。 默认发送到会话。
        req.scene = scene
        return req;
    }

    /*
     * 分享视频
     */
    private fun buildShareVideoReq(
        videoUrl: String,
        title: String,
        description: String,
        thumbBitmap: Bitmap? = null,
        scene: Int = SendMessageToWX.Req.WXSceneSession
    ): SendMessageToWX.Req {
        val video = WXVideoObject()
        video.videoUrl = videoUrl
        val wxMediaMessage = WXMediaMessage(video)
        wxMediaMessage.title = title
        wxMediaMessage.description = description
        //		BitmapFactory.decodeStream(new URL(video.videoUrl).openStream());
        /**
         * 测试过程中会出现这种情况，会有个别手机会出现调不起微信客户端的情况。造成这种情况的原因是微信对缩略图的大小、title、description等参数的大小做了限制，所以有可能是大小超过了默认的范围。
         * 一般情况下缩略图超出比较常见。Title、description都是文本，一般不会超过。
         */
        if (thumbBitmap != null) {
            wxMediaMessage.setThumbImage(thumbBitmap)
        }
        val req = SendMessageToWX.Req()
        req.transaction = buildTransaction("video")
        req.message = wxMediaMessage
        req.scene = scene
        return req
    }

    private fun buildTransaction(type: String?): String {
        return if (type == null) System.currentTimeMillis()
            .toString() else type + System.currentTimeMillis()
    }
}