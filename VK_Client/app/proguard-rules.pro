# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /home/Andrew Drobyazko (c2q9450@gmail.com)/Android/Sdk/tools/proguard/proguard-android.txt
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

-keep class butterknife.** { *; }
-dontwarn butterknife.internal.**
-keep class **$$ViewBinder { *; }
-keep class **$$ViewInjector { *; }
-dontwarn butterknife.Views$InjectViewProcessor
-dontwarn com.squareup.okhttp.**

-keepclasseswithmembernames class * {
    @butterknife.* <fields>;
}

-keepclasseswithmembernames class * {
    @butterknife.* <methods>;
}

-keepattributes *Annotation*
-keepclassmembers class ** {
    @com.squareup.otto.Subscribe public *;
    @com.squareup.otto.Produce public *;
}

# keep Realm models
-keepclasseswithmembernames class * {
    @com.nethergrim.vk.models.** *;
}
-keepclasseswithmembers class * {
    @com.nethergrim.vk.models.** *;
}
-keepclassmembers class * {
   @com.nethergrim.vk.models.** *;
}
-keep class com.nethergrim.vk.models.** { *; }

# Proguard Configuration for Realm (http://realm.io)
# For detailed discussion see: https://groups.google.com/forum/#!topic/realm-java/umqKCc50JGU
# Additionally you need to keep your Realm Model classes as well
# For example:
# -keep class com.yourcompany.realm.** { *; }

-keep class io.realm.annotations.RealmModule
-keep @io.realm.annotations.RealmModule class *
-dontwarn javax.**
-dontwarn io.realm.**

# Keep Jackson stuff
-keep class org.codehaus.** { *; }
-keep class com.fasterxml.jackson.annotation.** { *; }

# Keep Retrofit
-keep class retrofit.** { *; }
-keepclasseswithmembers class * {
    @retrofit.** *;
}
-keepclassmembers class * {
    @retrofit.** *;
}

# Keep Picasso
-keep class com.squareup.picasso.** { *; }
-keepclasseswithmembers class * {
    @com.squareup.picasso.** *;
}
-keepclassmembers class * {
    @com.squareup.picasso.** *;
}


-dontwarn rx.**

-dontwarn okio.**
-dontwarn com.fasterxml.jackson.databind.**

-keep class com.fasterxml.jackson.databind.ObjectMapper {
    public <methods>;
    protected <methods>;
}
-keep class com.fasterxml.jackson.databind.ObjectWriter {
    public ** writeValueAsString(**);
}

-dontwarn com.squareup.okhttp.**
-dontwarn com.alibaba.fastjson.**
-keep class com.squareup.okhttp.** { *; }
-keep interface com.squareup.okhttp.** { *; }

-dontwarn retrofit.**
-dontwarn retrofit.appengine.UrlFetchClient
-keep class retrofit.** { *; }
-keepclasseswithmembers class * {
    @retrofit.http.* <methods>;
}

-keepattributes Signature
-keepattributes *Annotation*

-keep @io.realm.annotations.RealmModule class *
-dontwarn javax.**
-dontwarn io.realm.**

# Parcel library
-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}

-keep class org.parceler.Parceler$$Parcels

#Fresco
# Keep our interfaces so they can be used by other ProGuard rules.
# See http://sourceforge.net/p/proguard/bugs/466/
-keep,allowobfuscation @interface com.facebook.common.internal.DoNotStrip

# Do not strip any method/class that is annotated with @DoNotStrip
-keep @com.facebook.common.internal.DoNotStrip class *
-keepclassmembers class * {
    @com.facebook.common.internal.DoNotStrip *;
}

# Keep native methods
-keepclassmembers class * {
    native <methods>;
}

-dontwarn okio.**
-dontwarn javax.annotation.**