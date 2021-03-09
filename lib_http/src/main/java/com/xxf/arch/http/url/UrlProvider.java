package com.xxf.arch.http.url;

/**
 * @author XGod
 * @describe 网络域名或基路由提供
 * @date 2020/5/13 13:56
 */
public interface UrlProvider {

    /**
     * 提供网络域名或基路由提供
     * @param apiClazz
     * @return
     */
    String getBaseUrl(Class apiClazz);
}
