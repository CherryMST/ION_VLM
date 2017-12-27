package com.nokia.vlm.view;

import android.content.Context;
import android.support.v7.widget.AppCompatEditText;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.AttributeSet;

import com.qx.framelib.utlis.TextUtil;

/**
 * 价格输入框
 */

public class PriceEditText extends AppCompatEditText implements InputFilter {
    public PriceEditText(Context context) {
        super(context);
    }

    public PriceEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PriceEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        //过滤
        setFilters(new InputFilter[]{this});
    }

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {

        if (dstart == 0 && source.equals(".")) {//第一位是否是小数
            return "";
        } else {
            String str = dest.toString();
            if (!TextUtil.isEmpty(str)) {
                String dotBehind = TextUtil.getDotBehind(str);
                if (!TextUtil.isEmpty(dotBehind) && dotBehind.length() >= 2) {//保留两位小数
                    return "";
                }
            }
        }
        return source;
    }
}
