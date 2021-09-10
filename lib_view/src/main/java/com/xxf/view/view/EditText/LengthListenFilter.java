package com.xxf.view.view.EditText;

import android.text.InputFilter;
import android.text.Spanned;

import androidx.annotation.CallSuper;
import androidx.annotation.IntRange;
import androidx.annotation.NonNull;

import com.xxf.utils.StringUtils;

/**
 * Description  过长输入监听
 * Company Beijing icourt
 * author  youxuan  E-mail:xuanyouwu@163.com
 * date createTime：2017/10/10
 * version 2.1.0
 */
public abstract class LengthListenFilter extends InputFilter.LengthFilter {

    /**
     * 创建单个
     *
     * @param l
     * @return
     */
    public static InputFilter[] createSingleInputFilter(@NonNull LengthListenFilter l) {
        return new InputFilter[]{l};
    }

    int max;

    public LengthListenFilter(@IntRange(from = 1) int max) {
        super(max);
        this.max = max;
    }

    /**
     * @param source 为即将输入的字符串。source
     * @param start  source的start
     * @param end    endsource的end start为0，end也可理解为source长度了
     * @param dest   dest输入框中原来的内容，dest
     * @param dstart 要替换或者添加的起始位置，即光标所在的位置
     * @param dend   要替换或者添加的终止始位置，若为选择一串字符串进行更改，则为选中字符串 最后一个字符在dest中的位置。
     * @return
     */
    @CallSuper
    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        if (StringUtils.length(dest) + StringUtils.length(source) > max) {
            onInputOverLength(max);
        }
        return super.filter(source, start, end, dest, dstart, dend);
    }

    /**
     * 当输入超长时发生
     *
     * @param maxLength
     */
    public abstract void onInputOverLength(int maxLength);
}
