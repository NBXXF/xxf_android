package retrofit2;

import io.reactivex.rxjava3.functions.Function;
import okhttp3.ResponseBody;

/**
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * @Description
 */
public class OkHttpCallConvertor<T> implements Function<Call<T>, Converter<ResponseBody, T>> {

    @Override
    public Converter<ResponseBody, T> apply(Call<T> call) throws Exception {
        return (Converter<ResponseBody, T>) ((OkHttpCall<T>) call).responseConverter;
    }
}
