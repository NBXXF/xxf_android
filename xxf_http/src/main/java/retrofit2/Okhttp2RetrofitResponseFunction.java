package retrofit2;

import io.reactivex.functions.Function;


/**
 * @author youxuan  E-mail:xuanyouwu@163.com
 * @Description 转换okhttp结果为retrofit结果
 */
public class Okhttp2RetrofitResponseFunction<T> implements Function<okhttp3.Response, Response<T>> {
    private OkHttpCall<T> okHttpCall;

    /**
     * 需要 OkHttpCall<T>
     *
     * @param call
     */
    public Okhttp2RetrofitResponseFunction(Call<T> call) {
        this.okHttpCall = (OkHttpCall<T>) call;
    }

    @Override
    public Response<T> apply(okhttp3.Response response) throws Exception {
        return this.okHttpCall.parseResponse(response);
    }

}
