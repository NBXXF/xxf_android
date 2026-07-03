package com.xxf.arch.model;

import androidx.annotation.NonNull;

/**
 * @Author: XGod  xuanyouwu@163.com  17611639080
 * Date: 2/5/18  6:27 PM
 * Description: 下载任务
 */
public class DownloadTask implements Cloneable {
    String url;
    String filePath;
    long current;
    long duration;

    public DownloadTask(String url, String filePath, long duration) {
        this.url = url;
        this.filePath = filePath;
        this.duration = duration;
    }

    public void setCurrent(long current) {
        this.current = current;
    }

    public String getUrl() {
        return url;
    }

    public String getFilePath() {
        return filePath;
    }

    public long getCurrent() {
        return current;
    }

    public long getDuration() {
        return duration;
    }

    @Override
    public String toString() {
        return "DownloadTask{" +
                "url='" + url + '\'' +
                ", filePath='" + filePath + '\'' +
                ", current=" + current +
                ", duration=" + duration +
                '}';
    }

    @NonNull
    @Override
    public DownloadTask clone() throws CloneNotSupportedException {
        return (DownloadTask) super.clone();
    }
}
