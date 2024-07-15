// IMyAidlInterface.aidl
package com.xxf.jbinder.sample_server;

// Declare any non-default types here with import statements

interface IMyAidlInterface {
    IMyAidlInterface testIn(in IMyAidlInterface callback);
}
