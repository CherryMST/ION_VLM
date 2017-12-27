package com.nokia.vlm.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.RadioButton;

/**
 * @创建者 DK-house
 * @创建时间 2017/12/21 15:05
 * @描述 ${TODO}
 * @更新者 ${AUTHOR}
 * @更新时间 2017/12/21
 * @更新描述 ${TODO}
 */

@SuppressLint("AppCompatCustomView")
public class MyRadioButton extends RadioButton {
    public MyRadioButton(Context context) {
        super(context);
        initCustomView();
    }

    public MyRadioButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        initCustomView();
    }

    public MyRadioButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initCustomView();
    }


    public MyRadioButton(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initCustomView();
    }

    private void initCustomView(){
        setButtonDrawable(null);
    }
}
