package com.xxf.arch.utils;

import android.content.ContentResolver;
import android.net.Uri;

import java.io.File;
import java.io.InputStream;
import java.io.RandomAccessFile;

/**
 * 多线程拷贝暂时不明显
 */
public class CopyThread2 extends Thread {

    ContentResolver contentResolver;
    Uri uri;
    File dstFile;
    long start;
    long length;

    CopyThread2(ContentResolver contentResolver,
                Uri uri,
                File dstFile,
                long start,
                long length) {
        this.contentResolver = contentResolver;
        this.uri = uri;
        this.dstFile = dstFile;
        this.start = start;
        this.length = length;
    }

    @Override
    public void run() {
        super.run();

        try {
            InputStream inputStream = contentResolver.openInputStream(uri);
            inputStream.skip(start);
            RandomAccessFile rw = new RandomAccessFile(dstFile, "rw");
            rw.seek(start);
            byte[] buffer = new byte[1024 * 8];
            long time = (long) Math.ceil(length * 1.0f / buffer.length);
            while (time >= 0) {
                inputStream.read(buffer);
                rw.write(buffer);
                time--;
            }
            inputStream.close();
            rw.close();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}
