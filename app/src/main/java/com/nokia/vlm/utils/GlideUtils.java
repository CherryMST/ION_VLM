package com.nokia.vlm.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;

import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;


public class GlideUtils {

    /**
     * 圆形图片
     *
     * @param context
     * @return
     */
    public static Transformation bitmapCropCircleTransformation(Context context) {
        return new BitmapTransformation(context) {
            @Override
            protected Bitmap transform(BitmapPool pool, Bitmap source, int outWidth, int outHeight) {
                if (source == null) return null;

                int size = Math.min(source.getWidth(), source.getHeight());
                int x = (source.getWidth() - size) / 2;
                int y = (source.getHeight() - size) / 2;

                // TODO this could be acquired from the pool too
                Bitmap squared = Bitmap.createBitmap(source, x, y, size, size);

                Bitmap result = pool.get(size, size, Bitmap.Config.ARGB_8888);
                if (result == null) {
                    result = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
                }

                Canvas canvas = new Canvas(result);
                Paint paint = new Paint();
                paint.setShader(new BitmapShader(squared, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP));
                paint.setAntiAlias(true);
                float r = size / 2f;
                canvas.drawCircle(r, r, r, paint);
                return result;
            }

            @Override
            public String getId() {
                return "bitmapCropCircleTransformation";
            }
        };
    }

    /**
     * 圆角
     * @param context
     * @param radius
     * @return
     */
    public static Transformation bitmapRadiusTransformation(Context context, final int radius) {
        return new BitmapTransformation(context) {
            @Override
            protected Bitmap transform(BitmapPool mBitmapPool, Bitmap source, int outWidth, int outHeight) {

                int width = source.getWidth();
                int height = source.getHeight();

                Bitmap bitmap = mBitmapPool.get(width, height, Bitmap.Config.ARGB_8888);
                if (bitmap == null) {
                    bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
                }

                Canvas canvas = new Canvas(bitmap);
                Paint paint = new Paint();
                paint.setAntiAlias(true);
                paint.setShader(new BitmapShader(source, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP));

                canvas.drawRoundRect(new RectF(0, 0, width, height), radius, radius, paint);

                return bitmap;
            }


            @Override
            public String getId() {
                return "bitmapRadiusTransformation";
            }


        };
    }


}
