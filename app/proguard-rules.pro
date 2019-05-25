# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in C:\Users\2136\AppData\Local\Android\Sdk/tools/proguard/proguard-android.txt
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

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile
-optimizationpasses 5
  -dontusemixedcaseclassnames
  -dontskipnonpubliclibraryclasses
   -dontpreverify
 -verbose
   -optimizations !code/simplification/arithmetic,!field/*,!class/merging/*

   -keep public class * extends android.app.Activity
  -keep public class * extends android.app.Application
   -keep public class * extends android.app.Service
  -keep public class * extends android.content.BroadcastReceiver
   -keep public class * extends android.content.ContentProvider
   -keep public class * extends android.app.backup.BackupAgentHelper
  -keep public class * extends android.preference.Preference
  -keep public class com.android.vending.licensing.ILicensingService

   #keep all classes that might be used in XML layouts
 -keep public class * extends android.view.View
  -keep public class * extends android.app.Fragment
 -keep public class * extends android.support.v4.Fragment


-keep public class * extends android.content.pm.IPackageStatsObserver
-keep public class * implements android.content.pm.IPackageStatsObserver

-keepclassmembers class * {

public static android.content.pm.IPackageStatsObserver asInterface(android.os.IBinder);



}

  #keep all public and protected methods that could be used by java reflection

  -keep class * extends java.util.ListResourceBundle {
    protected Object[][] getContents();
}

-keep public class com.google.android.gms.common.internal.safeparcel.SafeParcelable {
    public static final *** NULL;
}

-keepnames @com.google.android.gms.common.annotation.KeepName class *
-keepclassmembernames class * {
    @com.google.android.gms.common.annotation.KeepName *;
}

-keepnames class * implements android.os.Parcelable {
    public static final ** CREATOR;
}

-keepclassmembernames class * {
     public protected <methods>;
 }

  -keepclasseswithmembernames class * {
     native <methods>;
  }

  -keepclasseswithmembernames class * {
       public <init>(android.content.Context, android.util.AttributeSet);
   }

  -keepclasseswithmembernames class * {
      public <init>(android.content.Context, android.util.AttributeSet, int);
 }


  -keepclassmembers enum * {
     public static **[] values();
       public static ** valueOf(java.lang.String);
  }

 -keep class * implements android.os.Parcelable {
    public static final android.os.Parcelable$Creator *;
  }
   -keep class org.apache.http.** { *; }

  -dontwarn **CompatHoneycomb
  -dontwarn org.htmlcleaner.*
  -dontwarn android.support.**
  -dontwarn com.google.**
  -dontwarn org.apache.http.*
  -dontwarn android.net.http.**
  -dontwarn org.apache.commons.**
  -keep class android.support.v7.widget.** { *; }
  -keep class com.crashlytics.** { *; }
  -dontwarn com.crashlytics.**