package com.qx.framelib.utlis;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;

import java.io.File;

/**
 * Created by ZhaoWei on 2016/10/26.
 * 拍照  相册 裁剪
 */

public class SysPhotoCropper {

    /**
     * 默认大小 PX单位
     */
    private int mDefaultSize = 640;
    private PhotoCropCallBack mPhotoCropCallBack;
    private Activity mActivity;
    private Fragment mFragment;
    private float aspectX = 1;
    private int type = 1; // 类型 1：activity 2：fragment

    /**
     * 请求相机
     */
    private static final int REQUEST_TYPE_CAMERA = 311;
    /**
     * 请求图库
     */
    private static final int REQUEST_TYPE_GALLERY = 312;
    /**
     * 剪裁
     */
    private static final int REQUEST_TYPE_CROP = 313;

    /**
     * 临时照片文件名称
     */
//    public static final String TMP_FILE = "default_photo_crop_tmp.jpg";
//    public static final String TMP_FILE2 = "default_photo_crop_tmp_copy.jpg";
//    private Uri mDefaultOutPutUri;
//    private Uri mDefaultOutPutUri_Out; //给拍照的图片换一个位置   原因: 166行注释

    private Uri photoUri;
    private Uri thumbUri;

    private SysPhotoCropper() {
    }


    public SysPhotoCropper(Activity activity, PhotoCropCallBack cropCallBack) {
        this.mActivity = activity;
        this.mPhotoCropCallBack = cropCallBack;
        type = 1;
    }

    public SysPhotoCropper(Fragment fragment, PhotoCropCallBack mPhotoCropCallBack) {
        this.mFragment = fragment;
        this.mPhotoCropCallBack = mPhotoCropCallBack;
        type = 2;
    }

//    private void init() {
//        File tmpFile = new File(
//                FileUtils.getPhotoDirPath(), TMP_FILE);
//        mDefaultOutPutUri = Uri.fromFile(tmpFile);
//    }

    private void initPhotoUri() {
        File tmpFile = new File(
                FileUtils.getPhotoDirPath(), "img_" + System.currentTimeMillis());
        photoUri = Uri.fromFile(tmpFile);
    }

    private void initThumbUri() {
        File tmpFile = new File(
                FileUtils.getThumbDir(), "img_" + System.currentTimeMillis());
        thumbUri = Uri.fromFile(tmpFile);
    }


    public void setAspectX(float aspectX) {
        this.aspectX = aspectX;
    }

    /**
     * 相机拍照剪裁
     */
    public void cropForCamera() {
        if (!Environment.getExternalStorageState().equalsIgnoreCase(
                Environment.MEDIA_MOUNTED)) {
            mPhotoCropCallBack.onFailed("sdcard not found");
            return;
        }
//        if (mDefaultOutPutUri_Out == null) {
//            File tmpFile2 = new File(FileUtils.getPhotoDirPath(), TMP_FILE2);
//            mDefaultOutPutUri_Out = Uri.fromFile(tmpFile2);  //给拍照的图片定义一个资源保存路径
//        }
        initPhotoUri();
        final Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
        if (type != 2) {
            mActivity.startActivityForResult(cameraIntent, REQUEST_TYPE_CAMERA);
        } else {
            mFragment.startActivityForResult(cameraIntent, REQUEST_TYPE_CAMERA);
        }

    }

    /**
     * 图库选择剪裁
     */
    public void cropForGallery() {
        try {
            Intent intent = getPhotoPickIntent();

            if (type != 2) {
                mActivity.startActivityForResult(intent, REQUEST_TYPE_GALLERY);
            } else {
                mFragment.startActivityForResult(intent, REQUEST_TYPE_GALLERY);
            }

        } catch (ActivityNotFoundException e) {
            mPhotoCropCallBack.onFailed("Gallery not found");
        }
    }

    private Intent getPhotoPickIntent() {
        Intent pickImageIntent = new Intent(
                Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        return pickImageIntent;
    }

    private Intent getCropImageIntent(Uri photoUri) {
        if (photoUri != null) {
            ZLog.d("SysPhotoCropper", "裁剪 uri is " + photoUri.toString());
        }
        initThumbUri();

        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(photoUri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", mDefaultSize);
        intent.putExtra("outputY", (int) (mDefaultSize / aspectX));
        intent.putExtra("return-data", false);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, thumbUri);
        intent.putExtra("scale", true);
        //intent.putExtra("scaleUpIfNeeded", true);
        //intent.putExtra("noFaceDetection", true);
        return intent;
    }

    /**
     * 进行剪裁
     *
     * @param uri 输入
     */
    private void doCropPhoto(Uri uri) {
        try {
            if (uri == null)
                //uri = mDefaultOutPutUri; //不能这样写
                uri = photoUri;    //在android4.4 | 5.1系统的某些机型上调用裁剪时,如果图片资源路径和裁剪后保存路径相同
            //会产生冲突，导致裁减完成后图片的大小变成0Byte  最终结果ActivtyResult = 0(Activity.RESULT_CANCELED)
            //从而无法实现裁剪功能, 定义不同的路径可以解决这一问题.
            final Intent intent = getCropImageIntent(uri);
            if (type != 2) {
                mActivity.startActivityForResult(intent, REQUEST_TYPE_CROP);
            } else {
                mFragment.startActivityForResult(intent, REQUEST_TYPE_CROP);
            }

        } catch (Exception e) {
            mPhotoCropCallBack.onFailed("cannot crop image");
        }
    }

    /**
     * 处理OnActivtyResult
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    public void handlerOnActivtyResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case REQUEST_TYPE_CAMERA:
                ZLog.d("SysPhotoCropper", "Camera");
                doCropPhoto(null);
                break;
            case REQUEST_TYPE_GALLERY:
                ZLog.d("SysPhotoCropper", "Gallery");
                Uri imageUri = data.getData();
                doCropPhoto(imageUri);
                break;
            case REQUEST_TYPE_CROP:
                ZLog.d("SysPhotoCropper", "Crop");
                ZLog.d("SysPhotoCropper", "Crop mDefaultOutPutUri=" + thumbUri);
                mPhotoCropCallBack.onPhotoCropped(thumbUri);
                break;
        }
    }

    /**
     * 图片剪裁回调
     * Created by linzb on 16-5-3.
     */
    public interface PhotoCropCallBack {
        void onFailed(String message);

        void onPhotoCropped(Uri uri);
    }

}
