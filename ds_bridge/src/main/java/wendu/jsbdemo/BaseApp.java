package wendu.jsbdemo;

import android.app.Application;

public class BaseApp extends Application {
    public static Application INSTANCE;

    @Override
    public void onCreate() {
        super.onCreate();
        INSTANCE=this;
    }
}
