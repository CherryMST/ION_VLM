package com.nokia.vlm.dao;

import com.nokia.vlm.callback.AddTestEquipAssetItemCallback;
import com.nokia.vlm.json.request.TestEquipAssetItemRequestJson;
import com.qx.framelib.callback.CallbackHelper;
import com.qx.framelib.dao.BaseDao;
import com.qx.framelib.entity.RequestEntity;

import okhttp3.Call;

/**
 * @创建者 xuejinghan
 * @创建时间 2017/11/15 17:41
 * @描述 ${TODO}
 * @更新者 ${AUTHOR}
 * @更新时间 2017/11/15
 * @更新描述 ${TODO}
 */

public class AddTestEquipAssetItemEngine extends BaseDao<AddTestEquipAssetItemCallback>{

        private String url = "update";

        public void addToTestEquipAsset() {
            RequestEntity entity = new RequestEntity();
            TestEquipAssetItemRequestJson requestJson = new TestEquipAssetItemRequestJson();
            //        requestJson.token = UserInfoManager.getInstance().getToken();
            requestJson.tableName = "TestEquip_Asset";
            requestJson.dataArray=new String[]{
                    "-1", //int 不行,要么单独放外面
                    "SH-HW-00000001",
                    "JQ-B2-2204-DN-S01#99",
                    "1000399999",
                    "Shanghai",
                    "Common",
                    "FPGA",
                    "TestInstrument",
                    "AGILENT",
                    "MY45103999",
                    "",
                    "In Service",
                    "ESA-E SERIES SPECTRUM ANALYZER TEST",
                    "",
                    "AAAAA",
                    "2017-10-26",
                    "8",
                    "CALIBRATION NEEDED"};

            entity.requestBody = requestJson.encodeRequest();
            entity.url = buildUrl(url);

            postRequest(entity);
        }


        @Override
        protected void onRequestFailed(Call call, Exception e) {
            notifyDataChangedInMainThread(new CallbackHelper.Caller<AddTestEquipAssetItemCallback>() {
                @Override
                public void call(AddTestEquipAssetItemCallback ac) {
//                    ac.addTestEquipAssetItem(-1001, "请求失败，请检查网络", null);
                }
            });
        }

        @Override
        protected void onRequestSuccess(String response) {
//            final TestEquipAssetItemInfo responseJson = new TestEquipAssetItemInfo();
//            responseJson.parse(response);

//            notifyDataChangedInMainThread(new CallbackHelper.Caller<AddTestEquipAssetItemCallback>() {
//                @Override
//                public void call(AddTestEquipAssetItemCallback ac) {
//                    if (responseJson != null) {
//                        ac.addTestEquipAssetItem(1,responseJson.msg, responseJson);
//                    }
//                }
//            });
        }
}
