package com.xxf.images.glide.svg;

import static com.bumptech.glide.request.target.Target.SIZE_ORIGINAL;

import androidx.annotation.NonNull;

import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.ResourceDecoder;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.resource.SimpleResource;
import com.caverock.androidsvg.PreserveAspectRatio;
import com.caverock.androidsvg.SVG;
import com.caverock.androidsvg.SVGKT;
import com.caverock.androidsvg.SVGParseException;

import java.io.IOException;
import java.io.InputStream;

/**
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * date createTime：2021/9/20
 * Description ://svg解析
 */
public class SvgDecoder implements ResourceDecoder<InputStream, SVG> {

  @Override
  public boolean handles(@NonNull InputStream source, @NonNull Options options) {
    // TODO: Can we tell?
    return true;
  }

  public Resource<SVG> decode(
      @NonNull InputStream source, int width, int height, @NonNull Options options)
      throws IOException {
    try {
      SVG svg = SVG.getFromInputStream(source);

      SVGKT.INSTANCE.updateExLength(svg);

      if (svg.getDocumentWidth() <= 0 || svg.getDocumentWidth() <= 0) {
        if (width != SIZE_ORIGINAL) {
          svg.setDocumentWidth(width);
        }
        if (height != SIZE_ORIGINAL) {
          svg.setDocumentHeight(height);
        }
      }

      /**
       * 支持拉伸缩放
       * PreserveAspectRatio.FULLSCREEN 不会拉伸
       */
      svg.setDocumentPreserveAspectRatio(PreserveAspectRatio.FULLSCREEN);
      return new SimpleResource<>(svg);
    } catch (SVGParseException ex) {
      throw new IOException("Cannot load SVG from stream", ex);
    }
  }
}