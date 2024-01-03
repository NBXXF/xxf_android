package com.xxf.qrcode;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Size;

import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * @author youxuan  E-mail:xuanyouwu@163.com
 * @Description
 */
public class QRCodeProviders {

    public static Builder of(String content) {
        return new Builder(content);
    }

    public static class Builder {
        /**
         * 二维码内容
         */
        String content;
        /**
         * 编码格式
         */
        Charset charSet = StandardCharsets.UTF_8;
        /**
         * 二维码输出大小
         */
        Size outputSize = new Size(200, 200);
        /**
         * 二维码颜色
         */
        int contentColor = Color.BLACK;

        /**
         * 设置内容填充
         */
        Bitmap contentFillImg;
        /**
         * 二维码内边距
         */
        int contentMargin = 1;
        /**
         * 二维码背景色
         */
        int backgroundColor = Color.WHITE;

        /**
         * logo
         */
        Bitmap logo;

        /**
         * logo 所占百分比
         */
        float logoPercent = 0.2F;

        /**
         * 容错率 L：7% M：15% Q：25% H：35%
         */
        ErrorCorrectionLevel errorCorrectionLevel = ErrorCorrectionLevel.M;

        Builder(String content) {
            this.content = Objects.requireNonNull(content);
        }

        Builder setCharSet(Charset charSet) {
            this.charSet = Objects.requireNonNull(charSet);
            return this;
        }

        public Builder setOutputSize(Size outputSize) {
            this.outputSize = Objects.requireNonNull(outputSize);
            return this;
        }

        public Builder setContentColor(int contentColor) {
            this.contentColor = contentColor;
            return this;
        }

        public Builder setContentMargin(int contentMargin) {
            this.contentMargin = contentMargin;
            return this;
        }

        public Builder setBackgroundColor(int backgroundColor) {
            this.backgroundColor = backgroundColor;
            return this;
        }

        public Builder setLogo(Bitmap logo) {
            this.logo = logo;
            return this;
        }

        public Builder setLogoPercent(float logoPercent) {
            this.logoPercent = logoPercent;
            return this;
        }

        public Builder setContentFillImg(Bitmap contentFillImg) {
            this.contentFillImg = contentFillImg;
            return this;
        }

        public Builder setErrorCorrectionLevel(ErrorCorrectionLevel errorCorrectionLevel) {
            this.errorCorrectionLevel = Objects.requireNonNull(errorCorrectionLevel);
            return this;
        }

        public Bitmap build() {
            return QRCodeUtil.createQRCodeBitmap(content, outputSize, charSet, errorCorrectionLevel, contentMargin, contentColor, backgroundColor, logo, logoPercent, contentFillImg);
        }
    }
}
