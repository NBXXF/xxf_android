package com.xxf.media.album;

import android.net.Uri;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: XGod  xuanyouwu@163.com  17611639080
 * Date: 3/9/21 11:06 AM
 * Description: 相册返回结果
 */
public class AlbumResult implements Serializable {
    private boolean isOriginalState;
    private List<Uri> uris;
    private List<String> paths;

    public AlbumResult(boolean isOriginalState, List<Uri> uris, List<String> paths) {
        this.isOriginalState = isOriginalState;
        this.uris = uris;
        this.paths = paths;
    }

    public boolean isOriginalState() {
        return isOriginalState;
    }

    public List<Uri> getUris() {
        return uris;
    }

    public List<String> getPaths() {
        return paths;
    }
}
