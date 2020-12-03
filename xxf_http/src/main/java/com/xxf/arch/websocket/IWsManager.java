package com.xxf.arch.websocket;

import okhttp3.WebSocket;
import okio.ByteString;

/**
 * @Description: websocket
 * @Author: XGod
 * @CreateDate: 2020/7/23 15:20
 */
interface IWsManager {
    String tag();

    WebSocket getWebSocket();

    void startConnect();

    void stopConnect();

    boolean isWsConnected();

    int getCurrentStatus();

    void setCurrentStatus(int currentStatus);

    boolean sendMessage(String msg);

    boolean sendMessage(ByteString byteString);
}
