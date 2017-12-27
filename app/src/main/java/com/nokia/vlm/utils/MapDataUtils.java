package com.nokia.vlm.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.nokia.vlm.entity.CardAsset;
import com.nokia.vlm.entity.ComputerAsset;
import com.nokia.vlm.entity.PluggableAsset;
import com.nokia.vlm.entity.ShelfAsset;
import com.nokia.vlm.entity.TestEquipAsset;
import com.qx.framelib.utlis.ZLog;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.nokia.vlm.ui.activity.QRResultActivity.tables;
import static com.nokia.vlm.ui.fragment.HomeFragment.CARD_ASSET;
import static com.nokia.vlm.ui.fragment.HomeFragment.COMPUTER_ASSET;
import static com.nokia.vlm.ui.fragment.HomeFragment.PLUGGABLE_ASSET;
import static com.nokia.vlm.ui.fragment.HomeFragment.SHELF_ASSET;
import static com.nokia.vlm.ui.fragment.HomeFragment.TESTEQUIP_ASSET;

/**
 * @创建者 DK-house
 * @创建时间 2017/12/11 11:04
 * @描述 ${TODO}
 * @更新者 ${AUTHOR}
 * @更新时间 2017/12/11
 * @更新描述 ${TODO}
 */

public class MapDataUtils {

    public static String mResultTableName = "";

    public static HashMap<String, List<HashMap<String, String>>> parseJsonToMap4Tables(String jsonStr) {
        HashMap<String, List<HashMap<String, String>>> tableHashMap = new HashMap<>();
        Map                                            maps         = (Map) JSON.parse(jsonStr);
        JSONArray                                      value;
        ZLog.d("这个是用JSON类来解析JSON字符串!!!");

        for (Object map : maps.entrySet()) {

            if (TESTEQUIP_ASSET.equals(((Map.Entry) map).getKey())) {
                value = (JSONArray) ((Map.Entry) map).getValue();
                //                tableHashMap.put(TESTEQUIP_ASSET, parseIntoMap(value, TestEquipAsset.keyArray));
                tableHashMap.put(TESTEQUIP_ASSET, parseIntoMap2(jsonStr, TESTEQUIP_ASSET));
                ZLog.d("JSON字符串!!!", value.toString());
            }
            if (CARD_ASSET.equals(((Map.Entry) map).getKey())) {
                value = (JSONArray) ((Map.Entry) map).getValue();
                tableHashMap.put(CARD_ASSET, parseIntoMap2(jsonStr, CARD_ASSET));
                ZLog.d("JSON字符串!!!", value.toString());
            }
            if (PLUGGABLE_ASSET.equals(((Map.Entry) map).getKey())) {
                value = (JSONArray) ((Map.Entry) map).getValue();
                tableHashMap.put(PLUGGABLE_ASSET, parseIntoMap2(jsonStr, PLUGGABLE_ASSET));
                ZLog.d("JSON字符串!!!", value.toString());
            }
            if (SHELF_ASSET.equals(((Map.Entry) map).getKey())) {
                value = (JSONArray) ((Map.Entry) map).getValue();
                tableHashMap.put(SHELF_ASSET, parseIntoMap2(jsonStr, SHELF_ASSET));
                ZLog.d("JSON字符串!!!", value.toString());
            }
            if (COMPUTER_ASSET.equals(((Map.Entry) map).getKey())) {
                value = (JSONArray) ((Map.Entry) map).getValue();
                tableHashMap.put(COMPUTER_ASSET, parseIntoMap2(jsonStr, COMPUTER_ASSET));
                ZLog.d("JSON字符串!!!", value.toString());
            }

        }
        return tableHashMap;

    }

    public static List<HashMap<String, String>> parseIntoMap2(String jsonStr, String type) {
        List<HashMap<String, String>> mapList    = new ArrayList<>();
        JsonObject                    jsonObject = new JsonParser().parse(jsonStr).getAsJsonObject();
        JsonArray                     jsonArray  = jsonObject.get(type).getAsJsonArray();
        String[]                      keyArray   = {};
        switch (type) {
            case "TestEquip_Asset":
                keyArray = TestEquipAsset.keyArray;
                break;
            case "Card_Asset":
                keyArray = CardAsset.keyArray;
                break;
            case "Pluggable_Asset":
                keyArray = PluggableAsset.keyArray;
                break;
            case "Shelf_Asset":
                keyArray = ShelfAsset.keyArray;
                break;
            case "Computer_Asset":
                break;
        }

        for (int i = 0; i < jsonArray.size(); i++) {
            JsonArray               asJsonArray = jsonArray.get(i).getAsJsonArray();
            HashMap<String, String> map         = new HashMap<>();
            //每个map代表一条记录
            for (int k = 0; k < asJsonArray.size(); k++) {
                String asString = "";
                if (!asJsonArray.get(k).isJsonNull()) {
                    asString = asJsonArray.get(k).getAsString();
                } else {
                    asString = "";
                }
                for (int j = 0; j < keyArray.length; j++) {
                    if (k == j) {
                        map.put(keyArray[j], asString);
                        break;
                    }
                }

            }
            mapList.add(map);
        }
        return mapList;
    }

    public static List<HashMap<String, String>> parseIntoMap(JSONArray value, String[] keyArray) {
        List<HashMap<String, String>> mapList = new ArrayList<>();
        //        for (Object obj : value) {

        try {
            for (int m = 0; m < value.size(); m++) {
                Object                  obj          = value.get(m);
                String                  toString     = obj.toString();
                String                  substring    = toString.substring(1, toString.length() - 1);
                String[]                splitedItems = substring.split(",");
                HashMap<String, String> map          = new HashMap<>();
                for (int i = 0; i < splitedItems.length; i++) {
                    for (int j = 0; j < keyArray.length; j++) {
                        if (i == j) {
                            String splitedItemStr = splitedItems[i];
                            if (i == 0) {
                                map.put(keyArray[j], String.valueOf(splitedItemStr));
                            } else {
                                map.put(keyArray[j], String.valueOf(splitedItemStr.substring(splitedItemStr.indexOf("") + 1, splitedItems[i].lastIndexOf("") - 1)));
                            }
                            break;
                        }
                    }
                }
                mapList.add(map);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return mapList;
    }


    /**
     * @return 表中符合搜索条件key的唯一值
     */
    public static HashMap<String, String> getResult4Search(HashMap<String, List<HashMap<String, String>>> tableHashMap, String key,
                                                           List<String[]> mStrArrayList4SearchAndUpdate) {
        //update和check模糊查询的时候要传的string数组的list
        //        mStrArrayList4SearchAndUpdate = new ArrayList<>();

        List<String[]> mStringsList = new ArrayList<>();

        //查到的结果是唯一结果
        HashMap<String, String> map = null;
        for (String type : tables) {
            mStrArrayList4SearchAndUpdate.addAll(parseIntoArray4Search(tableHashMap, type));//0
            switch (type) {
                case "TestEquip_Asset":
                    mStringsList.add(TestEquipAsset.keyArray);
                    break;
                case "Card_Asset":
                    mStringsList.add(CardAsset.keyArray);
                    break;
                case "Pluggable_Asset":
                    mStringsList.add(PluggableAsset.keyArray);
                    break;
                case "Shelf_Asset":
                    mStringsList.add(ShelfAsset.keyArray);
                    break;
                case "Computer_Asset":
                    mStringsList.add(ComputerAsset.keyArray);
                    break;

            }
            map = searchByKeys(key, type, tableHashMap, mStrArrayList4SearchAndUpdate, mStringsList);
            if (null != map && map.size() > 0) {
                return map;
            }
        }
        return map;
    }


    /**
     * @param type 表名
     */
    public static List<String[]> parseIntoArray4Search(HashMap<String, List<HashMap<String, String>>> tableMap, String type) {
        String[]       str4update        = new String[]{};
        List<String[]> strArrayForReturn = new ArrayList<>();
        for (HashMap<String, String> map : tableMap.get(type)) {

            List<String> list = new ArrayList<>();
            for (Object mapObj : map.entrySet()) {
                list.add((String) ((Map.Entry) mapObj).getValue());
            }
            str4update = list.toArray(new String[map.entrySet().size()]);
            strArrayForReturn.add(str4update);
        }
        return strArrayForReturn;
    }

    public static HashMap<String, String> searchByKeys(String searchKey, String tableName, HashMap<String, List<HashMap<String, String>>> mTableHashMap
            , List<String[]> mStrArrayList4SearchAndUpdate, List<String[]> mStringsList) {
        //精准查询
        HashMap<String, String>       resultMap = null;
        List<HashMap<String, String>> mapList;
        for (int i = 0; i < mStrArrayList4SearchAndUpdate.size(); i++) {
            String[] array = mStrArrayList4SearchAndUpdate.get(i);
            if (Arrays.toString(array).contains(searchKey)) {
                mapList = mTableHashMap.get(tableName);
                for (HashMap<String, String> map : mapList) {
                    for (String key : mStringsList.get(0)) {
                        //                                    if (searchKey.equals(map.get(key)) || map.get(key).contains(searchKey)) {
                        if (searchKey.equals(map.get(key))) {
                            String result4Search = map.get(key);//查询的结果
                            //TODO---这个map就是要找的item
                            mResultTableName = tableName;
                            resultMap = map;
                            break;
                        }
                    }
                }

            }
        }
        return resultMap;
    }

    /**
     * @param searchKey 数据库中的一个字段名
     * @return 每个表名对应一个存放符合条件的map, 该map中键是搜索的字段名, 值是list形式的结果集
     */
    // 查询所有表下某一字段的所有数值
    public static HashMap<String, HashMap<String, List<String>>> selectDistinctFromDBByKey(String searchKey, HashMap<String, List<HashMap<String, String>>> mTableHashMap) {
        HashMap<String, HashMap<String, List<String>>> resultMapAll = new HashMap<>();
        Collection<List<HashMap<String, String>>>      values       = mTableHashMap.values();
        Set<String>                                    keySet       = mTableHashMap.keySet();

        for (String type : tables) {
            List<HashMap<String, String>> valueList = new ArrayList<>();
            for (String str : keySet) {
                if (type.equals(str)) {
                    valueList.addAll(mTableHashMap.get(str));
                    List<String>                  list    = new ArrayList<>();
                    HashMap<String, List<String>> hashMap = new HashMap<>();
                    for (HashMap<String, String> map : valueList) {
                        for (String key : map.keySet()) {
                            if (searchKey.equals(key)) {
                                list.add(map.get(searchKey));
                            }
                        }
                    }
                    hashMap.put(searchKey, list);

                    resultMapAll.put(type, hashMap);
                    break;
                }
            }
        }

        return resultMapAll;
    }

    /**
     * @param tableName 搜索的字段所在的表名
     * @param searchKey 需要搜索的字段名
     * @return 在指定表中该字段对应的所有属性值的list集合.
     * */

    //select DISTINCT Subdepartment from TestEquip_Asset 相当于这句查询语句的功能
    public static List<String> selectDistinctFromAssetByKey(String tableName, String searchKey,HashMap<String, HashMap<String, List<String>>> sourceMap) {
        List<String> resultList = new ArrayList<>();
        for (String table : sourceMap.keySet()) {
            if (tableName.equals(table)) {
                HashMap<String, List<String>> map = sourceMap.get(tableName);
                for (String key : map.keySet()) {
                    if(searchKey.equals(key)){
                        resultList = map.get(searchKey);
                    }
                }
            }
        }

        return resultList;
    }
}
