package com.qx.framelib.utlis;

import android.content.Context;
import android.os.Environment;
import android.os.StatFs;
import android.text.TextUtils;

import com.qx.framelib.application.ZooerApp;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.math.BigDecimal;

/**
 * 文件操作相关工具类放在这里
 *
 * @author luohongbo
 */
public class FileUtils {

    public static final String APP_AMOUNT_ROOT_DIR = "/zhiqu";
    public static final String APP_UNAMOUNT_ROOT_DIR = "/zhiqu";

    public static final String LOG_DIR_PATH = "/log";

    public static final String THUMB_DIR_PATH = "/thumb";

    public static final String CACHE_DIR_PATH = "/cache";

    public static final String CONFIG_DIR_PATH = "/config";

    public static final String APK_DIR_PATH = "/apk";

    public static final String PHOTO_DIR_PATH = "/image";

    //webview开启本地存储时也需要一个cache目录
    public static final String WEBVIEW_CACHE_PATH = "/webview_cache";

    public static String getConfigDir() {
        return getCommonPath(CONFIG_DIR_PATH);
    }

    public static String getThumbDir() {
        return getCommonPath(THUMB_DIR_PATH);
    }

    public static String getCacheDir() {
        return getCommonPath(CACHE_DIR_PATH);
    }


    public static String getLogDir() {
        return getCommonPath(LOG_DIR_PATH);
    }

    public static String getApkDir() {
        return getCommonPath(APK_DIR_PATH);
    }

    public static String getWebViewCacheDir() {
        return getCommonPath(WEBVIEW_CACHE_PATH);
    }

    public static String getPhotoDirPath() {
        return getCommonPath(PHOTO_DIR_PATH);
    }

    public static String getCommonPath(String path) {
        final String rootDir = getCommonRootDir();
        String fullPath = null;
        if (!TextUtils.isEmpty(path)) {
            fullPath = rootDir + path;
        } else {
            fullPath = rootDir;
        }
        return getPath(fullPath, false);
    }

    //文件保存的根目录/mnt/sdcard/zooer
    public static String getCommonRootDir() {
        String dirPath = null;

        // 判断SDCard是否存在并且是可用的
        if (isSDCardExistAndCanWrite()) {
            try {
                dirPath = Environment.getExternalStorageDirectory().getPath()
                        + APP_AMOUNT_ROOT_DIR;
            } catch (Exception e) {
                e.printStackTrace();
                ZLog.d("getCommonRootDir", e.toString());
                return "";
            }

        } else {
            try {
                dirPath = ZooerApp.getAppSelf().getFilesDir().getAbsolutePath()
                        + APP_UNAMOUNT_ROOT_DIR;
            } catch (Exception e) {
                return null;
            }
        }
        File file = new File(dirPath);
        if (!file.exists()) {
            file.mkdirs();
        }
        return file.getAbsolutePath();
    }

    public static String getInnerCachePath() {
        String dirPath = ZooerApp.getAppSelf().getCacheDir() + File.separator;
        File file = new File(dirPath);
        if (!file.exists()) {
            file.mkdirs();
        }
        return dirPath;
    }

    // 判断SDCard是否存在并且是可写的
    public static boolean isSDCardExistAndCanWrite() {
        boolean result = false;
        try {
            result = Environment.MEDIA_MOUNTED.equals(Environment
                    .getExternalStorageState())
                    && Environment.getExternalStorageDirectory().canWrite();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static File getSDCard() {
        if (isSDCardExistAndCanWrite()) {
            return android.os.Environment.getExternalStorageDirectory();
        }
        return null;
    }

    /*
     * 删除指定路径文件
	 *
	 * @param path 文件的完整路径，包括文件名和后缀
	 *
	 * @return boolean，true代表成功
	 */
    public static boolean deleteFile(String path) {
        File dir = new File(path);
        deleteFile(dir);
        return false;
    }

    /*
    * 删除指定路径文件
    *
    * @param path 文件的完整路径，包括文件名和后缀
    *
    * @return boolean，true代表成功
    */
    public static boolean deleteFile(File dir) {
        if (dir.exists() && dir.isFile()) {
            return dir.delete();
        }
        return false;
    }

    /**
     * 将byte[]数据写入到文件缓存
     *
     * @param data 数据
     * @param dest 要保存的文件完整路径，包括文件名和后缀
     * @return 是否成功
     */
    public static boolean write2File(byte[] data, String dest) {
        if (data == null) {
            return false;
        }
        File file = new File(dest);
        if (file.exists()) {

            deleteFile(dest);
        }
        FileOutputStream fos = null;
        try {
            file.createNewFile();
            fos = new FileOutputStream(file);
            fos.write(data);
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            try {
                file.delete();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
        } finally {
            try {
                if (fos != null) {
                    fos.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        // }
        return false;
    }

    public static byte[] readFromAsset(Context context, String assetFileName) {
        InputStream is = null;
        byte[] content = null;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            is = context.getAssets().open(assetFileName);
            byte[] buf = new byte[1024];
            while (true) {
                int numread = is.read(buf);
                if (-1 == numread) {
                    break;
                }
                bos.write(buf, 0, numread);
            }
            content = bos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                }
            }
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return content;
    }

    public static String getPath(String path, boolean nomedia) {
        File file = new File(path);
        if (!file.exists() || !file.isDirectory()) {
            file.mkdirs();
            if (nomedia) {
                File nomediaFile = new File(path + File.separator + ".nomedia");
                try {
                    nomediaFile.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return file.getAbsolutePath();
    }

    /**
     * 读取文件到字节流 baos baos从外部来，外部close，这里不要close baos
     *
     * @param baos 字节流数据
     * @param dest 要读取的文件完整路径，包括文件名和后缀
     */
    public static boolean readFile(String dest, ByteArrayOutputStream baos) {
        File file = new File(dest);
        if (null == baos || !file.exists()) {

            return false;
        }
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
            byte[] buf = new byte[1024];
            while (true) {
                int numread = fis.read(buf);
                if (-1 == numread) {
                    break;
                }
                baos.write(buf, 0, numread);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        if (baos.size() > 0) {
            return true;
        } else {
            return false;
        }

    }


    /**
     * 计算剩余空间
     *
     * @param path
     * @return
     */
    public static long getAvailableSize(String path) {
        StatFs fileStats = new StatFs(path);
        fileStats.restat(path);
        return (long) fileStats.getAvailableBlocks() * fileStats.getBlockSize();
    }

    /**
     * 获得手机容量
     *
     * @return
     */
    public static long getPhoneStorageAvailableCapacity() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long nAvailableCount = stat.getBlockSize() * ((long) stat.getAvailableBlocks() - 4);
        if (nAvailableCount < 0l) {
            return 0l;
        }
        return nAvailableCount;
    }

    /**
     * 获得sd卡容量
     *
     * @return
     */
    public static long getExternalStorageAvailableCapacity() {
        String root = null;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            root = Environment.getExternalStorageDirectory().getPath();
        } else {
            return 0;
        }

        File base = new File(root);
        StatFs stat = new StatFs(base.getPath());
        long nAvailableCount = stat.getBlockSize() * ((long) stat.getAvailableBlocks() - 4);
        if (nAvailableCount < 0l) {
            return 0l;
        }
        return nAvailableCount;
    }

    /**
     * 创建一个指定路径的文件，并且指定大小
     *
     * @param path
     * @param size
     * @throws IOException
     */
    public static boolean createFileWithSpecialSize(String path, long size) {
        File file = new File(path);
        if (file.exists()) {
            file.delete();
        }
        RandomAccessFile accessFile = null;
        try {
            accessFile = new RandomAccessFile(path, "rw");
            accessFile.setLength(size);
            return true;
        } catch (IOException e1) {
            e1.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } catch (OutOfMemoryError error) {
        } finally {
            if (accessFile != null) {
                try {
                    accessFile.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    public static boolean copyRawFile_s(File src, File dst) throws Exception {
        if (!src.exists() || src.isDirectory()) {
            return false;
        }
        if (!dst.isFile()) {
            deleteDir(dst);
        }
        if (!dst.exists()) {
            dst.createNewFile();
        }
        if (!dst.exists()) {
            return false;
        }

        FileInputStream fis = null;
        try {
            fis = new FileInputStream(src);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(dst);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if (fos == null) {
            try {
                fis.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return false;
        }
        byte[] buf = new byte[1024 * 4];

        int read = 1;
        try {
            while (read > 0) {
                read = fis.read(buf);
                if (read > 0) {
                    fos.write(buf, 0, read);
                    fos.flush();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }

        return true;
    }

    public static boolean deleteDir(File dir) throws Exception {
        if (dir.isDirectory()) {
            String[] child = dir.list();

            for (int i = 0; i < child.length; i++) {
                boolean bool = deleteDir(new File(dir, child[i]));
                if (!bool) return bool;
            }
            try {
                dir.delete();
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
            return true;
        } else {
            boolean deleted = false;
            try {
                deleted = dir.delete();
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
            if (!deleted) {
            }
            return deleted;
        }
    }

    public static int readFromFile(File src, byte[] bytes, int maxRead) {
        if (src == null || bytes == null || maxRead <= 0)
            return -1;

        FileInputStream fis = null;

        try {
            fis = new FileInputStream(src);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if (fis == null)
            return -1;

        int read = 0;
        try {
            read = fis.read(bytes, 0, maxRead);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        if (fis != null) {
            try {
                fis.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return read;
    }


    /**
     * 获取文件夹大小
     *
     * @param file File实例
     * @return long 单位为M
     * @throws Exception
     */
    public static long getFolderSize(File file) throws Exception {
        long size = 0;
        File[] fileList = file.listFiles();

        for (int i = 0; i < fileList.length; i++) {
            if (fileList[i].isDirectory()) {
                size = size + getFolderSize(fileList[i]);
            } else {
                size = size + fileList[i].length();

            }
        }
        return size;
    }

    /**
     * 格式化文件单位
     *
     * @param size
     * @return
     */
    public static String formatFolderSize(long size) {
        double kiloByte = size / 1024;
        if (kiloByte < 1) {
            return size + "B";
        }

        double megaByte = kiloByte / 1024;
        if (megaByte < 1) {
            BigDecimal result1 = new BigDecimal(Double.toString(kiloByte));
            return result1.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "KB";
        }

        double gigaByte = megaByte / 1024;
        if (gigaByte < 1) {
            BigDecimal result2 = new BigDecimal(Double.toString(megaByte));
            return result2.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "MB";
        }

        double teraBytes = gigaByte / 1024;
        if (teraBytes < 1) {
            BigDecimal result3 = new BigDecimal(Double.toString(gigaByte));
            return result3.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "GB";
        }
        BigDecimal result4 = new BigDecimal(teraBytes);
        return result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "TB";
    }

}
