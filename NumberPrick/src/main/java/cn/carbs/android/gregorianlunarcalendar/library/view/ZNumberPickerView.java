package cn.carbs.android.gregorianlunarcalendar.library.view;

import android.content.Context;
import android.util.AttributeSet;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cn.carbswang.android.numberpickerview.library.NumberPickerView;

/**
 * Created by ZhaoWei on 2016/12/22.
 */
public class ZNumberPickerView extends NumberPickerView {


    public ZNumberPickerView(Context context) {
        this(context, null);
    }

    public ZNumberPickerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ZNumberPickerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void refreshByNewDisplayedValues(NumberPriceAdapter adapter) {

        if (adapter == null) return;
        int len = adapter.getCount();
        String[] display = new String[len];
        for (int i = 0; i < len; i++) {
            display[i] = adapter.getItemValue(adapter, i);
        }
        super.refreshByNewDisplayedValues(display);
    }


    public String getCurrentValue() {
        return getDisplayedValues()[getValue()];
    }


    public abstract static class NumberPriceAdapter<T> {
        private List<T> list;

        public NumberPriceAdapter(List<T> list) {
            this.list = list;
        }

        public NumberPriceAdapter(T[] t) {
            list = new ArrayList<>(Arrays.asList(t));
        }

        public int getCount() {
            if (list == null || list.isEmpty()) return 0;
            return list.size();
        }

        public T getItem(int position) {
            return list.get(position);
        }

        public abstract String getItemValue(NumberPriceAdapter adapter, int position);
    }

}
