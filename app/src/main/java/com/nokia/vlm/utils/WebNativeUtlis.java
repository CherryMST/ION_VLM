package com.nokia.vlm.utils;

import android.content.Context;
import android.net.Uri;


/**
 * web 与 原生交互
 */

public class WebNativeUtlis {
    public static final String KEY_SHCEME = "zhiqu";

    public static final String KEY_SHARE = "share";

    public static void shouldOverrideUrlLoading(Context context, Uri uri) {

        String scheme = uri.getScheme();
        if (!scheme.equals(KEY_SHCEME)) return;

        String host = uri.getHost();
        if (host.equals(KEY_SHARE)) {//分享
            //分享不需要
        }
    }


}
