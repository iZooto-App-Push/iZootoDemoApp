# ===============================
# 🛠️ General Android Optimization
# ===============================
-keep class * extends android.content.BroadcastReceiver
-keep class * extends android.app.Service
-keep class * extends android.app.Application
-keepclassmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}

# ===============================
# 🔇 Remove Log statements
# ===============================
#-assumenosideeffects class android.util.Log {
#    public static *** d(...);
#    public static *** v(...);
#    public static *** i(...);
#    public static *** w(...);
#    public static *** e(...);
#}

# ===============================
# 📊 comScore
# ===============================
-keep class com.comscore.** { *; }
-dontwarn com.comscore.**

# ===============================
# 🚀 Google Mobile Ads SDK
# ===============================
-keep public class com.google.android.gms.ads.** {
    public *;
}
-keep public class com.google.ads.** {
    public *;
}
-keep class com.google.android.gms.** { *; }
-dontwarn com.google.android.gms.**

# ===============================
# 📦 Retrofit, OkHttp, Okio
# ===============================
-keep class okhttp3.** { *; }
-keep interface okhttp3.** { *; }
-dontwarn okhttp3.**
-dontwarn okio.**
-dontnote okhttp3.internal.Platform
-dontwarn okio.Okio
-dontwarn okio.DeflaterSink

# ===============================
# 🖼️ Glide
# ===============================
-dontwarn com.bumptech.glide.**
-keep class com.bumptech.glide.** { *; }

# ===============================
# 🧪 Huawei (suppress known missing classes)
# ===============================
-dontwarn com.huawei.agconnect.config.AGConnectServicesConfig
-dontwarn com.huawei.hms.**
-dontwarn com.huawei.hianalytics.**

# ===============================
# 🎯 Install Referrer
# ===============================
-keep public class com.android.installreferrer.** { *; }

# ===============================
# 🌐 WebView JavaScript Interface (if used)
# ===============================
# Uncomment and replace with your JS interface if you use WebView JS
# -keepclassmembers class com.example.MyWebViewJSInterface {
#     public *;
# }

# ===============================
# 🧱 Kotlin & Annotations
# ===============================
-keep class kotlin.jvm.internal.** { *; }
-keepattributes Signature
-keepattributes *Annotation*

# ===============================
# 📌 Optional Library-Specific
# ===============================
# DiscreteScrollView
-keep class com.yarolegovich.discretescrollview.** { *; }

# ===============================
# 🧼 Optional: Debug Info
# ===============================
# Uncomment to preserve line numbers in stack traces
# -keepattributes SourceFile,LineNumberTable

# Uncomment to hide original file names
# -renamesourcefileattribute SourceFile
-keep class com.google.firebase.** { *; }
-dontwarn com.google.firebase.**
-keep class com.facebook.infer.annotation.** { *; }
-keep class com.facebook.ads.** { *; }
-keep class com.facebook.** { *; }
-dontwarn com.facebook.infer.annotation.**
