package retrofit2;

import androidx.annotation.Nullable;

import java.lang.reflect.Type;

/**
 * @Description: 取的okhttp call参数
 * @Author: XGod
 * @CreateDate: 2020/6/18 10:26
 */
public abstract class OkHttpRxJavaCallAdapter<R, T> implements CallAdapter<R, T> {
    @Override
    public Type responseType() {
        return null;
    }

    @Override
    public final T adapt(Call<R> call) {
        Object[] args = null;
        if (call instanceof OkHttpCall) {
            args = ((OkHttpCall<R>) call).args;
        }
        return adapt(call, args);
    }

    public abstract T adapt(Call<R> call, @Nullable Object[] args);
}
