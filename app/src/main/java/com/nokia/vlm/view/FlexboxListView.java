package com.nokia.vlm.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import com.google.android.flexbox.FlexboxLayout;
import com.qx.framelib.utlis.ZLog;
import com.nokia.vlm.view.adapter.TagAdapter;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;


public class FlexboxListView extends FlexboxLayout implements TagAdapter.OnDataChangedListener {
    private TagAdapter adapter;
    private OnTagItemClickListener tagItemClickListener;

    private int mMaxSelectCount = 1;// 最多选中个数 -1：不限制
    private boolean isSingeCheck = true; //是否单选 //个数为1的时候有用

    public void setSingeCheck(boolean singeCheck) {
        isSingeCheck = singeCheck;
    }

    public boolean isSingeCheck() {
        return isSingeCheck;
    }

    private Set<Integer> mSelectView = new HashSet<>();

    public void setMaxSelectCount(int mMaxSelectCount) {
        if (mSelectView.size() > mMaxSelectCount) {
            Log.w("FlexboxListView", "you has already select more than " + mMaxSelectCount + " views , so it will be clear .");
            clearSelectView();
        }
        this.mMaxSelectCount = mMaxSelectCount;
    }

    public void clearSelectView() {
        Iterator<Integer> iterator = mSelectView.iterator();
        while (iterator.hasNext()) {
            int preIndex = iterator.next();
            View preView = getChildAt(preIndex);
            if (preView instanceof CheckBox) {
                ((CheckBox) preView).setChecked(false);
            }
        }
        mSelectView.clear();
    }

    public int getSelectCount() {
        return mSelectView.size();
    }

    public int getMaxSelectCount() {
        return mMaxSelectCount;
    }

    public FlexboxListView(Context context) {
        this(context, null);
    }

    public FlexboxListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FlexboxListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setAdapter(TagAdapter adapter) {
        this.adapter = adapter;
        this.adapter.setOnDataChangedListener(this);
        initView();
    }

    private void initView() {
        removeAllViews();
        int count = adapter.getCount();
        for (int i = 0; i < count; i++) {
            addView(i);
        }
    }

    private void addView(int i) {
        View view = adapter.getView(this, i, adapter.getItem(i));
        final int position = i;
        view.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                doSelect(view, position);
                if (tagItemClickListener != null) {
                    tagItemClickListener.onClick(FlexboxListView.this, view, position);
                }
            }
        });

        ViewGroup parent = (ViewGroup) view.getParent();
        if (parent != null) {
            ZLog.d(parent.toString());
            parent.removeAllViews();
        }
        selectView(view, position);
        addView(view);
    }

    private void doSelect(View view, int position) {
        if (view instanceof CheckBox) {
            CheckBox child = (CheckBox) view;

            if (child.isChecked()) {
                if (mMaxSelectCount == 1 && mSelectView.size() == 1) { //单选
                    Iterator<Integer> iterator = mSelectView.iterator();
                    Integer preIndex = iterator.next();
                    View preView = getChildAt(preIndex);
                    if (preView instanceof CheckBox) {
                        ((CheckBox) preView).setChecked(false);
                    }
//                    child.setChecked(true);
                    mSelectView.remove(preIndex);
                    mSelectView.add(position);
                } else {

                    if (mMaxSelectCount > 0 && mSelectView.size() >= mMaxSelectCount) {
                        child.setChecked(false);
                        return;
                    }
//                    child.setChecked(true);
                    mSelectView.add(position);
                }

            } else {
                if (mMaxSelectCount == 1 && isSingeCheck) {
                    child.setChecked(true);
                } else {
                    mSelectView.remove(position);
                }


            }


        }
    }

    /**
     * 选中的
     *
     * @param view
     * @param position
     */
    private void selectView(View view, int position) {
        if (view instanceof CheckBox) {
            CheckBox child = (CheckBox) view;

            if (child.isChecked() && !mSelectView.contains(position)) {
                mSelectView.add(position);
            }

            if (mSelectView.contains(position) && !child.isChecked()) {
                child.setChecked(true);
            }
        }
    }

    public TagAdapter getAdapter() {
        return adapter;
    }

    @Override
    public void onChanged() {
        initView();
    }

    @Override
    public void onItemChanged(int position) {

    }

    @Override
    public void onItemRemove(int position) {
        if (position < getChildCount()) {
            removeViewAt(position);
            adapter.removeItem(position);
            onChanged();
        }

    }

    @Override
    public void onAddLastItem(boolean isCheck) {
        int lastItem = adapter.getCount() - 1;
        if (isCheck) {
            mSelectView.add(lastItem);
        }
        addView(lastItem);
    }

//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        if (event.getAction() == MotionEvent.ACTION_UP) {
//            mMotionEvent = MotionEvent.obtain(event);
//        }
//        return super.onTouchEvent(event);
//    }
//
//    @Override
//    public boolean performClick() {
//        if (mMotionEvent == null) return super.performClick();
//
//        int x = (int) mMotionEvent.getX();
//        int y = (int) mMotionEvent.getY();
//        mMotionEvent = null;
//
//        View child = findChild(x, y);
//        int pos = findPosByView(child);
//        if (child != null) {
////            doSelect(child, pos);
//            if (tagItemClickListener != null) {
//                tagItemClickListener.onClick(this, child, pos);
//            }
//        }
//        return super.performClick();
//    }
//
//    private View findChild(int x, int y) {
//        final int cCount = getChildCount();
//        for (int i = 0; i < cCount; i++) {
//            View v = getChildAt(i);
//            if (v.getVisibility() == View.GONE) continue;
//            Rect outRect = new Rect();
//            v.getHitRect(outRect);
//            if (outRect.contains(x, y)) {
//                return v;
//            }
//        }
//        return null;
//    }
//
//    private int findPosByView(View child) {
//        final int cCount = getChildCount();
//        for (int i = 0; i < cCount; i++) {
//            View v = getChildAt(i);
//            if (v == child) return i;
//        }
//        return -1;
//    }

    public int getAdapterItemcount() {
        return adapter.getCount();
    }

    public Set<Integer> getSelectView() {
        return new HashSet<>(mSelectView);
    }

    public void setOnTagItemClickListener(OnTagItemClickListener listener) {
        tagItemClickListener = listener;
    }

    public interface OnTagItemClickListener {
        public void onClick(FlexboxListView parent, View view, int position);
    }

}
