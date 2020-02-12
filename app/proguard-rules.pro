# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

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
-dontwarn com.squareup.3.10.0
-dontwarn okio.3.10.0
-keep public class org.codehaus.3.10.0{3.10.0;}
-keep public class java,nio.3.10.0{3.10.0;}

-dontwarn interfaces.heweather.com.interfacesmodule.2.8.5
-keep class interfaces.heweather.com.interfacesmodule.2.8.5{2.8.5;}