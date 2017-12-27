//package com.qixiang.framelib.utlis;
//
//import android.net.Uri;
//import android.text.TextUtils;
//
//import com.facebook.binaryresource.BinaryResource;
//import com.facebook.binaryresource.FileBinaryResource;
//import com.facebook.cache.common.CacheKey;
//import com.facebook.drawee.backends.pipeline.Fresco;
//import com.facebook.drawee.backends.pipeline.PipelineDraweeController;
//import com.facebook.drawee.view.SimpleDraweeView;
//import com.facebook.imagepipeline.cache.DefaultCacheKeyFactory;
//import com.facebook.imagepipeline.common.ResizeOptions;
//import com.facebook.imagepipeline.core.ImagePipeline;
//import com.facebook.imagepipeline.core.ImagePipelineFactory;
//import com.facebook.imagepipeline.request.ImageRequest;
//import com.facebook.imagepipeline.request.ImageRequestBuilder;
//
//import java.io.File;
//
///**
// * Created by ZhaoWei on 2016/5/13.
// * 判断 图片有没有缓存
// */
//public class FrescoUtils {
//
//    public static boolean isImageDownloaded(Uri loadUri) {
//        if (loadUri == null) {
//            return false;
//        }
//        CacheKey cacheKey = DefaultCacheKeyFactory.getInstance().getEncodedCacheKey(ImageRequest.fromUri(loadUri), null);
//        return ImagePipelineFactory.getInstance().getMainFileCache().hasKey(cacheKey) || ImagePipelineFactory.getInstance().getSmallImageFileCache().hasKey(cacheKey);
//    }
//
//    //return file or null
//    public static File getCachedImageOnDisk(Uri loadUri) {
//        File localFile = null;
//        if (loadUri != null) {
//            CacheKey cacheKey = DefaultCacheKeyFactory.getInstance().getEncodedCacheKey(ImageRequest.fromUri(loadUri), null);
//            if (ImagePipelineFactory.getInstance().getMainFileCache().hasKey(cacheKey)) {
//                BinaryResource resource = ImagePipelineFactory.getInstance().getMainDiskStorageCache().getResource(cacheKey);
//                localFile = ((FileBinaryResource) resource).getFile();
//            } else if (ImagePipelineFactory.getInstance().getSmallImageFileCache().hasKey(cacheKey)) {
//                BinaryResource resource = ImagePipelineFactory.getInstance().getSmallImageFileCache().getResource(cacheKey);
//                localFile = ((FileBinaryResource) resource).getFile();
//            }
//        }
//        return localFile;
//    }
//
//    /**
//     * 清除所有缓存
//     */
//    public static void clearCache() {
//        ImagePipeline imagePipeline = Fresco.getImagePipeline();
//        imagePipeline.clearMemoryCaches();
//        imagePipeline.clearDiskCaches();
//
//// combines above two lines
//        imagePipeline.clearCaches();
//    }
//
//    /**
//     * 清除缓存
//     *
//     * @param url
//     */
//    public static void clearCache(String url) {
//        if (TextUtils.isEmpty(url) || "null".equals(url)) return;
//        Uri uri = Uri.parse(url);
//        if (isImageDownloaded(uri)) {
//
//            ImagePipeline imagePipeline = Fresco.getImagePipeline();
//            imagePipeline.evictFromMemoryCache(uri);
//            imagePipeline.evictFromDiskCache(uri);
//
//// combines above two lines
//            imagePipeline.evictFromCache(uri);
//        }
//    }
//
//
//    /**
//     * 设置图片大小
//     * @param view
//     * @param uri
//     * @param width
//     * @param height
//     */
//    public static void resizeOptions(SimpleDraweeView view, String uri, int width, int height) {
//        ImageRequest request = ImageRequestBuilder.newBuilderWithSource(Uri.parse(uri))
//                .setResizeOptions(new ResizeOptions(width, height))
//                .build();
//
//        PipelineDraweeController controller = (PipelineDraweeController) Fresco.newDraweeControllerBuilder()
//                .setOldController(view.getController())
//                .setImageRequest(request)
//                .build();
//        view.setController(controller);
//    }
//
//}
