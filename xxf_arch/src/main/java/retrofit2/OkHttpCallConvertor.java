package retrofit2;

import java.lang.reflect.Field;

import io.reactivex.functions.Function;
import okhttp3.ResponseBody;

/**
 * @author youxuan  E-mail:xuanyouwu@163.com
 * @Description
 */
public class OkHttpCallConvertor<T> implements Function<Call<T>, Converter<ResponseBody, T>> {

    @Override
    public Converter<ResponseBody, T> apply(Call<T> call) throws Exception {
        OkHttpCall<T> okCall = (OkHttpCall<T>) call;
        Field responseConverter = okCall.getClass().getDeclaredField("responseConverter");
        responseConverter.setAccessible(true);
        return (Converter<ResponseBody, T>) responseConverter.get(okCall);
    }
}
