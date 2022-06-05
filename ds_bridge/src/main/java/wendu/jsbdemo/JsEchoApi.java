package wendu.jsbdemo;

import android.webkit.JavascriptInterface;
import android.widget.Toast;

import org.json.JSONException;

import wendu.dsbridge.CompletionHandler;

/**
 * Created by du on 16/12/31.
 */

public class JsEchoApi {

    @JavascriptInterface
    public Object syn(Object args) throws JSONException {
        Toast.makeText(BaseApp.INSTANCE, "xxxx", Toast.LENGTH_SHORT).show();
        return  args;
    }

    @JavascriptInterface
    public void asyn(Object args,CompletionHandler handler){
        handler.complete(args);
    }
}