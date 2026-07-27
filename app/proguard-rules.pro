# Xposed 模块 - 混淆时必须保留的规则

# YukiHookAPI
-keep class com.highcapable.yukihookapi.** { *; }
-keep class * extends com.highcapable.yukihookapi.hook.entity.YukiBaseHooker { *; }

# Xposed
-keep class de.robv.android.xposed.** { *; }
-keep class * implements de.robv.android.xposed.IXposedHookLoadPackage { *; }
-keep class * implements de.robv.android.xposed.IXposedHookInitPackageResources { *; }

# Sesame 入口
-keep class io.github.lazyimmortal.sesame.hook.HookEntry { *; }
-keep class io.github.lazyimmortal.sesame.hook.ApplicationHook { *; }

# Lombok
-keep class lombok.** { *; }
-dontwarn lombok.**

# 反射调用涉及的类
-keep class com.alipay.mobile.** { *; }
-keep class com.alibaba.ariver.** { *; }

# Model 类（通过反射加载）
-keep class io.github.lazyimmortal.sesame.model.** { *; }
-keep class io.github.lazyimmortal.sesame.data.** { *; }

# 序列化相关
-keepattributes *Annotation*
-keep class com.fasterxml.jackson.** { *; }
-dontwarn com.fasterxml.jackson.databind.**

# OkHttp
-dontwarn okhttp3.**
-dontwarn okio.**

# Native 方法
-keepclasseswithmembernames class * {
    native <methods>;
}

# R8 优化
-keepattributes Signature
-keepattributes InnerClasses
