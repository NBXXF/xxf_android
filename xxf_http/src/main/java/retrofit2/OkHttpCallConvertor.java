package retrofit2;

import io.reactivex.functions.Function;
import okhttp3.ResponseBody;

/**
 * @author youxuan  E-mail:xuanyouwu@163.com
 * @Description
 */
public class OkHttpCallConvertor<T> implements Function<Call<T>, Converter<ResponseBody, T>> {

    @Override
    public Converter<ResponseBody, T> apply(Call<T> call) throws Exception {
        return (Converter<ResponseBody, T>) ((OkHttpCall<T>) call).responseConverter;
    }
}
