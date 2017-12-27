package com.nokia.vlm.ui.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.nokia.vlm.R;
import com.nokia.vlm.entity.CardAsset;
import com.nokia.vlm.entity.ComputerAsset;
import com.nokia.vlm.entity.PluggableAsset;
import com.nokia.vlm.entity.SerializableMap;
import com.nokia.vlm.entity.ShelfAsset;
import com.nokia.vlm.entity.TestEquipAsset;
import com.nokia.vlm.entity.UpdateDataInfo;
import com.nokia.vlm.entity.VLMUpdateData;
import com.nokia.vlm.ui.fragment.UpdateDialogFragment;
import com.nokia.vlm.ui.fragment.UpdateDialogFragmentOthers;
import com.nokia.vlm.utils.HttpWithoutJersey;
import com.qx.framelib.activity.BaseTitle2Activity;
import com.qx.framelib.utlis.TextUtil;
import com.qx.framelib.utlis.ToastUtils;
import com.qx.framelib.utlis.ZLog;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @创建者 DK-house
 * @创建时间 2017/11/13 23:57
 * @描述 ${TODO}
 * @更新者 ${AUTHOR}
 * @更新时间 2017/11/13
 * @更新描述 ${TODO}
 */

public class QRResultActivity extends BaseTitle2Activity {
    public static final String[] tables           = {"TestEquip_Asset", "Card_Asset", "Pluggable_Asset", "Shelf_Asset", "Computer_Asset"};
    public static final String   NAMEID           = "name";
    public static final String   EDITTEXT_TEXT    = "text";
    public static final String   RELATIVELAYOUTID = "rl_id";
    private List<RelativeLayout> mRlListForDisplay;
    private List<RelativeLayout> mRlListForAsset;
    private List<EditText>       mETListForDisplay;
    private List<TextView>       mTvListForDisplay;


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

    private EditText            mEtQRCode;
    private EditText            mEtArea;
    private EditText            mEtSN;
    private EditText            mEtAssetId;
    private EditText            mEtSite;
    private EditText            mEtDepartment;
    private EditText            mEtSubDepartment;
    private EditText            mEtCurrentUser;
    private EditText            mEtModule;
    private EditText            mEtVendor;
    private EditText            mEtType;
    private EditText            mEtDescription;
    private EditText            mEtTestEquipId;
    private EditText            mEtWorkingConidtion;
    private EditText            mEtRemark;
    private EditText            mEtDeliverDate;
    private EditText            mEtCalibrationCycle;
    private EditText            mEtCalibrationReq;
    //    public  boolean  mResultNotNull;
    private Map<String, String> mResultMapForKey;
    private String              mTableNameForResultMap;
    private String              mTotalStrForAdd;
    private int                 mIndexForUpdateLayout;

    private TextView mTvQRCode;
    private TextView mTvArea;
    private TextView mTvSN;
    private TextView mTvAssetId;
    private TextView mTvSite;
    private TextView mTvDepartment;
    private TextView mTvSubDepartment;
    private TextView mTvCurrentUser;
    private TextView mTvModule;
    private TextView mTvVendor;
    private TextView mTvType;
    private TextView mTvDescription;
    private TextView mTvTestEquipId;
    private TextView mTvWorkingConidtion;
    private TextView mTvRemark;
    private TextView mTvDeliverDate;
    private TextView mTvCalibrationCycle;
    private TextView mTvCalibrationReq;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String[] results = (String[]) msg.obj;
            switch (msg.what) {
                case 2:
                    updateDataMsg(1, "Successful", new UpdateDataInfo(results[0], results[1]));
                    break;
            }

        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        isStatusBarEnald = true;
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.layout_activity_qr_result);

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
        Serializable      value         = bundle.getSerializable(QRCodeCaptureActivity.QR_RESULT_MAP);
        String            qrKey         = bundle.getString(QRCodeCaptureActivity.QR_RESULT_KEY);
        ArrayList<String> tableNameList = bundle.getStringArrayList(QRCodeCaptureActivity.QR_RESULT_TABLENAME);


        mTableNameForResultMap = null;

        if (null != tableNameList && tableNameList.size() == 1) {
            mTableNameForResultMap = tableNameList.get(0);
        }
        if (null != mTableNameForResultMap) {
            if (null != value) {
                mResultMapForKey = ((SerializableMap) value).getMap();
            }
            displaySearchResult(mResultMapForKey, mTableNameForResultMap);

        }
        //mRlListForDisplay
        //mETListForDisplay
        onLongClickForUpdate();


    }

    private void onLongClickForUpdate() {
        for (final RelativeLayout relativeLayout : mRlListForDisplay) {
            final int relativeLayoutId = relativeLayout.getId();


            relativeLayout.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    //add操作时没有长按update,因为需要插入一条数据,这时testEquip_Id必须为-1;存在长按update的话需要分是首次插入数据还是非首次
                    // ,又增加了查询匹配且较为复杂.可待日后完善.

                    Bundle bundle = new Bundle();
                    mIndexForUpdateLayout = mRlListForDisplay.indexOf(relativeLayout);

                    if (relativeLayoutId == R.id.rl_area || relativeLayoutId == R.id.rl_qr_code) {
                        mIndexForUpdateLayout = mRlListForDisplay.indexOf(relativeLayout);
                        final UpdateDialogFragment updateDialogFragment = new UpdateDialogFragment(QRResultActivity.this, relativeLayoutId, true);
                        bundle.putString(EDITTEXT_TEXT, mETListForDisplay.get(mIndexForUpdateLayout).getText().toString().trim());
                        updateDialogFragment.setArguments(bundle);
                        updateDialogFragment.show(getFragmentManager(), "updateDialogFragment");
                        updateDialogFragment.setOnClickListener(new UpdateDialogFragment.OnClickListener() {
                            @Override
                            public void onLeftClick() {

                            }

                            @Override
                            public void onRightClick() {
                                ToastUtils.getInstance().show("Right Right Right ");
                                String newData = updateDialogFragment.getEtTobeUpdated().getText().toString().trim();
                                if (null != mResultMapForKey && null != mTableNameForResultMap) {
                                    String value = mTvListForDisplay.get(mIndexForUpdateLayout).getText().toString().trim().toLowerCase();
                                    if ("sn".equals(value)) {
                                        value = "serialnumber";
                                    }
                                    if (null != newData && null != value) {
                                        mResultMapForKey.put(value, newData);
                                    }
                                    updateData(mResultMapForKey, mTableNameForResultMap, newData);

                                }

                            }
                        });

                    } else {
                        final UpdateDialogFragmentOthers updateFragmentOthers = new UpdateDialogFragmentOthers(QRResultActivity.this);
                        bundle.putString(NAMEID, mTvListForDisplay.get(mIndexForUpdateLayout).getText().toString().trim());
                        bundle.putInt(RELATIVELAYOUTID, relativeLayoutId);
                        bundle.putString(EDITTEXT_TEXT, mETListForDisplay.get(mIndexForUpdateLayout).getText().toString().trim());
                        updateFragmentOthers.setArguments(bundle);
                        updateFragmentOthers.show(getFragmentManager(), "updateDialogFragment");
                        updateFragmentOthers.setOnClickListener(new UpdateDialogFragmentOthers.OnClickListener() {
                            @Override
                            public void onLeftClick() {
                                ToastUtils.getInstance().show("Others Left Left Left ");
                                updateFragmentOthers.dismiss();
                            }

                            @Override
                            public void onRightClick() {
                                ToastUtils.getInstance().show("Others Right Right Right ");
                                String newData = updateFragmentOthers.getEtTobeUpdated().getText().toString().trim();
                                if (null != mResultMapForKey && null != mTableNameForResultMap) {
                                    String value = mTvListForDisplay.get(mIndexForUpdateLayout).getText().toString().trim().toLowerCase();
                                    if ("sn".equals(value)) {
                                        value = "serialnumber";
                                    }
                                    if (null != newData && null != value) {
                                        mResultMapForKey.put(value, newData);
                                    }

                                    updateData(mResultMapForKey, mTableNameForResultMap, newData);
                                }
                            }
                        });
                    }

                    return true;
                }
            });
        }
    }

    private void displaySearchResult(Map<String, String> resultMapForKey, String resultMapName) {
        switch (resultMapName) {
            case "TestEquip_Asset":

                displayTestEquipAsset(resultMapForKey);

                break;
            case "Card_Asset":
                displayCardAsset(resultMapForKey);
                break;
            case "Pluggable_Asset":
                displayPluggabelAsset(resultMapForKey);
                break;
            case "Shelf_Asset":
                displayShelfAsset(resultMapForKey);
                break;
            case "Computer_Asset":
                displayComputerAsset(resultMapForKey);
                break;
        }
    }


    private void displayComputerAsset(Map<String, String> resultMapForKey) {
        displayResultForAssets(resultMapForKey, ComputerAsset.keyArray);

    }

    private void displayShelfAsset(Map<String, String> resultMapForKey) {
        displayResultForAssets(resultMapForKey, ShelfAsset.keyArray);
    }

    private void displayPluggabelAsset(Map<String, String> resultMapForKey) {
        displayResultForAssets(resultMapForKey, PluggableAsset.keyArray);
    }

    private void displayCardAsset(Map<String, String> resultMapForKey) {
        displayResultForAssets(resultMapForKey, CardAsset.keyArray);
    }

    private void displayTestEquipAsset(Map<String, String> resultMapForKey) {
        if (null != resultMapForKey) {
            displayResultForAssets(resultMapForKey, TestEquipAsset.arrayForDisplay);
        }
    }


    private void displayResultForAssets(Map<String, String> resultMapForKey, String[] keyArray) {
        Set<String>  keySet     = resultMapForKey.keySet();
        List<String> temKeyList = new ArrayList<>();
        temKeyList.addAll(keySet);
        for (int i = 0; i < keyArray.length; i++) {
            if (i > keyArray.length - 1) {
                return;
            }
            for (int j = 0; j < temKeyList.size(); j++) {
                if (j > temKeyList.size() - 1) {
                    break;
                }
                if (keyArray[i].equals(temKeyList.get(j))) {
                    mRlListForDisplay.get(i).setVisibility(View.VISIBLE);
                    String text = resultMapForKey.get(temKeyList.get(j));
                    if (!TextUtil.isEmpty(text)) {
                        if (text.length() <= 21) {
                            mETListForDisplay.get(i).setText(text);
                        } else {
                            mETListForDisplay.get(i).setText((new StringBuilder(text.substring(0, 21))).append("\n").append(text.substring(21)));
                        }

                    }

                    break;
                } else {
                    mRlListForDisplay.get(i).setVisibility(View.GONE);
                }
            }
        }
    }


    private void initView() {
        mRlListForDisplay = new ArrayList<>();
        mETListForDisplay = new ArrayList<>();
        mTvListForDisplay = new ArrayList<>();

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
        mEtDepartment = (EditText) findViewById(R.id.et_department);
        mEtSubDepartment = (EditText) findViewById(R.id.et_subdepartment);
        mEtCurrentUser = (EditText) findViewById(R.id.et_current_user);
        mEtModule = (EditText) findViewById(R.id.et_module);
        mEtVendor = (EditText) findViewById(R.id.et_vendor);
        mEtType = (EditText) findViewById(R.id.et_type);
        mEtDescription = (EditText) findViewById(R.id.et_description);
        mEtTestEquipId = (EditText) findViewById(R.id.et_testEquipId);
        mEtWorkingConidtion = (EditText) findViewById(R.id.et_workingconditon);
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


        mTvQRCode = (TextView) findViewById(R.id.tv_qr_code);
        mTvArea = (TextView) findViewById(R.id.tv_area);
        mTvSN = (TextView) findViewById(R.id.tv_sn);
        mTvAssetId = (TextView) findViewById(R.id.tv_asset_id);
        mTvSite = (TextView) findViewById(R.id.tv_site);
        mTvDepartment = (TextView) findViewById(R.id.tv_department);
        mTvSubDepartment = (TextView) findViewById(R.id.tv_subdepartment);
        mTvCurrentUser = (TextView) findViewById(R.id.tv_current_user);
        mTvModule = (TextView) findViewById(R.id.tv_module);
        mTvVendor = (TextView) findViewById(R.id.tv_vendor);
        mTvType = (TextView) findViewById(R.id.tv_type);
        mTvDescription = (TextView) findViewById(R.id.tv_description);
        mTvTestEquipId = (TextView) findViewById(R.id.tv_testEquipId);
        mTvWorkingConidtion = (TextView) findViewById(R.id.tv_workingconditon);
        mTvRemark = (TextView) findViewById(R.id.tv_remark);
        mTvDeliverDate = (TextView) findViewById(R.id.tv_deliver_date);
        mTvCalibrationCycle = (TextView) findViewById(R.id.tv_calibration_cycle);
        mTvCalibrationReq = (TextView) findViewById(R.id.tv_calibration_req);

        mTvListForDisplay.add(mTvQRCode);
        mTvListForDisplay.add(mTvArea);
        mTvListForDisplay.add(mTvDescription);
        mTvListForDisplay.add(mTvAssetId);
        mTvListForDisplay.add(mTvSite);
        mTvListForDisplay.add(mTvDepartment);
        mTvListForDisplay.add(mTvSubDepartment);
        mTvListForDisplay.add(mTvCurrentUser);
        mTvListForDisplay.add(mTvModule);
        mTvListForDisplay.add(mTvVendor);
        mTvListForDisplay.add(mTvType);
        mTvListForDisplay.add(mTvSN);
        mTvListForDisplay.add(mTvTestEquipId);
        mTvListForDisplay.add(mTvWorkingConidtion);
        mTvListForDisplay.add(mTvRemark);
        mTvListForDisplay.add(mTvDeliverDate);
        mTvListForDisplay.add(mTvCalibrationCycle);
        mTvListForDisplay.add(mTvCalibrationReq);

    }

    private void initTitle() {
        setTitle("ION VLM");
        //TODO---左右图片
        //        setLeftDrawble();
        //        setRightDrawble();
        setTitleColor(getResources().getColor(R.color.color_nokia_blue));
    }


    public void updateDataMsg(int code, String msg, UpdateDataInfo response) {
        if (code == 1 && response != null) {
            ZLog.d("updateDataMsg", response.getJsonStr());
            Map    parse   = (Map) JSON.parse(response.getJsonStr());
            Object obj     = parse.get("Result");
            String returns = obj.toString();

            if ("Successful".equals(returns)) {
                mETListForDisplay.get(mIndexForUpdateLayout).setText(response.getNewData());
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

    //TODO---update操作
    private void updateData(Map<String, String> resultMapForKey, String tableName, String newData) {
        ArrayList<ArrayList> lists           = new ArrayList<>();
        VLMUpdateData        vlmUpdateData   = new VLMUpdateData();
        Collection<String>   values          = resultMapForKey.values();
        ArrayList<String>    stringArrayList = new ArrayList<>();
        stringArrayList.addAll(values);
        lists.add(stringArrayList);
        switch (tableName) {//"TestEquip_Asset", "Card_Asset", "Pluggable_Asset", "Shelf_Asset", "Computer_Asset"
            case "TestEquip_Asset":
                lists.add(stringArrayList);
                vlmUpdateData.TestEquip_Asset = lists;
                break;
            case "Card_Asset":
                lists.add(stringArrayList);
                vlmUpdateData.Card_Asset = lists;
                break;
            case "Pluggable_Asset":
                lists.add(stringArrayList);
                vlmUpdateData.Pluggable_Asset = lists;
                break;
            case "Shelf_Asset":
                lists.add(stringArrayList);
                vlmUpdateData.Shelf_Asset = lists;
                break;
            case "Computer_Asset":
                lists.add(stringArrayList);
                vlmUpdateData.Computer_Asset = lists;
                break;
        }
        //        Map<String, ArrayList<ArrayList>> map = new HashMap<>();
        //        map.put(tableName, lists);
        //        Gson         gson   = new Gson();
        //        final String update = gson.toJson(map);

        Gson         gson   = new Gson();
        final String update = gson.toJson(vlmUpdateData);
        final String data   = newData;
        new Thread(new Runnable() {
            @Override
            public void run() {
                String postResult = HttpWithoutJersey.sendPost("http://135.252.218.106:20200/api/update", update);
                //                String postResult = "{\"Result\": \"Successful\"}";//离线测试用
                String[] strings = new String[]{postResult, data};
                Message  msg     = new Message();
                msg.what = 2;
                msg.obj = strings;
                handler.sendMessage(msg);
            }
        }).start();
    }


}
