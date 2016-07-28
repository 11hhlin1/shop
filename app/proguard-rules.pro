# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in D:\android\sdk/tools/proguard/proguard-android.txt
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
-dontwarn java.nio.file.**

-keep class com.tencent.mm.sdk.openapi.WXMediaMessage {
  *;
}
-keep class com.tencent.mm.sdk.openapi.** implements com.tencent.mm.sdk.openapi.WXMediaMessage$IMediaObject {
  *;
}
-keep class com.tencent.mm.sdk.** {
   *;
}
-keepclassmembers class * implements android.os.Parcelable {
    static ** CREATOR;
}
-keepclassmembers class * implements android.os.Parcelable {
    static ** CREATOR;
}
-keepclassmembers class ** {
    public void onEvent*(**);
}
# Only required if you use AsyncExecutor
-keepclassmembers class * extends de.greenrobot.event.util.ThrowableFailureEvent {
    <init>(java.lang.Throwable);
}
-keep public class com.gjj.shop.R$*{
public static final int *;
}
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keepclassmembers class * {
   public <init> (org.json.JSONObject);
}
-keep public class * implements butterknife.internal.ViewBinder { public <init>(); }

# Prevent obfuscation of types which use ButterKnife annotations since the simple name
# is used to reflectively look up the generated ViewBinder.
-keep class butterknife.*
-keepclasseswithmembernames class * { @butterknife.* <methods>; }
-keepclasseswithmembernames class * { @butterknife.* <fields>; }
-dontshrink
-dontnote
-renamesourcefileattribute ProGuard
-keepattributes SourceFile,LineNumberTable
-keep class com.sina.** {
  *;
}

-dontwarn java.lang.invoke.**
-dontwarn sun.misc.Unsafe
-dontwarn com.sina.weibo.**
-dontwarn com.alibaba.fastjson.**

-keep class com.alibaba.fastjson.** { *; }

-keepattributes Signature

#-keepattributes *Annotation*
-keep public class * implements java.io.Serializable {

        public *;

}