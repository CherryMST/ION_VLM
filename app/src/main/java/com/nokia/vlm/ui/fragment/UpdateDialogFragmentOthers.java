package com.nokia.vlm.ui.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.nokia.qrcode.view.CaptureView;
import com.nokia.vlm.R;
import com.nokia.vlm.ui.activity.QRResultActivity;
import com.qx.framelib.utlis.TextUtil;
import com.qx.framelib.utlis.ZLog;

/**
 * @创建者 DK-house
 * @创建时间 2017/12/20 10:40
 * @描述 ${TODO}
 * @更新者 ${AUTHOR}
 * @更新时间 2017/12/20
 * @更新描述 ${TODO}
 */

public class UpdateDialogFragmentOthers extends DialogFragment implements View.OnClickListener{
    private int             mEdittextId;
    private Context         context;
    private boolean         isToShow;
    private LinearLayout    mLlActions;
    private LinearLayout    mLlEdittextStyle;
    private LinearLayout    mLlEdittext;
    private LinearLayout    mLlContentView;
    private TextView        mTvItemName;
    public  Button          mBtnCancel;
    public  Button          mBtnApply;
    private EditText        mEtTobeUpdated;
    private OnClickListener listener;
    private SurfaceView     mSurfaceView;
    private CaptureView     mCaptureView;
    private String mTextForET;
    private String          mItemName;
    private int             mLayoutID;
    private Spinner mSpinner;


    public EditText getEtTobeUpdated() {
        return mEtTobeUpdated;
    }

    public UpdateDialogFragmentOthers(Context context) {
        super();

        this.context = context;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        ZLog.d("UpdateDialogFragment", "111111111");

        initListener();
        initEdittextStyle(mLayoutID);


        return super.onCreateView(inflater, container, savedInstanceState);
    }

    private void initListener() {
        mBtnCancel.setOnClickListener(this);
        mBtnApply.setOnClickListener(this);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mItemName = getArguments().getString(QRResultActivity.NAMEID);
        mTextForET = getArguments().getString(QRResultActivity.EDITTEXT_TEXT);
        mLayoutID = getArguments().getInt(QRResultActivity.RELATIVELAYOUTID);
        ZLog.d("UpdateDialogFragment", "22222222222");
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View           view     = inflater.inflate(R.layout.layout_update_dialog, null);

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(view);

        mBtnCancel = (Button) view.findViewById(R.id.btn_cancel);
        mBtnApply = (Button) view.findViewById(R.id.btn_apply);
        mTvItemName = (TextView) view.findViewById(R.id.tv_itemname);
        if (null != mItemName) {
            mTvItemName.setText(mItemName.concat(": "));
        }
        mLlEdittextStyle = (LinearLayout) view.findViewById(R.id.ll_edittext_style);
        return builder.create();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_cancel:
                if (listener != null) {
                    listener.onLeftClick();
                }
                break;
            case R.id.btn_apply:
                if (listener != null) {
                    listener.onRightClick();
                }
                break;
        }
    }

    public void setOnClickListener(OnClickListener listener) {
        this.listener = listener;
    }

    public interface OnClickListener {
        public void onLeftClick();

        public void onRightClick();

    }


    private void initEdittextStyle(int layoutId) {
        if (TextUtil.isEmpty(mItemName)) {
            mTvItemName.setText(mItemName);
        }
        LinearLayout.LayoutParams lp1 = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        LayoutInflater inflater1 = LayoutInflater.from(context);
        View           view1     = inflater1.inflate(R.layout.layout_update_edittext_style1, null);
        view1.setLayoutParams(lp1);
        switch (layoutId) {
            case R.id.rl_asset_id:
            case R.id.rl_module:
            case R.id.rl_vendor:
            case R.id.rl_sn:
            case R.id.rl_testEquipId:
            case R.id.rl_remark:
            case R.id.rl_calibration_req:
            case R.id.rl_calibration_cycle:
            case R.id.rl_description:
                view1 = inflater1.inflate(R.layout.layout_update_edittext_style1, null);
                mEtTobeUpdated = (EditText) view1.findViewById(R.id.et_update_data);
                if(null != mTextForET){
                    mEtTobeUpdated.setText(mTextForET);
                }
                break;
            case R.id.rl_site:

                view1 = getSpinnerView(inflater1,getResources().getStringArray(R.array.nameSite));
                break;
            case R.id.rl_department:
                view1 = getSpinnerView(inflater1,getResources().getStringArray(R.array.nameDep));
                break;
            case R.id.rl_subdepartment:
                view1 = getSpinnerView(inflater1,getResources().getStringArray(R.array.nameSubDepAll));
                break;
            case R.id.rl_current_user:
                //TODO---current user待确定
                break;
            case R.id.rl_type:
                view1 = getSpinnerView(inflater1, getResources().getStringArray(R.array.nameType));
                break;
            case R.id.rl_workingconditon:
                view1 = getSpinnerView(inflater1, getResources().getStringArray(R.array.nameWorkCondition));
                break;
            //TODO---R.rl_calibration_cycle, R.id.rl_calibration_req 待确定
            default:
                view1 = inflater1.inflate(R.layout.layout_update_edittext_style1, null);
                mEtTobeUpdated = (EditText) view1.findViewById(R.id.et_update_data);
                if(null != mTextForET){
                    mEtTobeUpdated.setText(mTextForET);
                }
                break;

        }
        view1.setLayoutParams(lp1);
        mLlEdittextStyle.addView(view1);

    }

    @NonNull
    private View getSpinnerView(LayoutInflater inflater1,String[] data) {
        final String[] array = data;
        View view1;
        view1 = inflater1.inflate(R.layout.layout_update_edittext_style3, null);
        mEtTobeUpdated = (EditText) view1.findViewById(R.id.et_update_data);
        mSpinner = (Spinner) view1.findViewById(R.id.spinner_update);
        //绑定要显示的texts
        ArrayAdapter adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item,array);
        //设置下拉列表的风格
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        adapter.setDropDownViewResource(R.layout.layout_white_text_spinner_item);
        mSpinner.setAdapter(adapter);
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TextView tv = (TextView)view;
                tv.setTextColor(getResources().getColor(R.color.color_white));
                tv.setGravity(Gravity.CENTER);


                String selectedStr = array[position];
                mEtTobeUpdated.setText(selectedStr);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        return view1;
    }
}