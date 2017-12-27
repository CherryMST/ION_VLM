package com.nokia.vlm.ui.activity;

import android.os.Bundle;
import android.os.Parcelable;

import com.nokia.vlm.R;
import com.nokia.vlm.ui.fragment.HomeFragment;
import com.qx.framelib.activity.BaseTitle2Activity;

/**
 * @创建者 DK-house
 * @创建时间 2017/11/19 20:30
 * @描述 ${TODO}
 * @更新者 ${AUTHOR}
 * @更新时间 2017/11/19
 * @更新描述 ${TODO}
 */

public class HomeSelectResultMoreDetailActivity extends BaseTitle2Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_result_item);

        initTitle();
        initView();
        initData();
    }

    private void initTitle() {
        setTitle("");
        setTitleColor(getResources().getColor(R.color.color_nokia_blue));
    }

    private void initView() {


    }

    private void initData() {
        Bundle bundle = getIntent().getExtras();
        if (bundle == null) return;
        Parcelable value = bundle.getParcelable(HomeFragment.RESULT_RESULT_ITEM);
        /*if (null == value) {
            return;
        }
        int textCounts = 0;
        if (value.getClass().equals(CardAssetItemInfo.class) ) {
            textCounts = 12;
            CardAssetItemInfo itemData = (CardAssetItemInfo) value;
            disPlayCardAssetResult(itemData);

        } else if (value.getClass().equals(ComputerAssetItemInfo.class)) {
            textCounts = 16;
            ComputerAssetItemInfo itemData = (ComputerAssetItemInfo)value;
            disPlayComputerAssetResult(itemData);

        }else if (value.getClass().equals(PluggableAssetItemInfo.class)) {
            textCounts = 13;
            PluggableAssetItemInfo itemData = (PluggableAssetItemInfo)value;
            disPlayPluggableAssetResult(itemData);

        }else if (value.getClass().equals(ShelfAssetItemInfo.class)) {
            textCounts = 12;
            ShelfAssetItemInfo itemData = (ShelfAssetItemInfo)value;
            disPlayShelfAssetResult(itemData);
        }else if (value.getClass().equals(TestEquipAssetItemInfo.class)) {
            textCounts = 18;
            TestEquipAssetItemInfo itemData = (TestEquipAssetItemInfo)value;

            disPlayTestEquipAssetResult(itemData);
        }*/

    }

    /*private void disPlayCardAssetResult(CardAssetItemInfo itemData) {
        ((TextView)findViewById(R.id.textView0)).setText(CardAsset.keyArray[0]);
        ((TextView)findViewById(R.id.textView1)).setText(CardAsset.keyArray[1]);
        ((TextView)findViewById(R.id.textView2)).setText(CardAsset.keyArray[2]);
        ((TextView)findViewById(R.id.textView3)).setText(CardAsset.keyArray[3]);
        ((TextView)findViewById(R.id.textView4)).setText(CardAsset.keyArray[4]);
        ((TextView)findViewById(R.id.textView5)).setText(CardAsset.keyArray[5]);
        ((TextView)findViewById(R.id.textView6)).setText(CardAsset.keyArray[6]);
        ((TextView)findViewById(R.id.textView7)).setText(CardAsset.keyArray[7]);
        ((TextView)findViewById(R.id.textView8)).setText(CardAsset.keyArray[8]);
        ((TextView)findViewById(R.id.textView9)).setText(CardAsset.keyArray[9]);
        ((TextView)findViewById(R.id.textView10)).setText(CardAsset.keyArray[10]);
        ((TextView)findViewById(R.id.textView11)).setText(CardAsset.keyArray[11]);

        findViewById(R.id.textView12).setVisibility(View.GONE);
        findViewById(R.id.textView13).setVisibility(View.GONE);
        findViewById(R.id.textView14).setVisibility(View.GONE);
        findViewById(R.id.textView15).setVisibility(View.GONE);
        findViewById(R.id.textView16).setVisibility(View.GONE);
        findViewById(R.id.textView17).setVisibility(View.GONE);

        ((EditText)findViewById(R.id.editText0)).setText(itemData.getQr());
        ((EditText)findViewById(R.id.editText1)).setText(itemData.getArea());
        ((EditText)findViewById(R.id.editText2)).setText(itemData.getDescription());
        ((EditText)findViewById(R.id.editText3)).setText(itemData.getDepartment());
        ((EditText)findViewById(R.id.editText4)).setText(itemData.getSubdepartment());
        ((EditText)findViewById(R.id.editText5)).setText(itemData.getType());
        ((EditText)findViewById(R.id.editText6)).setText(itemData.getDeliverdate());
        ((EditText)findViewById(R.id.editText7)).setText(itemData.getAsset_id());
        ((EditText)findViewById(R.id.editText8)).setText(itemData.getSite());
        ((EditText)findViewById(R.id.editText9)).setText(itemData.getSeriralnumber());
        ((EditText)findViewById(R.id.editText1)).setText(itemData.getCurrentuser());
        ((EditText)findViewById(R.id.editText1)).setText(itemData.getWorkingcondition());

        findViewById(R.id.editText12).setVisibility(View.GONE);
        findViewById(R.id.editText13).setVisibility(View.GONE);
        findViewById(R.id.editText14).setVisibility(View.GONE);
        findViewById(R.id.editText15).setVisibility(View.GONE);
        findViewById(R.id.editText16).setVisibility(View.GONE);
        findViewById(R.id.editText17).setVisibility(View.GONE);
    }

    private void disPlayComputerAssetResult(ComputerAssetItemInfo itemData) {
        ((TextView)findViewById(R.id.textView0)).setText(ComputerAsset.keyArray[0]);
        ((TextView)findViewById(R.id.textView1)).setText(ComputerAsset.keyArray[1]);
        ((TextView)findViewById(R.id.textView2)).setText(ComputerAsset.keyArray[2]);
        ((TextView)findViewById(R.id.textView3)).setText(ComputerAsset.keyArray[3]);
        ((TextView)findViewById(R.id.textView4)).setText(ComputerAsset.keyArray[4]);
        ((TextView)findViewById(R.id.textView5)).setText(ComputerAsset.keyArray[5]);
        ((TextView)findViewById(R.id.textView6)).setText(ComputerAsset.keyArray[6]);
        ((TextView)findViewById(R.id.textView7)).setText(ComputerAsset.keyArray[7]);
        ((TextView)findViewById(R.id.textView8)).setText(ComputerAsset.keyArray[8]);
        ((TextView)findViewById(R.id.textView9)).setText(ComputerAsset.keyArray[9]);
        ((TextView)findViewById(R.id.textView10)).setText(ComputerAsset.keyArray[10]);
        ((TextView)findViewById(R.id.textView11)).setText(ComputerAsset.keyArray[11]);
        ((TextView)findViewById(R.id.textView12)).setText(ComputerAsset.keyArray[12]);
        ((TextView)findViewById(R.id.textView13)).setText(ComputerAsset.keyArray[13]);
        ((TextView)findViewById(R.id.textView14)).setText(ComputerAsset.keyArray[14]);
        ((TextView)findViewById(R.id.textView15)).setText(ComputerAsset.keyArray[15]);
        findViewById(R.id.textView16).setVisibility(View.GONE);
        findViewById(R.id.textView17).setVisibility(View.GONE);

        ((EditText) findViewById(R.id.editText0)).setText(itemData.getQr());
        ((EditText) findViewById(R.id.editText1)).setText(itemData.getArea());
        ((EditText) findViewById(R.id.editText2)).setText(itemData.getDepartment());
        ((EditText) findViewById(R.id.editText3)).setText(itemData.getSubdepartment());
        ((EditText) findViewById(R.id.editText4)).setText(itemData.getType());
        ((EditText) findViewById(R.id.editText5)).setText(itemData.getModule());
        ((EditText) findViewById(R.id.editText6)).setText(itemData.getDescription());
        ((EditText) findViewById(R.id.editText7)).setText(itemData.getRemark());
        ((EditText) findViewById(R.id.editText8)).setText(itemData.getDeliverdate());
        ((EditText) findViewById(R.id.editText9)).setText(itemData.getBrand());
        ((EditText) findViewById(R.id.editText10)).setText(itemData.getIp());
        ((EditText) findViewById(R.id.editText11)).setText(itemData.getAsset_id());
        ((EditText) findViewById(R.id.editText12)).setText(itemData.getSite());
        ((EditText) findViewById(R.id.editText13)).setText(itemData.getSeriralnumber());
        ((EditText) findViewById(R.id.editText14)).setText(itemData.getCurrentuser());
        ((EditText) findViewById(R.id.editText15)).setText(itemData.getWorkingcondition());
        findViewById(R.id.editText16).setVisibility(View.GONE);
        findViewById(R.id.editText17).setVisibility(View.GONE);
    }

    private void disPlayPluggableAssetResult(PluggableAssetItemInfo itemData) {
        ((TextView)findViewById(R.id.textView0)).setText(PluggableAsset.keyArray[0]);
        ((TextView)findViewById(R.id.textView1)).setText(PluggableAsset.keyArray[1]);
        ((TextView)findViewById(R.id.textView2)).setText(PluggableAsset.keyArray[2]);
        ((TextView)findViewById(R.id.textView3)).setText(PluggableAsset.keyArray[3]);
        ((TextView)findViewById(R.id.textView4)).setText(PluggableAsset.keyArray[4]);
        ((TextView)findViewById(R.id.textView5)).setText(PluggableAsset.keyArray[5]);
        ((TextView)findViewById(R.id.textView6)).setText(PluggableAsset.keyArray[6]);
        ((TextView)findViewById(R.id.textView7)).setText(PluggableAsset.keyArray[7]);
        ((TextView)findViewById(R.id.textView8)).setText(PluggableAsset.keyArray[8]);
        ((TextView)findViewById(R.id.textView9)).setText(PluggableAsset.keyArray[9]);
        ((TextView)findViewById(R.id.textView10)).setText(PluggableAsset.keyArray[10]);
        ((TextView)findViewById(R.id.textView11)).setText(PluggableAsset.keyArray[11]);
        ((TextView)findViewById(R.id.textView12)).setText(PluggableAsset.keyArray[12]);
        findViewById(R.id.textView13).setVisibility(View.GONE);
        findViewById(R.id.textView14).setVisibility(View.GONE);
        findViewById(R.id.textView15).setVisibility(View.GONE);
        findViewById(R.id.textView16).setVisibility(View.GONE);
        findViewById(R.id.textView17).setVisibility(View.GONE);

        ((EditText)findViewById(R.id.editText0)).setText(itemData.getQr());
        ((EditText)findViewById(R.id.editText1)).setText(itemData.getArea());
        ((EditText)findViewById(R.id.editText2)).setText(itemData.getAsset_id());
        ((EditText)findViewById(R.id.editText3)).setText(itemData.getSite());
        ((EditText)findViewById(R.id.editText4)).setText(itemData.getDepartment());
        ((EditText)findViewById(R.id.editText5)).setText(itemData.getSubdepartment());
        ((EditText)findViewById(R.id.editText6)).setText(itemData.getType());
        ((EditText)findViewById(R.id.editText7)).setText(itemData.getSeriralnumber());
        ((EditText)findViewById(R.id.editText8)).setText(itemData.getWorkingcondition());
        ((EditText)findViewById(R.id.editText9)).setText(itemData.getDescription());
        ((EditText)findViewById(R.id.editText10)).setText(itemData.getRemark());
        ((EditText)findViewById(R.id.editText11)).setText(itemData.getCurrentuser());
        ((EditText)findViewById(R.id.editText12)).setText(itemData.getDeliverdate());
        findViewById(R.id.editText13).setVisibility(View.GONE);
        findViewById(R.id.editText14).setVisibility(View.GONE);
        findViewById(R.id.editText15).setVisibility(View.GONE);
        findViewById(R.id.editText16).setVisibility(View.GONE);
        findViewById(R.id.editText17).setVisibility(View.GONE);
    }

    private void disPlayShelfAssetResult(ShelfAssetItemInfo itemData) {
        ((TextView)findViewById(R.id.textView0)).setText(ShelfAsset.keyArray[0]);
        ((TextView)findViewById(R.id.textView1)).setText(ShelfAsset.keyArray[1]);
        ((TextView)findViewById(R.id.textView2)).setText(ShelfAsset.keyArray[2]);
        ((TextView)findViewById(R.id.textView3)).setText(ShelfAsset.keyArray[3]);
        ((TextView)findViewById(R.id.textView4)).setText(ShelfAsset.keyArray[4]);
        ((TextView)findViewById(R.id.textView5)).setText(ShelfAsset.keyArray[5]);
        ((TextView)findViewById(R.id.textView6)).setText(ShelfAsset.keyArray[6]);
        ((TextView)findViewById(R.id.textView7)).setText(ShelfAsset.keyArray[7]);
        ((TextView)findViewById(R.id.textView8)).setText(ShelfAsset.keyArray[8]);
        ((TextView)findViewById(R.id.textView9)).setText(ShelfAsset.keyArray[9]);
        ((TextView)findViewById(R.id.textView10)).setText(ShelfAsset.keyArray[10]);
        ((TextView)findViewById(R.id.textView11)).setText(ShelfAsset.keyArray[11]);
        findViewById(R.id.textView12).setVisibility(View.GONE);
        findViewById(R.id.textView13).setVisibility(View.GONE);
        findViewById(R.id.textView14).setVisibility(View.GONE);
        findViewById(R.id.textView15).setVisibility(View.GONE);
        findViewById(R.id.textView16).setVisibility(View.GONE);
        findViewById(R.id.textView17).setVisibility(View.GONE);




        ((EditText)findViewById(R.id.editText0)).setText(itemData.getQr());
        ((EditText)findViewById(R.id.editText1)).setText(itemData.getArea());
        ((EditText)findViewById(R.id.editText2)).setText(itemData.getDescription());
        ((EditText)findViewById(R.id.editText3)).setText(itemData.getDepartment());
        ((EditText)findViewById(R.id.editText4)).setText(itemData.getSubdepartment());
        ((EditText)findViewById(R.id.editText5)).setText(itemData.getType());
        ((EditText)findViewById(R.id.editText6)).setText(itemData.getDeliverdate());
        ((EditText)findViewById(R.id.editText7)).setText(itemData.getAsset_id());
        ((EditText)findViewById(R.id.editText8)).setText(itemData.getSite());
        ((EditText)findViewById(R.id.editText9)).setText(itemData.getSeriralnumber());
        ((EditText)findViewById(R.id.editText10)).setText(itemData.getCurrentuser());
        ((EditText)findViewById(R.id.editText11)).setText(itemData.getWorkingcondition());
        findViewById(R.id.editText12).setVisibility(View.GONE);
        findViewById(R.id.editText13).setVisibility(View.GONE);
        findViewById(R.id.editText14).setVisibility(View.GONE);
        findViewById(R.id.editText15).setVisibility(View.GONE);
        findViewById(R.id.editText16).setVisibility(View.GONE);
        findViewById(R.id.editText17).setVisibility(View.GONE);
    }

    private void disPlayTestEquipAssetResult(TestEquipAssetItemInfo itemData) {
        ((TextView)findViewById(R.id.textView0)).setText(TestEquipAsset.keyArray[0]);
        ((TextView)findViewById(R.id.textView1)).setText(TestEquipAsset.keyArray[1]);
        ((TextView)findViewById(R.id.textView2)).setText(TestEquipAsset.keyArray[2]);
        ((TextView)findViewById(R.id.textView3)).setText(TestEquipAsset.keyArray[3]);
        ((TextView)findViewById(R.id.textView4)).setText(TestEquipAsset.keyArray[4]);
        ((TextView)findViewById(R.id.textView5)).setText(TestEquipAsset.keyArray[5]);
        ((TextView)findViewById(R.id.textView6)).setText(TestEquipAsset.keyArray[6]);
        ((TextView)findViewById(R.id.textView7)).setText(TestEquipAsset.keyArray[7]);
        ((TextView)findViewById(R.id.textView8)).setText(TestEquipAsset.keyArray[8]);
        ((TextView)findViewById(R.id.textView9)).setText(TestEquipAsset.keyArray[9]);
        ((TextView)findViewById(R.id.textView10)).setText(TestEquipAsset.keyArray[10]);
        ((TextView)findViewById(R.id.textView11)).setText(TestEquipAsset.keyArray[11]);
        ((TextView)findViewById(R.id.textView12)).setText(TestEquipAsset.keyArray[12]);
        ((TextView)findViewById(R.id.textView13)).setText(TestEquipAsset.keyArray[13]);
        ((TextView)findViewById(R.id.textView14)).setText(TestEquipAsset.keyArray[14]);
        ((TextView)findViewById(R.id.textView15)).setText(TestEquipAsset.keyArray[15]);
        ((TextView)findViewById(R.id.textView16)).setText(TestEquipAsset.keyArray[16]);
        ((TextView)findViewById(R.id.textView17)).setText(TestEquipAsset.keyArray[17]);

        ((EditText)findViewById(R.id.editText0)).setText(itemData.getTestequip_id());
        ((EditText)findViewById(R.id.editText1)).setText(itemData.getQr());
        ((EditText)findViewById(R.id.editText2)).setText(itemData.getArea());
        ((EditText)findViewById(R.id.editText3)).setText(itemData.getAsset_id());
        ((EditText)findViewById(R.id.editText4)).setText(itemData.getSite());
        ((EditText)findViewById(R.id.editText5)).setText(itemData.getDepartment());
        ((EditText)findViewById(R.id.editText6)).setText(itemData.getSubdepartment());
        ((EditText)findViewById(R.id.editText7)).setText(itemData.getType());
        ((EditText)findViewById(R.id.editText8)).setText(itemData.getVendor());
        ((EditText)findViewById(R.id.editText9)).setText(itemData.getSeriralnumber());
        ((EditText)findViewById(R.id.editText10)).setText(itemData.getModule());
        ((EditText)findViewById(R.id.editText11)).setText(itemData.getWorkingcondition());
        ((EditText)findViewById(R.id.editText12)).setText(itemData.getDescription());
        ((EditText)findViewById(R.id.editText13)).setText(itemData.getRemark());
        ((EditText)findViewById(R.id.editText14)).setText(itemData.getCurrentuser());
        ((EditText)findViewById(R.id.editText15)).setText(itemData.getDeliverdate());
        ((EditText)findViewById(R.id.editText16)).setText(itemData.getCalibrationcycle());
        ((EditText)findViewById(R.id.editText17)).setText(itemData.getCalibrationreq());
    }*/
}
