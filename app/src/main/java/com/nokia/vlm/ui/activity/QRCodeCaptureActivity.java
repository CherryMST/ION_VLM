package com.nokia.vlm.ui.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Toast;

import com.google.zxing.Result;
import com.google.zxing.ResultPoint;
import com.nokia.qrcode.camera.CameraManager;
import com.nokia.qrcode.camera.PreviewFrameShotListener;
import com.nokia.qrcode.camera.Size;
import com.nokia.qrcode.decode.DecodeListener;
import com.nokia.qrcode.decode.DecodeThread;
import com.nokia.qrcode.decode.LuminanceSource;
import com.nokia.qrcode.decode.PlanarYUVLuminanceSource;
import com.nokia.qrcode.decode.RGBLuminanceSource;
import com.nokia.qrcode.view.CaptureView;
import com.nokia.vlm.R;
import com.nokia.vlm.callback.CodeCaptureCallBack;
import com.nokia.vlm.dao.CodeCaptureEngine;
import com.nokia.vlm.entity.CardAsset;
import com.nokia.vlm.entity.CodeCaptureInfo;
import com.nokia.vlm.entity.ComputerAsset;
import com.nokia.vlm.entity.MapListInfo;
import com.nokia.vlm.entity.PluggableAsset;
import com.nokia.vlm.entity.SerializableMap;
import com.nokia.vlm.entity.ShelfAsset;
import com.nokia.vlm.entity.TestEquipAsset;
import com.nokia.vlm.utils.ImageUtil;
import com.nokia.vlm.utils.ListDataSaveBySharePreference;
import com.qx.framelib.activity.BaseTitle2Activity;
import com.qx.framelib.utlis.HandlerUtils;
import com.qx.framelib.utlis.SysPhotoCropper;
import com.qx.framelib.utlis.TextUtil;
import com.qx.framelib.utlis.ToastUtils;
import com.qx.framelib.utlis.ZLog;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.nokia.vlm.ui.activity.QRResultActivity.tables;
import static com.nokia.vlm.ui.fragment.HomeFragment.ALL_ASSET;


/**
 * 二维码扫描
 */
public class QRCodeCaptureActivity extends BaseTitle2Activity implements SurfaceHolder.Callback, PreviewFrameShotListener, DecodeListener,
        OnCheckedChangeListener, OnClickListener, CodeCaptureCallBack {//,SysPhotoCropper.PhotoCropCallBack

    public static final String TAG_TESTEQUIPASSET = "TestEquipAsset";
    public static final String TAG_SHELFASSET     = "ShelfAsset";
    public static final String TAG_PLUGGABLEASSET = "PluggableAsset";
    public static final String TAG_COMPUTERASSET  = "ComputerAsset";
    public static final String TAG_CARDASSET      = "CardAsset";
    public static final String TAG_MAP4SEARCH     = "SearchMap";

    public static final int    CHOOSE_PHOTO        = 0;
    public static final long   VIBRATE_DURATION    = 200L;
    public static final int    REQUEST_CODE_ALBUM  = 0;
    public static final  String QR_RESULT_NOT_NULL  = "qrResultNotNull";
    public static final  String QR_RESULT_MAP       = "qrResultMap";
    public static final  String QR_RESULT_MAP_LIST  = "qrResultMaps";
    public static final  String QR_RESULT_KEY       = "qr_key";
    public static final  String QR_RESULT_TABLENAME = "qrResultTableName";
    public static final  String EXTRA_RESULT        = "result";
    public static final  String EXTRA_BITMAP        = "bitmap";

    private ListDataSaveBySharePreference mySharePreference;

    private CodeCaptureEngine codeCaptureEngine = new CodeCaptureEngine();
    private CodeCaptureInfo captureInfo;

    private ArrayList<String> mResultNameList = new ArrayList<>();
    private String                                         mResultTableName;
    private HashMap<String, List<HashMap<String, String>>> mTableHashMap;
    private List<String[]> mStringsList = new ArrayList<>();
    private List<String[]> mStrArrayList4SearchAndUpdate;

    private SurfaceView   previewSv;
    private CaptureView   captureView;
    //    private RelativeLayout rl_title;
    private CameraManager mCameraManager;
    private DecodeThread  mDecodeThread;
    private Rect    previewFrameRect = null;
    private boolean isDecoding       = false;
    private SysPhotoCropper mPhotoCropper;
    private String          mQrcode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_code_capture);
        initTitle();
        initView();
        initData();
        registers();
    }

    private void initTitle() {
        setTitleLayoutColor(getResources().getColor(R.color.color_text_content_super_dark));
        setRightText("相册");
        setRightTextColor(getResources().getColor(R.color.color_white));
        setRightOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("android.intent.action.GET_CONTENT");
                intent.setType("image/*");
                startActivityForResult(intent, CHOOSE_PHOTO);
            }
        });
    }

    private void initData() {

    }


    private void initView() {
        previewSv = (SurfaceView) findViewById(R.id.sv_preview);
        captureView = (CaptureView) findViewById(R.id.cv_capture);
        //        rl_title = (RelativeLayout) findViewById(R.id.rl_title);
        //        findViewById(R.id.img_back).setOnClickListener(this);
        //        findViewById(R.id.tv_album).setOnClickListener(this);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            //            albumBtn.setVisibility(View.GONE);
            //            setRightName("");
        }
        previewSv.getHolder().addCallback(this);
        mCameraManager = new CameraManager(this);
        mCameraManager.setPreviewFrameShotListener(this);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        try {
            mCameraManager.initCamera(holder);
        } catch (Exception e) {
            Toast.makeText(this, "相机权限被拒绝，请去设置里面打开", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        if (!mCameraManager.isCameraAvailable()) {
            Toast.makeText(this, R.string.capture_camera_failed, Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        //        if (mCameraManager.isFlashlightAvailable()) {
        //            flashCb.setEnabled(true);
        //        }
        mCameraManager.startPreview();
        if (!isDecoding) {
            mCameraManager.requestPreviewFrameShot();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        mCameraManager.stopPreview();
        if (mDecodeThread != null) {
            mDecodeThread.cancel();
        }
        mCameraManager.release();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisters();
    }

    @Override
    public void onPreviewFrame(byte[] data, Size dataSize) {
        if (mDecodeThread != null) {
            mDecodeThread.cancel();
        }
        if (previewFrameRect == null) {
            previewFrameRect = mCameraManager.getPreviewFrameRect(captureView.getFrameRect());
        }
        PlanarYUVLuminanceSource luminanceSource = new PlanarYUVLuminanceSource(data, dataSize, previewFrameRect);
        mDecodeThread = new DecodeThread(luminanceSource, this);
        isDecoding = true;
        mDecodeThread.execute();
    }


    @Override
    public void onDecodeSuccess(Result result, LuminanceSource source, Bitmap bitmap) {
        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        vibrator.vibrate(VIBRATE_DURATION);
        isDecoding = false;
        //        if (bitmap.getWidth() > 100 || bitmap.getHeight() > 100) {
        //            Matrix matrix = new Matrix();
        //            matrix.postScale(100f / bitmap.getWidth(), 100f / bitmap.getHeight());
        //            Bitmap resizeBmp = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        //            bitmap.recycle();
        //            bitmap = resizeBmp;
        //        }
        //        Intent resultData = new Intent();
        //        resultData.putExtra(QRCodeResultActivity.KEY_QR_CODE_RESULT, result.getText());
        //        resultData.setClass(this, QRCodeResultActivity.class);
        //        startActivity(resultData);
        //        finish();

        //        String res = result.getText();
        //QR_CODE
        mQrcode = result.getText();//TODO---这里存的是什么？？？
        ZLog.d("onDecodeSuccess", "text:" + mQrcode);
        if (mQrcode == null || mQrcode.length() == 0) {
            return;
        }


        //        byte[] content = Base64.decode(res, Base64.NO_WRAP);
        //        byte[] content = Base64.decode(res, Base64.DEFAULT);
        //        String text = new String(content);

        //        if (text.contains("shop")) {
        //            String[] s = text.split("=");
        //
        //            if (s.length<=1 || TextUtil.isEmpty(s[1])) {
        //                ToastUtils.getInstance().show("不识别该二维码", Toast.LENGTH_SHORT);
        //                isDecoding = true;
        //            } else {
        //                try {
        codeCaptureEngine.getCodeCapture();//请求syncall接口
        //                } catch (Exception e) {
        //                    ToastUtils.getInstance().show("不识别该二维码", Toast.LENGTH_SHORT);
        //                }
        //
        //            }
        //        } else {
        //            ToastUtils.getInstance().show("不识别该二维码", Toast.LENGTH_SHORT);
        //            isDecoding = true;
        //        }

    }

    @Override
    public void onDecodeFailed(LuminanceSource source) {
        if (source instanceof RGBLuminanceSource) {
            Toast.makeText(this, R.string.capture_decode_failed, Toast.LENGTH_SHORT).show();
        }
        fial();
    }

    @Override
    public void foundPossibleResultPoint(ResultPoint point) {
        captureView.addPossibleResultPoint(point);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            mCameraManager.enableFlashlight();
        } else {
            mCameraManager.disableFlashlight();
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (isShowLoading()) {
                isDecoding = true;
            }
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    //    @Override
    //    public void onClick(View v) {
    ////        super.onClick(v);
    ////        switch (v.getId()) {
    ////            case R.id.title_right_text:
    ////                Intent intent = null;
    ////                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
    ////                    intent = new Intent(Intent.ACTION_GET_CONTENT);
    ////                } else {
    ////                    intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
    ////                }
    ////                intent.addCategory(Intent.CATEGORY_OPENABLE);
    ////                intent.setType("image/*");
    ////                intent.putExtra("return-data", true);
    ////                startActivityForResult(intent, REQUEST_CODE_ALBUM);
    ////                break;
    ////            default:
    ////                break;
    ////        }
    //    }

    //    @Override
    //    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    //        if (requestCode == REQUEST_CODE_ALBUM && resultCode == RESULT_OK && data != null) {
    //            Bitmap cameraBitmap = null;
    //            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
    //                String path = DocumentUtil.getPath(DeliveryCaptureActivity.this, data.getData());
    //                cameraBitmap = DocumentUtil.getBitmap(path);
    //            } else {
    //                // Not supported in SDK lower that KitKat
    //            }
    //            if (cameraBitmap != null) {
    //                if (mDecodeThread != null) {
    //                    mDecodeThread.cancel();
    //                }
    //                int width = cameraBitmap.getWidth();
    //                int height = cameraBitmap.getHeight();
    //                int[] pixels = new int[width * height];
    //                cameraBitmap.getPixels(pixels, 0, width, 0, 0, width, height);
    //                RGBLuminanceSource luminanceSource = new RGBLuminanceSource(pixels, new Size(width, height));
    //                mDecodeThread = new DecodeThread(luminanceSource, DeliveryCaptureActivity.this);
    //                isDecoding = true;
    //                mDecodeThread.execute();
    //            }
    //        }
    //    }


    /**
     * 延迟1s
     */
    private void fial() {
//        HandlerUtils.getMainZooerHandler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                isDecoding = false;
//                mCameraManager.requestPreviewFrameShot();
//            }
//        }, 1000);

        isDecoding = false;
        mCameraManager.requestPreviewFrameShot();

    }

    @Override
    public void onClick(View v) {
       /* switch (v.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.tv_album:
//                mPhotoCropper = new SysPhotoCropper(QRCodeCaptureActivity.this, this);
//                mPhotoCropper.cropForGallery();
                Intent intent = new Intent("android.intent.action.GET_CONTENT");
                intent.setType("image*//*");
                startActivityForResult(intent, CHOOSE_PHOTO);
                break;
        }*/
    }


    @Override
    public void codeCaptureMsg(int code, String msg, final CodeCaptureInfo responseJson) {
        /**
         * 存储这些list，改为用静态list保存，便于操作。
         * 日后，为了应对大数据了，list中的数据不适合存在本地，应当是服务端执行相应的数据库命令返回客户端所需数据。
         * 从而，只对返回数据进行操作。
         * */

        if (code == 1 && responseJson != null) {
            if (null != responseJson.getTableHashMap() && responseJson.getTableHashMap().size() > 0) {

                //qrKey中存的是key的话，使用以下方法，查询key---某条属性---所对应的info
                mTableHashMap = responseJson.getTableHashMap();
                if (null != mTableHashMap && mTableHashMap.size() > 0) {
                    final HashMap<String, String>                        resultMapForKey     = getResult4Search(mTableHashMap, mQrcode); //搜索表中唯一键,结果唯一
                    final HashMap<String, List<HashMap<String, String>>> resultMapListForKey = getResult4Search2(mTableHashMap,mQrcode); //搜索表中任意属性,结果为多个
                    final MapListInfo                                    mapListInfo         = new MapListInfo(resultMapListForKey);
                    //                    HashMap<String, String> resultMapForKey = searchByKeys(mQrcode);
                    if (null == resultMapForKey && null == resultMapListForKey) {
                        ToastUtils.getInstance().show("未能搜索到相应设备数据！");
                        return;
                    }
                    final Bundle bundle = new Bundle();
                    if (!TextUtil.isEmpty(mQrcode)) {
                        bundle.putString(QR_RESULT_KEY, mQrcode);
                    }


                    if (mResultNameList.size() > 0) {
                        bundle.putStringArrayList(QR_RESULT_TABLENAME, mResultNameList);
                    }else if (!TextUtil.isEmpty(mResultTableName)) {
                        ArrayList<String> arrayList = new ArrayList<>();
                        arrayList.add(mResultTableName);
                        bundle.putStringArrayList(QR_RESULT_TABLENAME, arrayList);
                    }

                    if (resultMapListForKey.size() > 1) {
                        HandlerUtils.getMainZooerHandler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                final Intent intent = new Intent(QRCodeCaptureActivity.this, AssetActivity.class);
                                bundle.putSerializable(QR_RESULT_MAP_LIST, mapListInfo);
                                bundle.putParcelable(ALL_ASSET, responseJson);
                                intent.putExtras(bundle);
                                startActivity(intent);
                                finish();
                            }

                        }, 300);

                    } else if (null != resultMapForKey) {
                        HandlerUtils.getMainZooerHandler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                final Intent          intent          = new Intent(QRCodeCaptureActivity.this, QRResultActivity.class);
                                final SerializableMap serializableMap = new SerializableMap();
                                serializableMap.setMap(resultMapForKey);
//                                bundle.putBoolean(QR_RESULT_NOT_NULL, true);
                                bundle.putSerializable(QR_RESULT_MAP, serializableMap);
                                intent.putExtras(bundle);
                                startActivity(intent);
                                finish();
                            }

                        }, 300);
                    }
                } else {
                    ToastUtils.getInstance().show("未能搜索到相应设备数据！");
                }


            } else {
                ToastUtils.getInstance().show("未能搜索到相应设备数据！");
            }


        } else {
            ToastUtils.getInstance().show(msg);
        }
    }


    private HashMap<String, String> getResult4Search(HashMap<String, List<HashMap<String, String>>> tableHashMap, String key) {
        //update和check模糊查询的时候要传的string数组的list
        mStrArrayList4SearchAndUpdate = new ArrayList<>();

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
            map = searchByKeys(key, type);
            if (null != map && map.size() > 0) {
                return map;
            }
        }
        return map;
    }

    private HashMap<String, String> searchByKeys(String searchKey, String tableName) {
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

    @Override
    public void onBackPressed() {
        mActivity.finish();

    }

    private HashMap<String, List<HashMap<String, String>>> getResult4Search2(HashMap<String, List<HashMap<String, String>>> tableHashMap, String key) {
        //update和check模糊查询的时候要传的string数组的list
        mStrArrayList4SearchAndUpdate = new ArrayList<>();
        HashMap<String, List<HashMap<String, String>>> resultListMap = new HashMap<>();
        for (String type : QRResultActivity.tables) {
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
            Map<String, List<HashMap<String, String>>> listMap = searchByKeys2(key, type);
            if (null != listMap && listMap.size() > 0) {
                resultListMap.putAll(listMap);

            }
        }
        return resultListMap;
    }

    private Map<String, List<HashMap<String, String>>> searchByKeys2(String searchKey, String tableName) {
        //精准查询
        Map<String, List<HashMap<String, String>>> listMap       = new HashMap<>();//table名 和 该table下符合搜索条件的记录组成的list 一一对应
        List<HashMap<String, String>>              resultMapList = new ArrayList<>();
        List<HashMap<String, String>>              mapList;
        //        for (int i = 0; i < mStrArrayList4SearchAndUpdate.size(); i++) {
        //            String[] array = mStrArrayList4SearchAndUpdate.get(i);
        //            if (Arrays.toString(array).contains(searchKey)) {
        mapList = mTableHashMap.get(tableName);
        for (HashMap<String, String> map : mapList) {
            for (String key : mStringsList.get(0)) {
                //                                    if (searchKey.equals(map.get(key)) || map.get(key).contains(searchKey)) {
                if (searchKey.equals(map.get(key))) {
                    String result4Search = map.get(key);//查询的结果
                    //TODO---这个map就是要找的item
                    mResultNameList.add(tableName);
                    resultMapList.add(map);
                    break;
                }
            }
            //                }
            //
            //            }
        }
        if (resultMapList.size() > 0) {
            listMap.put(tableName, resultMapList);
        }
        return listMap;
    }

    private List<String[]> parseIntoArray4Search(HashMap<String, List<HashMap<String, String>>> tableMap, String type) {
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

    private void registers() {
        codeCaptureEngine.register(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case CHOOSE_PHOTO:
                if (resultCode == RESULT_OK) {
                    Bitmap bitmap = null;
                    //判断手机系统版本号
                    if (Build.VERSION.SDK_INT >= 19) {
                        //4.4及以上系统使用这个方法处理图片
                        bitmap = ImageUtil.handleImageOnKitKat(this, data);        //ImgUtil是自己实现的一个工具类
                    } else {
                        //4.4以下系统使用这个方法处理图片
                        bitmap = ImageUtil.handleImageBeforeKitKat(this, data);
                    }
                    if (null != bitmap) {
                        //TODO--解析图片
                    }
                }
                break;
            default:
                break;
        }
    }

    private void unregisters() {
        if (codeCaptureEngine != null) {
            codeCaptureEngine.unregister(this);
        }

    }

/*    @Override
    public void onFailed(String message) {
        ZLog.d("PhotoCropCallBack",message);
    }

    @Override
    public void onPhotoCropped(Uri uri) {
        ZLog.d("PhotoCropCallBack",uri.toString());

    }*/
}
