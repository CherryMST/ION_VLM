package com.qx.framelib.utlis;

import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.widget.Toast;

import com.qx.framelib.activity.BaseActivity;
import com.qx.framelib.fragment.BaseFragment;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2016/3/9.
 * 照相  相册 使用
 */
public class CameraAndPhotoUtils {

    /**
     * 获取文件的uri
     *
     * @return
     */
    public static Uri getOutputMediaFileUri(File file) {
        return Uri.fromFile(file);
    }

    public static Uri getOutputMediaFileUri() {
        return Uri.fromFile(getOutputMediaFile());
    }

    public static File getOutputMediaFile() {

        File mediaStorageDir = null;
        try {

            mediaStorageDir = new File(
                    FileUtils.getPhotoDirPath());

            ZLog.d("getOutputMediaFile", "Successfully created mediaStorageDir: "
                    + mediaStorageDir);

        } catch (Exception e) {
            e.printStackTrace();
            ZLog.d("getOutputMediaFile", "Error in Creating mediaStorageDir: "
                    + mediaStorageDir);
        }

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                // 在SD卡上创建文件夹需要权限：
                // <uses-permission
                // android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
                ZLog.d("getOutputMediaFile",
                        "failed to create directory, check if you have the WRITE_EXTERNAL_STORAGE permission");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
                .format(new Date());
        File mediaFile = new File(mediaStorageDir.getPath() + File.separator
                + "IMG_" + timeStamp + ".jpg");

        return mediaFile;

    }

    public static String getRealPathFromURI(Context context, Uri contentUri) {
        String res = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
        }
        cursor.close();
        cursor = null;
        return res;
    }

    public static Bitmap zoomBitmap(Uri imgUri, int width, int height) {
        // If there is no thumbnail image data, the image
        // will have been stored in the target output URI.

        // Resize the full image to fit in out image view.
//        width = img_picker_1.getWidth();
//        height = img_picker_1.getHeight();

        BitmapFactory.Options factoryOptions = new BitmapFactory.Options();

        factoryOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imgUri.getPath(), factoryOptions);
        int imageWidth = factoryOptions.outWidth;
        int imageHeight = factoryOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(imageWidth / width, imageHeight
                / height);

        // Decode the image file into a Bitmap sized to fill the
        // View
        factoryOptions.inJustDecodeBounds = false;
        factoryOptions.inSampleSize = scaleFactor;
        factoryOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(imgUri.getPath(),
                factoryOptions);

        return bitmap;
    }

    /**
     * 文件是否存在
     *
     * @param context
     * @param uri
     * @return
     */
    public static boolean isExists(Context context, Uri uri) {
        String path = getPath(context, uri);
        File file = new File(path);
        return file.exists();
    }


    /**
     * 专为Android4.4设计的从Uri获取文件绝对路径，以前的方法已不好使
     */
    @SuppressLint("NewApi")
    public static String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{split[1]};

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }


    /**
     * 裁剪图片
     *
     * @param activity    activity
     * @param uri         图片源
     * @param outputX     x轴裁剪尺寸
     * @param outputY     y轴裁剪尺寸
     * @param destUri     裁剪后，图片保存位置
     * @param requestCode 裁剪后 图片返回的code
     */
    public static void cropImage(BaseActivity activity, Uri uri, int outputX, int outputY, Uri destUri, int requestCode) {
        ZLog.d("cropImage","图片源:"+uri.toString()+"\n裁剪后，图片保存位置:"+destUri.toString());
        //裁剪图片意图
        Intent intent = new Intent("com.android.camera.action.CROP");
//        intent.setDataAndType(uri, "image/*");

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            String url=getPath(activity,uri);
            intent.setDataAndType(Uri.fromFile(new File(url)), "image/*");
        }else{
            intent.setDataAndType(uri, "image/*");
        }

        intent.putExtra("crop", "true");
        //裁剪框的比例，4：3
        intent.putExtra("aspectX", outputX);
        intent.putExtra("aspectY", outputY);
        //裁剪后输出图片的尺寸大小
        intent.putExtra("outputX", outputX);
        intent.putExtra("outputY", outputY);
        //图片格式
        intent.putExtra("outputFormat", "JPEG");
        intent.putExtra("noFaceDetection", true);
//        boolean return_data = true;
//        if ("xiaomi".equals(android.os.Build.MANUFACTURER.toLowerCase()))
//            return_data = false;
        intent.putExtra("return-data", false); //图片不要bitmap的形式返回
//        if (!return_data)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, destUri); //图片已uri的形式返回
        try {
            activity.startActivityForResult(intent, requestCode);
        } catch (Exception e) {
            ToastUtils.getInstance().show("该图片有问题，请重新选择", Toast.LENGTH_SHORT);
        }

    }

    /**
     * 裁剪图片
     *
     * @param fragment    fragment
     * @param uri         图片源
     * @param outputX     x轴裁剪尺寸
     * @param outputY     y轴裁剪尺寸
     * @param destUri     裁剪后，图片保存位置
     * @param requestCode 裁剪后 图片返回的code
     */
    public static void cropImage(BaseFragment fragment, Uri uri, int outputX, int outputY, Uri destUri, int requestCode) {
        //裁剪图片意图
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        //裁剪框的比例，4：3
        intent.putExtra("aspectX", outputX);
        intent.putExtra("aspectY", outputY);
        //裁剪后输出图片的尺寸大小
        intent.putExtra("outputX", outputX);
        intent.putExtra("outputY", outputY);
        //图片格式
        intent.putExtra("outputFormat", "JPEG");
        intent.putExtra("noFaceDetection", true);
//        boolean return_data = true;
//        if ("xiaomi".equals(android.os.Build.MANUFACTURER.toLowerCase()))
//            return_data = false;
        intent.putExtra("return-data", false); //图片不要bitmap的形式返回
//        if (!return_data)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, destUri); //图片已uri的形式返回
        try {
            fragment.startActivityForResult(intent, requestCode);
        } catch (Exception e) {
            ToastUtils.getInstance().show("该图片有问题，请重新选择", Toast.LENGTH_SHORT);
        }

    }

}
