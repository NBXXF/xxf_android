package com.xxf.qrcode;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.text.TextUtils;
import android.util.Size;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.EncodeHintType;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.Result;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.nio.charset.Charset;
import java.util.Hashtable;
import java.util.Map;

/**
 * @author youxuan  E-mail:xuanyouwu@163.com
 * @Description
 */
public class QRCodeUtil {
    /**
     * 生成自定义二维码
     *
     * @param content                字符串内容
     * @param outputSizePx           输出二维码图片大小
     * @param character_set          编码方式（一般使用UTF-8）
     * @param error_correction_level 容错率 L：7% M：15% Q：25% H：35%
     * @param padding                空白边距（二维码与边框的空白区域） 做到了边框大小 确定 和输出尺寸确定
     * @param contentColor           内容色块
     * @param backgroundColor        背景色块
     * @param logoBitmap             logo图片（传null时不添加logo）
     * @param logoPercent            logo所占百分比
     * @param contentReplaceBitmp    用来代替黑色色块的图片（传null时不代替）
     * @return
     */
    public static Bitmap createQRCodeBitmap(String content,
                                            Size outputSizePx,
                                            Charset character_set,
                                            ErrorCorrectionLevel error_correction_level,
                                            int padding,
                                            int contentColor,
                                            int backgroundColor,
                                            Bitmap logoBitmap,
                                            float logoPercent,
                                            Bitmap contentReplaceBitmp) {
        // 字符串内容判空
        if (TextUtils.isEmpty(content)) {
            return null;
        }
        // 宽和高>=0
        if (outputSizePx.getWidth() < 0 || outputSizePx.getWidth() < 0) {
            return null;
        }
        try {
            /** 1.设置二维码相关配置,生成BitMatrix(位矩阵)对象 */
            Hashtable<EncodeHintType, Object> hints = new Hashtable<>();
            // 字符转码格式设置
            hints.put(EncodeHintType.CHARACTER_SET, character_set.name());
            // 容错率设置
            hints.put(EncodeHintType.ERROR_CORRECTION, error_correction_level);
            // 空白边距设置 系统的有bug
            hints.put(EncodeHintType.MARGIN, 0);
            /** 2.将配置参数传入到QRCodeWriter的encode方法生成BitMatrix(位矩阵)对象 */
            BitMatrix bitMatrix = new QRCodeWriter().encode(content, BarcodeFormat.QR_CODE, outputSizePx.getWidth(), outputSizePx.getHeight(), hints);

            //系统默认内边距很烦人
            bitMatrix = deleteMargin(bitMatrix);
            int outputWidth = bitMatrix.getWidth();
            int outputHeight = bitMatrix.getHeight();


            /** 3.创建像素数组,并根据BitMatrix(位矩阵)对象为数组元素赋颜色值 */
            if (contentReplaceBitmp != null) {
                //从当前位图按一定的比例创建一个新的位图
                contentReplaceBitmp = Bitmap.createScaledBitmap(contentReplaceBitmp, outputWidth, outputHeight, false);
            }

            //着色
            int[] pixels = new int[outputWidth * outputHeight];
            for (int y = 0; y < outputHeight; y++) {
                for (int x = 0; x < outputWidth; x++) {
                    //bitMatrix.get(x,y)方法返回true是黑色色块，false是白色色块
                    if (bitMatrix.get(x, y)) {// 黑色色块像素设置
                        if (contentReplaceBitmp != null) {//图片不为null，则将黑色色块换为新位图的像素。
                            pixels[y * outputWidth + x] = contentReplaceBitmp.getPixel(x, y);
                        } else {
                            pixels[y * outputWidth + x] = contentColor;
                        }
                    } else {
                        pixels[y * outputWidth + x] = backgroundColor;// 白色色块像素设置
                    }
                }
            }

            /** 4.创建Bitmap对象,根据像素数组设置Bitmap每个像素点的颜色值,并返回Bitmap对象 */
            Bitmap bitmap = Bitmap.createBitmap(outputWidth, outputHeight, Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, outputWidth, 0, 0, outputWidth, outputHeight);

            /**  5 保证最终输出尺寸是符合参数的*/
            bitmap = merge(bitmap, outputSizePx, backgroundColor, padding);

            /** 5.为二维码添加logo图标 */
            if (logoBitmap != null) {
                return addLogo(bitmap, logoBitmap, logoPercent);
            }
            return bitmap;
        } catch (WriterException e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 叠加居中合并
     *
     * @param bitmap
     * @return
     */
    public static Bitmap merge(Bitmap bitmap,
                               Size outputSizePx,
                               int backgroundColor,
                               int padding) {
        if (padding * 2 >= outputSizePx.getWidth() || padding * 2 >= outputSizePx.getHeight()) {
            throw new IllegalArgumentException("边距过大!");
        }

        Bitmap resultBitmap = Bitmap.createBitmap(outputSizePx.getWidth(), outputSizePx.getHeight(), bitmap.getConfig());
        resultBitmap.eraseColor(backgroundColor);
        int outPutWidth = resultBitmap.getWidth();
        int outPutHeight = resultBitmap.getHeight();

        // 创建画布对象
        Canvas canvas = new Canvas(resultBitmap);
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, resultBitmap.getWidth() - 2 * padding, resultBitmap.getHeight() - 2 * padding, false);
        canvas.drawBitmap(scaledBitmap, (outPutWidth - scaledBitmap.getWidth()) / 2.0f, (outPutHeight - scaledBitmap.getHeight()) / 2.0f, null);
        return resultBitmap;
    }


    /**
     * 自定义白边边框宽度 但是无法保证 最终输出大小 650 最后就是620了
     *
     * @param matrix
     * @param margin 其实应该是padding
     * @return
     */
    @Deprecated
    public static BitMatrix update(final BitMatrix matrix, final int margin) {
        int tempM = margin * 2;
        // 获取二维码图案的属性
        int[] rec = matrix.getEnclosingRectangle();
        int resWidth = rec[2] + tempM;
        int resHeight = rec[3] + tempM;
        // 按照自定义边框生成新的BitMatrix
        BitMatrix resMatrix = new BitMatrix(resWidth, resHeight);
        resMatrix.clear();
        // 循环，将二维码图案绘制到新的bitMatrix中
        for (int i = margin; i < resWidth - margin; i++) {
            for (int j = margin; j < resHeight - margin; j++) {
                if (matrix.get(i - margin + rec[0], j - margin + rec[1])) {
                    resMatrix.set(i, j);
                }
            }
        }
        return resMatrix;
    }

    private static BitMatrix deleteMargin(BitMatrix matrix) {
        int[] rec = matrix.getEnclosingRectangle();
        int resWidth = rec[2];
        int resHeight = rec[3];

        BitMatrix resMatrix = new BitMatrix(resWidth, resHeight);
        resMatrix.clear();
        for (int i = 0; i < resWidth; i++) {
            for (int j = 0; j < resHeight; j++) {
                if (matrix.get(i + rec[0], j + rec[1]))
                    resMatrix.set(i, j);
            }
        }
        return resMatrix;
    }

    /**
     * 向二维码中间添加logo图片(图片合成)
     *
     * @param srcBitmap   原图片（生成的简单二维码图片）
     * @param logoBitmap  logo图片
     * @param logoPercent 百分比 (用于调整logo图片在原图片中的显示大小, 取值范围[0,1] )
     *                    原图片是二维码时,建议使用0.2F,百分比过大可能导致二维码扫描失败。
     * @return
     */
    private static Bitmap addLogo(Bitmap srcBitmap, Bitmap logoBitmap, float logoPercent) {
        if (srcBitmap == null) {
            return null;
        }
        if (logoBitmap == null) {
            return srcBitmap;
        }
        //传值不合法时使用0.2F
        if (logoPercent < 0F || logoPercent > 1F) {
            logoPercent = 0.2F;
        }

        /** 1. 获取原图片和Logo图片各自的宽、高值 */
        int srcWidth = srcBitmap.getWidth();
        int srcHeight = srcBitmap.getHeight();
        int logoWidth = logoBitmap.getWidth();
        int logoHeight = logoBitmap.getHeight();

        /** 2. 计算画布缩放的宽高比 */
        float scaleWidth = srcWidth * logoPercent / logoWidth;
        float scaleHeight = srcHeight * logoPercent / logoHeight;

        /** 3. 使用Canvas绘制,合成图片 */
        Bitmap bitmap = Bitmap.createBitmap(srcWidth, srcHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawBitmap(srcBitmap, 0, 0, null);
        canvas.scale(scaleWidth, scaleHeight, srcWidth / 2, srcHeight / 2);
        canvas.drawBitmap(logoBitmap, srcWidth / 2 - logoWidth / 2, srcHeight / 2 - logoHeight / 2, null);

        return bitmap;
    }


    /**
     * 识别图片中的二维码
     *
     * @param input
     * @param hints Map<DecodeHintType, Object> hints = new HashMap<>();
     *              hints.put(DecodeHintType.CHARACTER_SET, "UTF-8");
     * @return
     */
    public static Result decode(Bitmap input, Map<DecodeHintType, ?> hints) {
        try {
            int width = input.getWidth();
            int height = input.getHeight();
            int[] data = new int[width * height];
            input.getPixels(data, 0, width, 0, 0, width, height);
            RGBLuminanceSource source = new RGBLuminanceSource(width, height, data);
            BinaryBitmap bitmap1 = new BinaryBitmap(new HybridBinarizer(source));
            QRCodeReader reader = new QRCodeReader();
            return reader.decode(bitmap1, hints);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}