package retrofit2;

import com.google.gson.Gson;
import com.xxf.arch.json.GsonFactory;

import io.reactivex.functions.Function;
import okhttp3.MediaType;
import okhttp3.ResponseBody;

/**
 * @author youxuan  E-mail:xuanyouwu@163.com
 * @Description 转换okhttp结果为retrofit结果
 */
public class Retrofit2OkhttpResponseFunction<T> implements Function<Response<T>, okhttp3.Response> {

    @Override
    public okhttp3.Response apply(Response<T> tResponse) throws Exception {
        MediaType mediaType = MediaType.get(tResponse.raw().header("Content-Type", "application/json;charset=UTF-8"));
        Gson gson = GsonFactory.createGson();
        //TODO 目前支持json转换 注意
        ResponseBody responseBody = ResponseBody.create(mediaType, gson.toJson(tResponse.body()));
        return tResponse.raw()
                .newBuilder()
                .body(responseBody)
                .build();
    }


}
