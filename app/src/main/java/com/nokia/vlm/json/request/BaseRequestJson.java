package com.nokia.vlm.json.request;

import android.util.Pair;

import com.qx.framelib.utlis.SecureUtils;
import com.qx.framelib.utlis.ZLog;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 所有的请求全部继承该类，自动组装json串，加入必要
 * 如： 时间戳  ，签名 ，token 等
 * <p/>
 * 注意： 在子类中填写的字段全部参与签名，没有填写的字段不参与签名，
 * 填写的字段不要为null !!!!
 * <p/>
 */
public abstract class BaseRequestJson {

    public static final String APPKEY = "Zhiqu@#$425";

    //定义子类可以存在在基本类型，这些基本类型将参与签名
    public static final Map<Class<?>, String> TYPES = new HashMap<Class<?>, String>();

    //缓存class对象的字段
    private static final Map<Class<? extends BaseRequestJson>, List<Field>> field_cache = new ConcurrentHashMap<Class<? extends BaseRequestJson>, List<Field>>();

    static {
        TYPES.put(byte.class, "INTEGER");
        TYPES.put(boolean.class, "INTEGER");
        TYPES.put(short.class, "INTEGER");
        TYPES.put(int.class, "INTEGER");
        TYPES.put(long.class, "INTEGER");
        TYPES.put(String.class, "TEXT");
        TYPES.put(byte[].class, "BLOB");
        TYPES.put(float.class, "REAL");
        TYPES.put(double.class, "REAL");
    }

    protected Class<? extends BaseRequestJson> getSubClass() {
        return this.getClass();
    }


    /**
     * 签名
     * <p/>
     * //     * @param json   已有字段完成了json的拼接,拿到后要在原有json后加上sign,timestamp
     *
     * @param listKey     子类中已有的属性名
     * @param keyValueMap 子类属性名key-value一一对应
     * @return
     */
    public String buildRequestStr(List<String> listKey, Map<String, Object> keyValueMap) {

        JSONObject json = new JSONObject();

        //注意只有请求里面有参数才将这些字段做拼接
        if (listKey != null && listKey.size() > 0) {
            for (String key : listKey) {
                Object obj = keyValueMap.get(key);
                if (obj instanceof Integer || obj instanceof String) {
                    try {
                        json.put(key, obj);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        StringBuilder sb = new StringBuilder();
        if (listKey != null && listKey.size() > 0) {

            //先转换为strs
            String[] arrays = new String[listKey.size()];
            for (int j = 0; j < listKey.size(); j++) {
                arrays[j] = listKey.get(j);
            }

            //再排序
            Arrays.sort(arrays);

            int len = arrays.length;
            for (int i = 0; i < len; i++) {
                String key = arrays[i];
                sb.append(key);
                sb.append("=");
                sb.append(keyValueMap.get(key));
                sb.append("&");
            }
        }

        sb.append("appkey");
        sb.append("=");
        sb.append(APPKEY);

        long timestamp = System.currentTimeMillis();

        sb.append("&");
        sb.append("timestamp");
        sb.append("=");
        sb.append(timestamp);

        ZLog.d("zooer", "new str: " + sb.toString());

        String content = "";
//        try {
//            content = URLEncoder.encode(sb.toString(), "UTF-8");
////            content = Uri.encode(sb.toString(), "UTF-8");
//            ZLog.d("content", "content:" + content);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        content = SecureUtils.md5Encode(sb.toString()).toLowerCase();

        try {
            json.put("sign", content);
            json.put("timestamp", timestamp);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ZLog.d("zooer", "request content: " + json.toString());
        return json.toString();
    }

    public String encodeRequest() {
        Pair<List<String>, Map<String, Object>> pairs = generateRequestPair(this);
        if (pairs != null) {
            List<String> listKey = pairs.first;
            Map<String, Object> keyValueMap = pairs.second;
//            if(listKey != null && listKey.size() > 0){
            return buildRequestStr(listKey, keyValueMap);
//            }
        }
        return null;
    }



    private Pair<List<String>, Map<String, Object>> generateRequestPair(BaseRequestJson baseRequestJson) {
        List<String> keys = new ArrayList<String>();
        Map<String, Object> keyValueMap = new HashMap<String, Object>();
        Class<? extends BaseRequestJson> clz = baseRequestJson.getSubClass();
        List<Field> f = getValidField(clz);
        Field field = null;
        for (int i = 0, fLen = f.size(); i < fLen; i++) {
            field = f.get(i);
            String name = field.getName();
            Class c = field.getType();
            String type = TYPES.get(c);
            if (type != null) {
                if (type.equals("INTEGER") || type.equals("TEXT")) {
                    try {
                        Object obj = field.get(baseRequestJson);
                        if (obj != null) {
                            keys.add(name);
                            keyValueMap.put(name, obj);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return Pair.create(keys, keyValueMap);
    }

    private static List<Field> getValidField(Class<? extends BaseRequestJson> clazz) {
        List<Field> fields = field_cache.get(clazz);
        if (fields == null) {
            Field[] f = clazz.getFields();
            fields = new ArrayList<Field>(f.length);
            for (Field field : f) {
                //跳过定义的一些静态变量
                if (!Modifier.isStatic(field.getModifiers())) {
                    fields.add(field);
                }
            }
            field_cache.put(clazz, fields);
        }
        return fields;
    }

}
