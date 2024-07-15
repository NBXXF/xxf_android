package com.xxf.jbinder.sample_client;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.xxf.jbinder.sample_library.IRemoteServiceImpl;
import com.xxf.jbinder.JBinder;

/**
 * 替代aidl
 */
public class LocalService extends Service {
    @Override
    public IBinder onBind(Intent intent) {
        return JBinder.create(new IRemoteServiceImpl("LocalService"));
    }
}
