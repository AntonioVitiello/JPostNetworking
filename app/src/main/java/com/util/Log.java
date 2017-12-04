package com.util;

/**
 * Created by antlap on 30/11/2017.
 */

public class Log {
    private static String sLogTag = null;
    private static boolean sIsLogTagDefined = false;
    private static boolean sIsLogTagConcat = false;
    private static boolean sDebug = true;

    public static void init(boolean debug, String logTag, boolean isLogTagConcat) {
        sDebug = debug;
        sIsLogTagDefined = logTag != null;
        if(sIsLogTagDefined) {
            sLogTag = logTag;
            sIsLogTagConcat = isLogTagConcat;
        }
    }

    public static void v(String tag, String msg, Object... args) {
        if (sDebug) {
            String logTag = sIsLogTagDefined ? (sIsLogTagConcat ? sLogTag + tag : sLogTag) : tag;
            android.util.Log.v(logTag, formatMessage(msg, args));
        }
    }

    public static void v(String tag, String msg, Throwable throwable, Object... args) {
        if (sDebug) {
            String logTag = sIsLogTagDefined ? (sIsLogTagConcat ? sLogTag + tag : sLogTag) : tag;
            android.util.Log.v(logTag, formatMessage(msg, args), throwable);
        }
    }

    public static void d(String tag, String msg, Object... args) {
        if (sDebug) {
            String logTag = sIsLogTagDefined ? (sIsLogTagConcat ? sLogTag + tag : sLogTag) : tag;
            android.util.Log.d(logTag, formatMessage(msg, args));
        }
    }

    public static void d(String tag, String msg, Throwable throwable, Object... args) {
        if (sDebug) {
            String logTag = sIsLogTagDefined ? (sIsLogTagConcat ? sLogTag + tag : sLogTag) : tag;
            android.util.Log.d(logTag, formatMessage(msg, args), throwable);
        }
    }

    public static void i(String tag, String msg, Object... args) {
        if (sDebug) {
            String logTag = sIsLogTagDefined ? (sIsLogTagConcat ? sLogTag + tag : sLogTag) : tag;
            android.util.Log.i(logTag, formatMessage(msg, args));
        }
    }

    public static void i(String tag, String msg, Throwable throwable, Object... args) {
        if (sDebug) {
            String logTag = sIsLogTagDefined ? (sIsLogTagConcat ? sLogTag + tag : sLogTag) : tag;
            android.util.Log.i(logTag, formatMessage(msg, args), throwable);
        }
    }

    public static void w(String tag, String msg, Object... args) {
        android.util.Log.w(tag, formatMessage(msg, args));
    }

    public static void w(String tag, Throwable throwable) {
        android.util.Log.w(tag, throwable);
    }

    public static void w(String tag, String msg, Throwable throwable, Object... args) {
        android.util.Log.w(tag, formatMessage(msg, args), throwable);
    }

    public static void e(String tag, String msg, Object... args) {
        android.util.Log.e(tag, formatMessage(msg, args));
    }

    public static void e(String tag, Throwable throwable) {
        android.util.Log.e(tag, formatMessage(null), throwable);
    }

    public static void e(String tag, String msg, Throwable throwable, Object... args) {
        android.util.Log.e(tag, formatMessage(msg, args), throwable);
    }

    public static void wtf(String tag, String msg, Object... args) {
        android.util.Log.wtf(tag, formatMessage(msg, args));
    }

    public static void wtf(String tag, Throwable throwable) {
        android.util.Log.wtf(tag, throwable);
    }

    public static void wtf(String tag, String msg, Throwable throwable, Object... args) {
        android.util.Log.wtf(tag, formatMessage(msg, args), throwable);
    }

    /* eg: String output = formatMessage("My name is: %s, age: %d", "Joe", 35);
        => output = "My name is: Joe, age: 35"
       see: https://dzone.com/articles/java-string-format-examples
            https://docs.oracle.com/javase/8/docs/api/index.html
     */
    private static String formatMessage(String msg, Object... args) {
        String message = msg;
        if (message == null) {
            message = "";
        } else if (args.length > 0) {
            message = String.format(message, args);
        }
        return message;
    }
}