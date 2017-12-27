package com.nokia.vlm.ui.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.nokia.vlm.R;
import com.nokia.vlm.entity.CardAsset;
import com.nokia.vlm.entity.ComputerAsset;
import com.nokia.vlm.entity.PluggableAsset;
import com.nokia.vlm.entity.ShelfAsset;
import com.nokia.vlm.entity.TestEquipAsset;
import com.nokia.vlm.entity.UpdateDataInfo;
import com.nokia.vlm.ui.fragment.AddFragment;
import com.nokia.vlm.utils.HttpWithoutJersey;
import com.nokia.vlm.utils.PXUtils;
import com.qx.framelib.activity.BaseTitle2Activity;
import com.qx.framelib.utlis.ToastUtils;
import com.qx.framelib.utlis.ZLog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @创建者 DK-house
 * @创建时间 2017/11/13 23:57
 * @描述 ${TODO}
 * @更新者 ${AUTHOR}
 * @更新时间 2017/11/13
 * @更新描述 ${TODO}
 */

public class AddNewOneActivity extends BaseTitle2Activity implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    public static final String[] tables = {"TestEquip_Asset", "Card_Asset", "Pluggable_Asset", "Shelf_Asset", "Computer_Asset"};
    private List<RelativeLayout> mRlListForDisplay;
    private List<EditText>         mETListForDisplay;
    private List<EditText>         mETListForAddNewOne;


    private ImageView mIvQRScan;
    private ImageView mIvAreaScan;


    private List<EditText> listPart2;
    private List<EditText> listPart3;
    private List<EditText> listPart4;
    private LinearLayout mLlpart1;
    private LinearLayout mLlpart2;
    private LinearLayout mLlpart3;
    private LinearLayout mLlpart4;

    private RelativeLayout mRlQRCode;
    private RelativeLayout mRlArea;
    private RelativeLayout mRlSN;
    private RelativeLayout mRlAssetId;
    private RelativeLayout mRlSite;
    private RelativeLayout mRlDepartment;
    private RelativeLayout mRlSubDepartment;
    private RelativeLayout mRlCurrentUser;
    private RelativeLayout mRlModule;
    private RelativeLayout mRlVendor;
    private RelativeLayout mRlType;
    private RelativeLayout mRlDescription;
    private RelativeLayout mRlTestEquipId;
    private RelativeLayout mRlWorkingConidtion;
    private RelativeLayout mRlRemark;
    private RelativeLayout mRlDeliverDate;
    private RelativeLayout mRlCalibrationCycle;
    private RelativeLayout mRlCalibrationReq;

    private EditText mEtQRCode;
    private EditText mEtArea;
    private EditText mEtSN;
    private EditText mEtAssetId;
    private EditText  mEtSite;
    private EditText  mEtDepartment;
    private EditText  mEtSubDepartment;
    private EditText  mEtCurrentUser;
    private EditText mEtModule;
    private EditText mEtVendor;
    private EditText  mEtType;
    private EditText mEtDescription;
    private EditText mEtTestEquipId;
    private EditText  mEtWorkingConidtion;
    private EditText mEtRemark;
    private EditText mEtDeliverDate;
    private EditText mEtCalibrationCycle;
    private EditText mEtCalibrationReq;

    private Spinner mSpinnerSite;
    private Spinner mSpinnerDepartment;
    private Spinner mSpinnerSubDepartment;
    private Spinner mSpinnerCurrentUser;
    private Spinner mSpinnerType;
    private Spinner mSpinnerWorkingConidtion;

    //    public  boolean  mResultNotNull;

    private String       mTableNameForResultMap;
    private boolean      mIsAddNewOne;
    private LinearLayout mLlAddNewOne;
    private Button       mBtnAdd;
    private Button       mBtnReset;
    private String       mTotalStrForAdd;


    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String result = (String) msg.obj;
            switch (msg.what) {
                case 1:
                    Map parse = (Map) JSON.parse(result);
                    Object obj = parse.get("Result");
                    String str = obj.toString();

                    if (str.contains("Successful")) {
                        Toast.makeText(getApplicationContext(), "add successfully!", Toast.LENGTH_SHORT).show();
                    }else {

                        Toast.makeText(getApplicationContext(), "add failed!", Toast.LENGTH_SHORT).show();
                    }
                    break;
            }

        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        isStatusBarEnald = true;
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.layout_activity_add_detail);

        initTitle();
        initView();
        initData();
        registers();
    }

    private void registers() {

    }

    private void initData() {
        Bundle bundle = getIntent().getExtras();
        if (bundle == null)
            return;
        //        mResultNotNull = bundle.getBoolean(QRCodeCaptureActivity.QR_RESULT_NOT_NULL, false);
        mTableNameForResultMap = null;
        mIsAddNewOne = bundle.getBoolean(AddFragment.ADD_NEW_ONE, false);
        ArrayList<String> tableNameList = bundle.getStringArrayList(QRCodeCaptureActivity.QR_RESULT_TABLENAME);
        if (null != tableNameList && tableNameList.size() == 1) {
            mTableNameForResultMap = tableNameList.get(0);
        }

        mETListForAddNewOne = new ArrayList<>();


        if (null != mTableNameForResultMap) {

            displaySearchResult(mTableNameForResultMap);

        }
        setLinearLayoutSizeByPart();
        initViewForAdd();

    }


    private void initViewForAdd() {

        mLlAddNewOne = (LinearLayout) findViewById(R.id.ll_add_new_one);
        //添加add按钮
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        LayoutInflater inflater = LayoutInflater.from(AddNewOneActivity.this);
        View           view     = inflater.inflate(R.layout.layout_buttons_add_new_one, null);
        mBtnAdd = (Button) view.findViewById(R.id.btn_add);
        mBtnReset = (Button) view.findViewById(R.id.btn_reset);
        mBtnAdd.setOnClickListener(this);
        mBtnReset.setOnClickListener(this);
        view.setLayoutParams(lp);
        mLlAddNewOne.addView(view);
        for (Object obj : mETListForDisplay) {
            if (obj instanceof EditText) {
                ((EditText) obj).setEnabled(true);
                ((EditText) obj).setGravity(Gravity.RIGHT);

            }
        }
    }

    private void displaySearchResult(String resultMapName) {
        switch (resultMapName) {
            case "TestEquip_Asset":

                displayTestEquipAsset();

                break;
            case "Card_Asset":
                displayCardAsset();
                break;
            case "Pluggable_Asset":
                displayPluggabelAsset();
                break;
            case "Shelf_Asset":
                displayShelfAsset();
                break;
            case "Computer_Asset":
                displayComputerAsset();
                break;
        }
    }


    private void displayComputerAsset() {

    }

    private void displayShelfAsset() {

    }

    private void displayPluggabelAsset() {

    }

    private void displayCardAsset() {
        //        displayForAddNewOneTestEquipAsset(xxxxx);//缺少一个用于展示cardasset的array序列
    }

    private void displayTestEquipAsset() {


        mETListForAddNewOne = new ArrayList<>();
//        mETListForAddNewOne.addAll(mETListForDisplay);
        displayForAddNewOneTestEquipAsset(TestEquipAsset.keyArray);

    }


    /**
     * 以TestEquipAsset.arrayForDisplay 为标准, 隐藏多余的控件
     */
    private void displayForAddNewOneTestEquipAsset(String[] keyArray) {
        listPart2 = new ArrayList<>();
        listPart3 = new ArrayList<>();
        listPart4 = new ArrayList<>();
        List<EditText> list = new ArrayList<>();
        for (int i = 0; i < TestEquipAsset.arrayForDisplay.length; i++) {
            if (i > TestEquipAsset.arrayForDisplay.length - 1) {
                return;
            }


            for (int j = 0; j < keyArray.length; j++) {
                if (j > keyArray.length - 1) {
                    break;
                }
                if (TestEquipAsset.arrayForDisplay[i].equals(keyArray[j])) {
                    mRlListForDisplay.get(i).setVisibility(View.VISIBLE);
                    list.add(mETListForDisplay.get(i));
                    if (i >= 3 && i <= 7) {
                        listPart2.add(mETListForDisplay.get(i));
                    } else if (i >= 8 && i <= 14) {
                        listPart3.add(mETListForDisplay.get(i));
                    } else if (i >= 15 && i <= 17) {
                        listPart4.add(mETListForDisplay.get(i));

                    }
                }
            }
        }

        mETListForAddNewOne = list;

        for (int k = 0; k < mRlListForDisplay.size(); k++) {
            if (!mETListForAddNewOne.contains(mETListForDisplay.get(k))) {
                mRlListForDisplay.get(k).setVisibility(View.GONE);
            }

        }
    }





    private void initView() {
        mLlpart1 = (LinearLayout) findViewById(R.id.ll_add_detail_part1);
        mLlpart2 = (LinearLayout) findViewById(R.id.ll_add_detail_part2);
        mLlpart3 = (LinearLayout) findViewById(R.id.ll_add_detail_part3);
        mLlpart4 = (LinearLayout) findViewById(R.id.ll_add_detail_part4);


        mIvQRScan = (ImageView) findViewById(R.id.iv_add_scan_qr);
        mIvAreaScan = (ImageView) findViewById(R.id.iv_add_scan_area);


        mRlListForDisplay = new ArrayList<>();
        mETListForDisplay = new ArrayList<>();

        mRlQRCode = (RelativeLayout) findViewById(R.id.rl_qr_code);
        mRlArea = (RelativeLayout) findViewById(R.id.rl_area);
        mRlSN = (RelativeLayout) findViewById(R.id.rl_sn);
        mRlAssetId = (RelativeLayout) findViewById(R.id.rl_asset_id);
        mRlSite = (RelativeLayout) findViewById(R.id.rl_site);
        mRlDepartment = (RelativeLayout) findViewById(R.id.rl_department);
        mRlSubDepartment = (RelativeLayout) findViewById(R.id.rl_subdepartment);
        mRlCurrentUser = (RelativeLayout) findViewById(R.id.rl_current_user);
        mRlModule = (RelativeLayout) findViewById(R.id.rl_module);
        mRlVendor = (RelativeLayout) findViewById(R.id.rl_vendor);
        mRlType = (RelativeLayout) findViewById(R.id.rl_type);
        mRlDescription = (RelativeLayout) findViewById(R.id.rl_description);
        mRlTestEquipId = (RelativeLayout) findViewById(R.id.rl_testEquipId);
        mRlWorkingConidtion = (RelativeLayout) findViewById(R.id.rl_workingconditon);
        mRlRemark = (RelativeLayout) findViewById(R.id.rl_remark);
        mRlDeliverDate = (RelativeLayout) findViewById(R.id.rl_deliver_date);
        mRlCalibrationCycle = (RelativeLayout) findViewById(R.id.rl_calibration_cycle);
        mRlCalibrationReq = (RelativeLayout) findViewById(R.id.rl_calibration_req);

        mRlListForDisplay.add(mRlQRCode);
        mRlListForDisplay.add(mRlArea);
        mRlListForDisplay.add(mRlDescription);
        mRlListForDisplay.add(mRlAssetId);
        mRlListForDisplay.add(mRlSite);
        mRlListForDisplay.add(mRlDepartment);
        mRlListForDisplay.add(mRlSubDepartment);
        mRlListForDisplay.add(mRlCurrentUser);
        mRlListForDisplay.add(mRlModule);
        mRlListForDisplay.add(mRlVendor);
        mRlListForDisplay.add(mRlType);
        mRlListForDisplay.add(mRlSN);
        mRlListForDisplay.add(mRlTestEquipId);
        mRlListForDisplay.add(mRlWorkingConidtion);
        mRlListForDisplay.add(mRlRemark);
        mRlListForDisplay.add(mRlDeliverDate);
        mRlListForDisplay.add(mRlCalibrationCycle);
        mRlListForDisplay.add(mRlCalibrationReq);


        mEtQRCode = (EditText) findViewById(R.id.et_qr_code);
        mEtArea = (EditText) findViewById(R.id.et_area);
        mEtSN = (EditText) findViewById(R.id.et_sn);
        mEtAssetId = (EditText) findViewById(R.id.et_asset_id);

        mEtSite = (EditText) findViewById(R.id.et_site);
        mSpinnerSite = (Spinner) findViewById(R.id.spinner_site);
        mSpinnerSite.setSelection(0);
        mEtSite.setText(getResources().getStringArray(R.array.nameSite)[0]);
        mSpinnerSite.setOnItemSelectedListener(this);

        mEtDepartment = (EditText) findViewById(R.id.et_department);
        mSpinnerDepartment = (Spinner) findViewById(R.id.spinner_department);
        mSpinnerDepartment.setSelection(0);
        mEtSite.setText(getResources().getStringArray(R.array.nameDep)[0]);
        mSpinnerDepartment.setOnItemSelectedListener(this);

        mEtSubDepartment = (EditText) findViewById(R.id.et_subdepartment);
        mSpinnerSubDepartment = (Spinner) findViewById(R.id.spinner_subdepartment);
        mSpinnerSubDepartment.setSelection(0);
        mEtSubDepartment.setText(getResources().getStringArray(R.array.nameSubDepAll)[0]);
        mSpinnerSubDepartment.setOnItemSelectedListener(this);

        mEtCurrentUser = (EditText) findViewById(R.id.et_current_user);
        mSpinnerCurrentUser = (Spinner) findViewById(R.id.spinner_current_user);
        //TODO---待补充
        mSpinnerCurrentUser.setOnItemSelectedListener(this);

        mEtModule = (EditText) findViewById(R.id.et_module);
        mEtVendor = (EditText) findViewById(R.id.et_vendor);

        mEtType = (EditText) findViewById(R.id.et_type);
        mSpinnerType = (Spinner) findViewById(R.id.spinner_type);
        mSpinnerType.setSelection(0);
        mEtType.setText(getResources().getStringArray(R.array.nameType)[0]);
        mSpinnerType.setOnItemSelectedListener(this);

        mEtDescription = (EditText) findViewById(R.id.et_description);
        mEtTestEquipId = (EditText) findViewById(R.id.et_testEquipId);

        mEtWorkingConidtion = (EditText) findViewById(R.id.et_workingconditon);
        mSpinnerWorkingConidtion = (Spinner) findViewById(R.id.spinner_workingconditon);
        mSpinnerWorkingConidtion.setSelection(0);
        mEtWorkingConidtion.setText(getResources().getStringArray(R.array.nameWorkCondition)[0]);
        mSpinnerWorkingConidtion.setOnItemSelectedListener(this);

        mEtRemark = (EditText) findViewById(R.id.et_remark);
        mEtDeliverDate = (EditText) findViewById(R.id.et_deliver_date);
        mEtCalibrationCycle = (EditText) findViewById(R.id.et_calibration_cycle);
        mEtCalibrationReq = (EditText) findViewById(R.id.et_calibration_req);

        mETListForDisplay.add(mEtQRCode);
        mETListForDisplay.add(mEtArea);
        mETListForDisplay.add(mEtDescription);
        mETListForDisplay.add(mEtAssetId);
        mETListForDisplay.add(mEtSite);
        mETListForDisplay.add(mEtDepartment);
        mETListForDisplay.add(mEtSubDepartment);
        mETListForDisplay.add(mEtCurrentUser);
        mETListForDisplay.add(mEtModule);
        mETListForDisplay.add(mEtVendor);
        mETListForDisplay.add(mEtType);
        mETListForDisplay.add(mEtSN);
        mETListForDisplay.add(mEtTestEquipId);
        mETListForDisplay.add(mEtWorkingConidtion);
        mETListForDisplay.add(mEtRemark);
        mETListForDisplay.add(mEtDeliverDate);
        mETListForDisplay.add(mEtCalibrationCycle);
        mETListForDisplay.add(mEtCalibrationReq);

        mEtQRCode.setFocusable(true);
        mEtQRCode.setFocusableInTouchMode(true);
        mEtQRCode.requestFocus();
    }

    private void setLinearLayoutSizeByPart() {
        if (null != listPart2 && listPart2.size() > 0) {
            setLinearLayoutSize(mLlpart2, listPart2.size());
        }
        if (null != listPart3 && listPart3.size() > 0) {
            setLinearLayoutSize(mLlpart3, listPart3.size());
        }

        if (null != listPart4 && listPart4.size() > 0) {
            setLinearLayoutSize(mLlpart4, listPart4.size());

        }
    }

    private void setLinearLayoutSize(LinearLayout layout,int size) {
        ViewGroup.LayoutParams params = layout.getLayoutParams();
        params.height = PXUtils.dip2px(AddNewOneActivity.this, 60 * size);
        layout.setLayoutParams(params);
    }

    private void initTitle() {
        setTitle("ION VLM");
        //TODO---左右图片
        //        setLeftDrawble();
        //        setRightDrawble();
        setTitleColor(getResources().getColor(R.color.color_nokia_blue));
    }


    public void updateDataMsg(int code, String msg, UpdateDataInfo responseJson) {
        if (code == 1 && responseJson != null) {
            ZLog.d("updateDataMsg", responseJson.getJsonStr());
            JsonElement jsonElement = new JsonParser().parse(responseJson.getJsonStr());
            String      returns     = jsonElement.toString();
            if ("Success".equals(returns)) {
                ToastUtils.getInstance().show("更新数据成功!");

            } else {
                ToastUtils.getInstance().show("更新数据失败,请重新操作!");
            }
        } else {
            ToastUtils.getInstance().show(msg);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregister();
    }

    private void unregister() {

    }

    private void addNewOne(String tableName) {
        ArrayList<ArrayList> lists           = new ArrayList<>();
        ArrayList            stringArrayList = new ArrayList<>();
        String[]             displayKeyArray = {""};
        String[]             keyArray        = {""};

        //"TestEquip_Asset", "Card_Asset", "Pluggable_Asset", "Shelf_Asset", "Computer_Asset"
        switch (tableName) {
            case "TestEquip_Asset":
                displayKeyArray = TestEquipAsset.arrayForDisplay;
                keyArray = TestEquipAsset.keyArray;
                break;
            case "Card_Asset":
                displayKeyArray = CardAsset.keyArray;
                keyArray = CardAsset.keyArray;

                break;
            case "Pluggable_Asset":
                displayKeyArray = PluggableAsset.keyArray;
                keyArray = PluggableAsset.keyArray;

                break;
            case "Shelf_Asset":
                displayKeyArray = ShelfAsset.keyArray;
                keyArray = ShelfAsset.keyArray;

                break;
            case "Computer_Asset":
                displayKeyArray = ComputerAsset.keyArray;
                keyArray = ComputerAsset.keyArray;
                break;
        }


        for (int i = 0; i < keyArray.length; i++) {
            for (int j = 0; j < displayKeyArray.length; j++) {
                String key = displayKeyArray[j];
                if (key.equals(keyArray[i].trim().toLowerCase())) {
                    EditText editText = mETListForAddNewOne.get(j);
                    String   text     = editText.getText().toString().trim();
                    if (editText.getId() == R.id.et_testEquipId) {
                        stringArrayList.add(-1);
                    } else {
                        stringArrayList.add(text);
                    }
                    break;
                }
            }

        }

        lists.add(stringArrayList);
        Map<String, ArrayList<ArrayList>> map = new HashMap<>();
        map.put(tableName, lists);
        Gson gson = new Gson();
        mTotalStrForAdd = gson.toJson(map);
        //        mTotalStrForAdd = gson.toJson(lists);
        ZLog.d("addNewOne", mTotalStrForAdd);
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (null != mTotalStrForAdd) {
//                    mUpdateDataEngine.postUpdateRequest(mTotalStrForAdd);
                    String  postResult = HttpWithoutJersey.sendPost("http://135.252.218.106:20200/api/update", mTotalStrForAdd);
                    Message msg        = new Message();
                    msg.what = 1;
                    msg.obj = postResult;
                    handler.sendMessage(msg);

                }
            }
        }).start();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_add:
                if (null != mTableNameForResultMap) {

                    addNewOne(mTableNameForResultMap);
                }
                break;
            case R.id.btn_reset:
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                alertDialogBuilder.setMessage("您确定要重置以上所有数据吗?")
                        .setCancelable(true)
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                resetEdittextContent(mETListForAddNewOne);
                            }
                        });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();//将dialog显示出来

                break;
            case R.id.iv_add_scan_qr:
            case R.id.iv_add_scan_area:
                //TODO---弹出扫描DialogFragment

                break;

        }
    }

    private void resetEdittextContent(List<EditText> list) {
        for (EditText editText : list) {
            editText.setText("");
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        /**
         * mSpinnerSite
         mSpinnerDepartment
         mSpinnerSubDepartment
         mSpinnerCurrentUser
         mSpinnerType
         mSpinnerWorkingConidtion
         *
         * */
        String selectedStr = "";
        switch (view.getId()) {
            case R.id.spinner_site:
                 selectedStr = getResources().getStringArray(R.array.nameSite)[position];
                mEtSite.setText(selectedStr);
                break;
            case R.id.spinner_department:
                 selectedStr = getResources().getStringArray(R.array.nameDep)[position];
                mEtDepartment.setText(selectedStr);
                break;
            case R.id.spinner_subdepartment:
                 selectedStr = getResources().getStringArray(R.array.nameSubDepAll)[position];
                mEtSubDepartment.setText(selectedStr);
                break;
            case R.id.spinner_current_user:
//                 selectedStr = getResources().getStringArray(R.array.namec)[position];
//                mEtCurrentUser.setText(selectedStr);
                break;
            case R.id.spinner_type:
                 selectedStr = getResources().getStringArray(R.array.nameType)[position];
                mEtType.setText(selectedStr);
                break;
            case R.id.spinner_workingconditon:
                 selectedStr = getResources().getStringArray(R.array.nameWorkCondition)[position];
                mEtWorkingConidtion.setText(selectedStr);
                break;


        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
