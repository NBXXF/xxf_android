

package com.xxf.mlkit.imageproxy;

import android.annotation.SuppressLint;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.media.Image;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.camera.core.ExperimentalGetImage;
import androidx.camera.core.ImageInfo;
import androidx.camera.core.ImageProxy;
import androidx.camera.core.ImmutableImageInfo;
import androidx.camera.core.impl.TagBundle;

import java.nio.ByteBuffer;

/** An {@link ImageProxy} which wraps around an {@link Image}. */
@RequiresApi(21) // TODO(b/200306659): Remove and replace with annotation on package-info.java
public final class AndroidImageProxy implements ImageProxy {
    private final Image mImage;

    private final PlaneProxy[] mPlanes;

    private final ImageInfo mImageInfo;

    /**
     * Creates a new instance which wraps the given image.
     *
     * @param image to wrap
     */
    @SuppressLint("RestrictedApi")
    public AndroidImageProxy(@NonNull Image image,int rotationDegrees) {
        mImage = image;

        Image.Plane[] originalPlanes = image.getPlanes();
        if (originalPlanes != null) {
            mPlanes = new PlaneProxy[originalPlanes.length];
            for (int i = 0; i < originalPlanes.length; ++i) {
                mPlanes[i] = new PlaneProxy(originalPlanes[i]);
            }
        } else {
            mPlanes = new PlaneProxy[0];
        }

        mImageInfo = ImmutableImageInfo.create(
                TagBundle.emptyBundle(),
                image.getTimestamp(),
                rotationDegrees,
                new Matrix());
    }

    @Override
    public void close() {
        mImage.close();
    }

    @Override
    @NonNull
    public Rect getCropRect() {
        return mImage.getCropRect();
    }

    @Override
    public void setCropRect(@Nullable Rect rect) {
        mImage.setCropRect(rect);
    }

    @Override
    public int getFormat() {
        return mImage.getFormat();
    }

    @Override
    public int getHeight() {
        return mImage.getHeight();
    }

    @Override
    public int getWidth() {
        return mImage.getWidth();
    }

    @Override
    @NonNull
    public ImageProxy.PlaneProxy[] getPlanes() {
        return mPlanes;
    }

    /** An {@link ImageProxy.PlaneProxy} which wraps around an {@link Image.Plane}. */
    private static final class PlaneProxy implements ImageProxy.PlaneProxy {
        private final Image.Plane mPlane;

        PlaneProxy(Image.Plane plane) {
            mPlane = plane;
        }

        @Override
        public int getRowStride() {
            return mPlane.getRowStride();
        }

        @Override
        public int getPixelStride() {
            return mPlane.getPixelStride();
        }

        @Override
        @NonNull
        public ByteBuffer getBuffer() {
            return mPlane.getBuffer();
        }
    }

    @Override
    @NonNull
    public ImageInfo getImageInfo() {
        return mImageInfo;
    }

    @Override
    @ExperimentalGetImage
    public Image getImage() {
        return mImage;
    }
}
