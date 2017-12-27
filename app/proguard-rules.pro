# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in C:\Users\Administrator\AppData\Local\Android\Sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}
-keepclassmembers class fqcn.of.javascript.interface.for.webview {
   public *;
}

#指定代码的压缩级别
#-optimizationpasses 5

#包明不混合大小写
-dontusemixedcaseclassnames

#不去忽略非公共的库类
-dontskipnonpubliclibraryclasses

 #优化  不优化输入的类文件
-dontoptimize

 #预校验
-dontpreverify

 #混淆时是否记录日志
-verbose

#保留Annotation不混淆
-keepattributes *Annotation*,InnerClasses
-keepattributes EnclosingMethod

#避免混淆泛型
-keepattributes Signature

-ignorewarnings

 # 混淆时所采用的算法
#-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*

#####################################################################
###                Keep options for Android system                ###
#####################################################################

-keep public class * extends android.preference.Preference
-keep public class * extends android.view.View
-keep class * extends android.widget.BaseAdapter
# 发布的时候将下面一行去掉
-keepclasseswithmembers public class * extends android.support.v4.app.Fragment
-keepclasseswithmembers public class * extends android.support.v4.app.FragmentActivity
-keepclasseswithmembers public class * extends android.app.Activity
-keepclasseswithmembers public class * extends android.app.Application
-keepclasseswithmembers public class * extends android.app.Service
-keepclasseswithmembers public class * extends android.app.IntentService
-keepclasseswithmembers public class * extends android.content.BroadcastReceiver
-keepclasseswithmembers public class * extends android.content.ContentProvider
-keepclasseswithmembers public class * extends android.app.backup.BackupAgentHelper
-keepclasseswithmembers public class * extends android.preference.Preference
-keepclasseswithmembers public class com.android.vending.licensing.ILicensingService
-keepclasseswithmembers public class * extends android.database.sqlite.SQLiteOpenHelper {*;}
-keepclasseswithmembers public class com.facebook.drawee.R {*;}
-keepclasseswithmembers  class com.facebook.drawee.R$* {*;}
#
-keep class com.facebook.**{*;}

-keepclasseswithmembernames class *{
	native <methods>;
}

#不要混淆BaseRequestJson的所有属性与方法
-keepclasseswithmembers class com.qixiangnet.zhiqu.json.request.BaseRequestJson {
    <fields>;
    <methods>;
}

#不要混淆BaseRequestJson所有子类的属性与方法
-keepclasseswithmembers class * extends com.qixiangnet.zhiqu.json.request.BaseRequestJson{
    <fields>;
    <methods>;
}

-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}

-keep class **.R$* {
*;
}

-dontwarn javax.annotation.**


#okhttputils
-dontwarn com.zhy.http.**
-keep class com.zhy.http.**{*;}
-keep interface com.zhy.http.**{*;}

#okhttp
-dontwarn okhttp3.**
-keep class okhttp3.**{*;}
-keep interface okhttp3.**{*;}

#okio
-dontwarn okio.**
-keep class okio.**{*;}
-keep interface okio.**{*;}


#zxing
-keep class com.google.zxing.**{*;}
-dontwarn com.google.zxing.**
-keep class com.qixiang.samonxu.**{*;}
-dontwarn com.qixiang.samonxu.**

#==================gson==========================
-dontwarn com.google.**
-keep class com.google.gson.** {*;}

#==================protobuf======================
-dontwarn com.google.**
-keep class com.google.protobuf.** {*;}


#百度地图
-keep class com.baidu.** {*;}
-keep class vi.com.** {*;}
-dontwarn com.baidu.**

#zxing
-keep class com.google.zxing.**{*;}
-dontwarn com.google.zxing.**
-keep class com.qixiang.samonxu.**{*;}
-dontwarn com.qixiang.samonxu.**

#扫描相关
-dontwarn com.example.uartdemo.**
-keep class com.example.uartdemo.** { *;}
-dontwarn com.qixiangnet.jianzhu.rfid.**
-keep class com.qixiangnet.jianzhu.rfid.** { *;}


#jpush
#-dontwarn cn.jpush.**
#-keep class cn.jpush.** { *; }
-dontoptimize
-dontpreverify

-dontwarn cn.jpush.**
-keep class cn.jpush.** { *; }

-dontwarn cn.jiguang.**
-keep class cn.jiguang.** { *; }


#混淆支付
-dontwarn com.alipay.**
-keep class com.alipay.android.app.IAlixPay{*;}
-keep class com.alipay.android.app.IAlixPay$Stub{*;}
-keep class com.alipay.android.app.IRemoteServiceCallback{*;}
-keep class com.alipay.android.app.IRemoteServiceCallback$Stub{*;}
-keep class com.alipay.sdk.app.PayTask{ public *;}
-keep class com.alipay.sdk.app.AuthTask{ public *;}



#2D地图
#-keep class com.amap.api.maps2d.**{*;}
#-keep class com.amap.api.mapcore2d.**{*;}

#3D 地图 V5.0.0之后：
-keep   class com.amap.api.maps.**{*;}
-keep   class com.autonavi.**{*;}
-keep   class com.amap.api.trace.**{*;}

#定位
-keep class com.amap.api.location.**{*;}
-keep class com.amap.api.fence.**{*;}
-keep class com.autonavi.aps.amapapi.model.**{*;}

#Glide
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}

# for DexGuard only
#-keepresourcexmlelements manifest/application/meta-data@value=GlideModule

#七牛
-keep class com.qiniu.**{*;}
-keep class com.qiniu.**{public <init>();}
-ignorewarnings


#sharesdk
-keep class cn.sharesdk.**{*;}
-keep class com.sina.**{*;}
-keep class **.R{*;}
-keep class com.mob.**{*;}
-dontwarn com.mob.**
-dontwarn cn.sharesdk.**
-dontwarn **.R$*

#融云

-keepattributes Exceptions,InnerClasses

-keepattributes *Annotation*

-keep class com.google.gson.examples.android.model.** { *; }

-keep class **$Properties
-dontwarn org.eclipse.jdt.annotation.**

#-libraryjars libs/agora-rtc-sdk.jar
#-keep class io.agora.rtc.** {*;}

-keep class io.rong.**{*;}
#-keep class io.agora.rtc.**{*; }
-keep class * implements io.rong.imlib.model.MessageContent{*;}

-dontwarn io.rong.push.**
-dontnote com.xiaomi.**
-dontnote com.huawei.android.pushagent.**
-dontnote com.google.android.gms.gcm.**
-dontnote io.rong.**

-keep class com.qixiangnet.zhiqu.imkit.IMKitNotificationReceiver {*;}
