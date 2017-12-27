package com.nokia.vlm.ui.activity;

import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.nokia.vlm.R;
import com.nokia.vlm.entity.Bean;
import com.nokia.vlm.entity.CodeCaptureInfo;
import com.nokia.vlm.ui.adapter.SearchBySelectionRecycleViewAdapter;
import com.nokia.vlm.ui.adapter.SearchSelectionsAdapter;
import com.nokia.vlm.ui.adapter.SelectionExpandListAdapter;
import com.nokia.vlm.ui.fragment.HomeFragment;
import com.nokia.vlm.utils.ListViewUtils;
import com.nokia.vlm.utils.MapDataUtils;
import com.nokia.vlm.utils.PXUtils;
import com.nokia.vlm.view.NoScrollGridView;
import com.nokia.vlm.view.SelectionExpandGridViewAdapter;
import com.qx.framelib.activity.BaseTitle2Activity;
import com.qx.framelib.utlis.TextUtil;
import com.qx.framelib.view.TitleBar2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import static com.nokia.vlm.ui.fragment.HomeFragment.ALL_ASSET;

/**
 * @创建者 DK-house
 * @创建时间 2017/12/8 15:51
 * @描述 ${TODO}
 * @更新者 ${AUTHOR}
 * @更新时间 2017/12/8
 * @更新描述 ${TODO}
 */

public class SearchBySelectionActivity extends BaseTitle2Activity implements View.OnClickListener, SearchBySelectionRecycleViewAdapter.SearchSelectionsClick{
    //各条件选择形成最终的筛选条件
    private HashMap<String, String> mFilterMap           = new HashMap<>();
    private HashSet<String>         mSearchConditionsSet = new HashSet<>();
    private HashSet<String>         mSearchCalResultSet  = new HashSet<>();
    private List<RadioButton>       mRadioButtons        = new ArrayList<>();
    private String mSearchEdittext;

    private int                      mLastCheckId     = -1;
    private HashMap<Integer, String> mTotoalForSearch = new HashMap<>();

    private Button                                         mBtnCancel;
    private Button                                         mBtnSearch;
    private ListView                                       mListViewSelections;
    private LinearLayout                                   mLLSelectionResult;
    private ScrollView                                     mScrollView;
    private EditText                                       mEdInEdittextType;
    private NoScrollGridView                               mGridInEdittextType;
    private ListView                                       mListViewInEdittextType;
    private SearchSelectionsAdapter                        mSearchSelectionsAdapter;
    private List<RadioButton>                              mSelectionButtonsList;
    private EditText                                       mExpandEditText;
    private SelectionExpandGridViewAdapter                 mExpandGridViewAdapter;
    private SelectionExpandListAdapter                     mExpandListAdapter;
    private int[]                                          mSelectionDrawableArray;
    private HashMap<String, List<HashMap<String, String>>> mTableHashMap;
    private List<RadioButton>                              mRadioButtonList;
    private List<CheckBox>                                 mCheckBoxList;
    private RecyclerView                                   mRecycleView;
    private SearchBySelectionRecycleViewAdapter            mRecycleViewAdapter;
    private int mLastPosition = -1;
    private RadioGroup          mRadioGroup;
    private RadioButton         mRbSite;
    private RadioButton         mRbDep;
    private RadioButton         mRbSubDep;
    private RadioButton         mRbWKCondition;
    private RadioButton         mRbAssetId;
    private RadioButton         mRbCurrentUser;
    private RadioButton         mRbSn;
    private RadioButton         mRbModule;
    private RadioButton         mRbCalResult;
    private RadioButton         mRbCalReq;
    private List<CheckBox>      mCheckBoxsListForCalRes;
    private String              mTableNameForResultMap;
    private ArrayList<String[]> mStrArrayList4Search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_search_by_selection);

        setStatusBarEnald(true);
        initTitle();
        initView();
        initData();
        initListener();
        initRequest();
        registers();
    }


    private void initRequest() {

    }

    private void initListener() {
        mBtnCancel.setOnClickListener(this);
        mBtnSearch.setOnClickListener(this);

    }

    private void initView() {
        mBtnCancel = (Button) findViewById(R.id.btn_cancel);
        mBtnSearch = (Button) findViewById(R.id.btn_search);
        mLLSelectionResult = (LinearLayout) findViewById(R.id.ll_selection_result);

        mRadioGroup = (RadioGroup) findViewById(R.id.rg_search_selections);
        mRbSite = (RadioButton) findViewById(R.id.rb_site);
        mRbDep = (RadioButton) findViewById(R.id.rb_department);
        mRbSubDep = (RadioButton) findViewById(R.id.rb_subdepartment);
        mRbWKCondition = (RadioButton) findViewById(R.id.rb_workingcondition);
        mRbAssetId = (RadioButton) findViewById(R.id.rb_asset_id);
        mRbCurrentUser = (RadioButton) findViewById(R.id.rb_current_user);
        mRbSn = (RadioButton) findViewById(R.id.rb_sn);
        mRbModule = (RadioButton) findViewById(R.id.rb_module);
        mRbCalResult = (RadioButton) findViewById(R.id.rb_cal_result);
        mRbCalReq = (RadioButton) findViewById(R.id.rb_cal_req);

        mRadioButtons.add(mRbSite);
        mRadioButtons.add(mRbDep);
        mRadioButtons.add(mRbSubDep);
        mRadioButtons.add(mRbWKCondition);
        mRadioButtons.add(mRbAssetId);
        mRadioButtons.add(mRbCurrentUser);
        mRadioButtons.add(mRbSn);
        mRadioButtons.add(mRbModule);
        mRadioButtons.add(mRbCalResult);
        mRadioButtons.add(mRbCalReq);
    }

    private void initData() {
        Intent            intent            = getIntent();
        Bundle            bundle            = intent.getExtras();
        CodeCaptureInfo   captureInfo       = bundle.getParcelable(ALL_ASSET);
        boolean           isToSearchInDB    = bundle.getBoolean(HomeFragment.DATA_FROM_DB, false);
        boolean           isToSearchInAsset = bundle.getBoolean(HomeFragment.DATA_FROM_TARGET_ASSET, false);
        ArrayList<String> tableNameList     = bundle.getStringArrayList(QRCodeCaptureActivity.QR_RESULT_TABLENAME);
        if (null != tableNameList && tableNameList.size() == 1) {
            mTableNameForResultMap = tableNameList.get(0);
        }
        mStrArrayList4Search = new ArrayList<>();

        if (null != captureInfo) {
            mTableHashMap = captureInfo.getTableHashMap();

            if (isToSearchInDB) {
                for (String type : QRResultActivity.tables) {
                    mStrArrayList4Search.addAll(MapDataUtils.parseIntoArray4Search(mTableHashMap, type));//0
                }
            } else if (isToSearchInAsset) {
                if (null != mTableNameForResultMap) {
                    mStrArrayList4Search.addAll(MapDataUtils.parseIntoArray4Search(mTableHashMap, mTableNameForResultMap));
                }
            }
        }


        mSelectionDrawableArray = new int[]{
                R.drawable.selector_selection_site,
                R.drawable.selector_selection_department,
                R.drawable.selector_selection_subdepartment,
                R.drawable.selector_selection_working_condition,
                R.drawable.selector_selection_asset_id,
                R.drawable.selector_selection_current_user,
                R.drawable.selector_selection_sn,
                R.drawable.selector_selection_module,
                R.drawable.selector_selection_calibration_result,
                R.drawable.selector_selection_calibration_cycle};
        List<Integer> data = new ArrayList<>();
        for (int i = 0; i < mSelectionDrawableArray.length; i++) {
            data.add(mSelectionDrawableArray[i]);
        }
        mRadioButtonList = new ArrayList<>();

        for (int k = 0; k < mRadioButtons.size(); k++) {
            mRadioButtons.get(k).setBackgroundDrawable(getResources().getDrawable(mSelectionDrawableArray[k]));
        }
        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                if (null != mExpandEditText) {
                    String trim = mExpandEditText.getText().toString().trim();
                    if (!TextUtil.isEmpty(trim)) {
                        mTotoalForSearch.put((Integer) mExpandEditText.getTag(), trim);
                    }
                }
                expandRightViewInRadioGroup(group, checkedId);

            }
        });

    }

    private void registers() {

    }

    private void initTitle() {
        setTitleLayoutColor(getResources().getColor(R.color.color_nokia_blue));
        setLeftDrawble(null, TitleBar2.DrawableIndex.LEFT);
        setLeftBackgroudResource(R.drawable.back_white, PXUtils.dip2px(SearchBySelectionActivity.this, 14), PXUtils.dip2px(SearchBySelectionActivity.this, 26));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisters();
    }

    private void unregisters() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_cancel:
                //重置update操作

                break;
            case R.id.btn_search:
                //输完SN直接搜索
                if (null != mExpandEditText) {
                    String trim = mExpandEditText.getText().toString().trim();
                    if (!TextUtil.isEmpty(trim)) {
                        mTotoalForSearch.put((Integer) mExpandEditText.getTag(), trim);
                    }
                }

                Collection<String> values = mTotoalForSearch.values();
                List<String> list = new ArrayList<>();
                list.addAll(values);

                //                mTotoalForSearch.clear();

                if (null != mStrArrayList4Search) {
                    List<HashMap<String, String>> resultMapList = new ArrayList<>();
                    List<String[]>                result        = searchResult(mStrArrayList4Search, list);
                    //resultMapList和result中的数据元是一样的,只不过一个是map的有键值对,一个是数组的只有value

                    //获得数据正确
                    if (result.size() > 1) {
                        for (String[] strings : result) {
                            HashMap<String, String> result4Search = MapDataUtils.getResult4Search(mTableHashMap, strings[1], new ArrayList<String[]>());
                            resultMapList.add(result4Search);
                        }

                        //TODO---intent跳到AssetActivity

                    } else if (result.size() == 1) {

                        String[]                strings       = result.get(0);
                        HashMap<String, String> result4Search = MapDataUtils.getResult4Search(mTableHashMap, strings[1], new ArrayList<String[]>());
                        //TODO---intent跳到QRResultActivity

                    }
                }

                break;
        }
    }

    private HashMap<String, String> parseResultToMap(List<String[]> result) {
        HashMap<String, String> resultMap = null;
        try {
            String[]                      strings     = result.get(0);
            JsonElement                   jsonElement = new JsonParser().parse(Arrays.toString(strings));
            Gson                          gson        = new Gson();
            String                        jsonStr     = gson.toJson(jsonElement);
            List<HashMap<String, String>> mapList     = MapDataUtils.parseIntoMap2(jsonStr, mTableNameForResultMap);
            resultMap = mapList.get(0);
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }
        return resultMap;
    }

    private List<String[]> searchResult(ArrayList<String[]> strArrayList4Search, List<String> strList) {
        List<String[]> strArrayList = new ArrayList<>();

        for (String[] strings : strArrayList4Search) {
            if(Arrays.asList(strings).containsAll(strList)){
                strArrayList.add(strings);
            }
        }

        return strArrayList;
    }



    private int[] getDrawableIdArray(int id) {
        TypedArray ar     = getResources().obtainTypedArray(id);
        int        len    = ar.length();
        int[]      resIds = new int[len];
        for (int i = 0; i < len; i++)
            resIds[i] = ar.getResourceId(i, 0);
        ar.recycle();
        return resIds;
    }

    private void selectionExpandEdittextType(int id) {
        mLLSelectionResult.removeAllViews();//避免多个视图重叠
        mLLSelectionResult.removeAllViewsInLayout();

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        LayoutInflater inflater = LayoutInflater.from(SearchBySelectionActivity.this);
        View           view2    = inflater.inflate(R.layout.activity_search_edittext_type, null);//activity_search_edittext_type
        view2.setLayoutParams(lp);
        mLLSelectionResult.addView(view2);

        mExpandEditText = (EditText) view2.findViewById(R.id.et_edittext_type);
        mExpandEditText.setTag(id);

    }

    private void selectionExpandGridType(int arrayRes, int nameRes) {
        int[]    resSite  = getDrawableIdArray(arrayRes);
        String[] nameSite = getResources().getStringArray(nameRes);

        mLLSelectionResult.removeAllViews();
        mLLSelectionResult.removeAllViewsInLayout();

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        LayoutInflater inflater = LayoutInflater.from(SearchBySelectionActivity.this);
        View           view2    = inflater.inflate(R.layout.activity_search_gridview_type, null);//activity_search_edittext_type
        view2.setLayoutParams(lp);
        mLLSelectionResult.addView(view2);

        final List<Bean> beanList = new ArrayList<>();
        for (int i = 0; i < nameSite.length; i++) {
            beanList.add(new Bean(false, resSite[i], nameSite[i]));
        }

        mGridInEdittextType = (NoScrollGridView) view2.findViewById(R.id.activity_search_gridView);
        mExpandGridViewAdapter = new SelectionExpandGridViewAdapter(SearchBySelectionActivity.this, beanList);
        mGridInEdittextType.setAdapter(mExpandGridViewAdapter);

        final int arrayKey = arrayRes;
        mGridInEdittextType.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                for (Bean bean : beanList) {
                    bean.setChecked(false);
                    if (mTotoalForSearch.keySet().contains(view.getId())) {
                        mTotoalForSearch.remove(view.getId());
                    }
                }
                beanList.get(position).setChecked(true);
                mTotoalForSearch.put(arrayKey, beanList.get(position).getName());
                mExpandGridViewAdapter.notifyDataSetChanged();
            }
        });
    }

    private void selectionExpandListType(int arrayRes, int nameRes, final List<CheckBox> checkBoxList) {
        int[]          res  = getDrawableIdArray(arrayRes);
        String[]       name = getResources().getStringArray(nameRes);

        mLLSelectionResult.removeAllViews();
        mLLSelectionResult.removeAllViewsInLayout();

        final List<Bean> beanList = new ArrayList<>();
        for (int i = 0; i < checkBoxList.size(); i++) {
            beanList.add(new Bean(checkBoxList.get(i).isChecked(), res[i], name[i]));
        }

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        final LayoutInflater inflater = LayoutInflater.from(SearchBySelectionActivity.this);
        View                 view2    = inflater.inflate(R.layout.activity_search_listview_type, null);//activity_search_edittext_type
        view2.setLayoutParams(lp);
        mLLSelectionResult.addView(view2);

        mListViewInEdittextType = (ListView) view2.findViewById(R.id.lv_selection_expand);
        mExpandListAdapter = new SelectionExpandListAdapter(SearchBySelectionActivity.this, beanList);
        mListViewInEdittextType.setAdapter(mExpandListAdapter);
        ListViewUtils.setListViewHeightBasedOnChildren(mListViewInEdittextType);

        final int arrayKey = arrayRes;
        mListViewInEdittextType.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                for (Bean bean : beanList) {
                    bean.setChecked(false);
                    if (mTotoalForSearch.keySet().contains(view.getId())) {
                        mTotoalForSearch.remove(view.getId());
                    }
                }
                beanList.get(position).setChecked(true);
                mTotoalForSearch.put(arrayKey, beanList.get(position).getName());
                mExpandListAdapter.notifyDataSetChanged();
            }
        });

    }


    @Override
    public void onSearchSelectionsClick(View view, boolean isChecked, int position) {

        if (view instanceof CompoundButton) {

            expandRightViewInRecycleView(view, position);
        }
    }

    private void expandRightViewInRecycleView(View view, int position) {
        HashSet<CheckBox>    checkBoxSet = mRecycleViewAdapter.getCheckBoxSet();
        final List<CheckBox> checkBoxs   = new ArrayList<>();
        checkBoxs.addAll(checkBoxSet);
        if (view instanceof CheckBox) {
            if (!((CheckBox) view).isChecked()) {
                return;
            }
            int drawable = (int) view.getTag();

            if (drawable == R.drawable.selector_selection_site) {
                //site

                selectionExpandGridType(R.array.resSite, R.array.nameSite);
            } else if (drawable == R.drawable.selector_selection_department) {
                //department
                selectionExpandGridType(R.array.resDep, R.array.nameDep);
            } else if (drawable == R.drawable.selector_selection_working_condition) {
                //working condition
                selectionExpandGridType(R.array.resWorkCondition, R.array.nameWorkCondition);
            } else if (drawable == R.drawable.selector_selection_calibration_result) {
                //calibration_result --- ListView
                int[]          resCalResult  = getDrawableIdArray(R.array.resCalResult);
                List<CheckBox> checkBoxsList = new ArrayList<>();
                for (int j = 0; j < resCalResult.length; j++) {
                    CheckBox checkBox = new CheckBox(SearchBySelectionActivity.this);
                    checkBoxsList.add(checkBox);
                }
                selectionExpandListType(R.array.resCalResult, R.array.nameCalResult, checkBoxsList);
            } else if (drawable == R.drawable.selector_selection_current_user) {
                //TODO---spinner中放的是当前所选条件查询后所得的名字???

            } else if (drawable == R.drawable.selector_selection_subdepartment) {
                selectionExpandGridType(R.array.resSubDepAll, R.array.nameSubDepAll);

            } else {
                selectionExpandEdittextType(drawable);
            }

            if (null != mExpandEditText) {
                mSearchEdittext = mExpandEditText.getText().toString();
            }
        }
    }


    private void expandRightViewInRadioGroup(RadioGroup group, int checkedId) {
        if (checkedId == R.id.rb_site) {
            //site
            selectionExpandGridType(R.array.resSite, R.array.nameSite);
        } else if (checkedId == R.id.rb_department) {
            //department
            selectionExpandGridType(R.array.resDep, R.array.nameDep);
        } else if (checkedId == R.id.rb_workingcondition) {
            //working condition
            selectionExpandGridType(R.array.resWorkCondition, R.array.nameWorkCondition);
        } else if (checkedId == R.id.rb_cal_result) {
            //calibration_result --- ListView
            int[]    resCalResult  = getDrawableIdArray(R.array.resCalResult);
            mCheckBoxsListForCalRes = new ArrayList<>();
            for (int j = 0; j < resCalResult.length; j++) {
                CheckBox checkBox = new CheckBox(SearchBySelectionActivity.this);
                mCheckBoxsListForCalRes.add(checkBox);
            }
            selectionExpandListType(R.array.resCalResult, R.array.nameCalResult, mCheckBoxsListForCalRes);
        } else if (checkedId == R.id.rb_current_user) {
            //TODO---spinner中放的是当前所选条件查询后所得的名字???

        } else if (checkedId == R.id.rb_subdepartment) {
            selectionExpandGridType(R.array.resSubDepAll, R.array.nameSubDepAll);

        } else {
            selectionExpandEdittextType(checkedId);
        }

        if (null != mExpandEditText) {
            mSearchEdittext = mExpandEditText.getText().toString();
        }
    }
}
