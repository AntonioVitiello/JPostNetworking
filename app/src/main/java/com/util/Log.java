package com.util;

import android.content.Context;
import android.support.annotation.Nullable;

import com.example.jpost.networking.R;

/**
 * Created by antlap on 30/11/2017.
 */

public class Log {
    private static final int APP_TAG_MAX_LENGHT = 23;
    private static String DEBUG_TAG = null;
    private static boolean INITIALIZED = false;
    private static boolean DEBUG = true;

    public static void init(boolean debug, String debugTag) {
        DEBUG_TAG = debugTag;
        init(null, debug);
    }

    public static void init(@Nullable Context context, boolean debug) {
        if (!INITIALIZED) {
            INITIALIZED = true;
            DEBUG = debug;
            if (DEBUG && context != null) {
                String appName = context.getResources().getString(R.string.app_name);
                DEBUG_TAG = appName.substring(0, Math.min(appName.length(), APP_TAG_MAX_LENGHT)) + " ";
            }
        }
    }

    public static void v(String tag, String msg, Object... args) {
        if (DEBUG) {
            String logTag = DEBUG_TAG != null ? DEBUG_TAG : tag;
            android.util.Log.v(logTag, formatMessage(msg, args));
        }
    }

    public static void v(String tag, String msg, Throwable throwable, Object... args) {
        if (DEBUG) {
            String logTag = DEBUG_TAG != null ? DEBUG_TAG : tag;
            android.util.Log.v(logTag, formatMessage(msg, args), throwable);
        }
    }

    public static void d(String tag, String msg, Object... args) {
        if (DEBUG) {
            String logTag = DEBUG_TAG != null ? DEBUG_TAG : tag;
            android.util.Log.d(logTag, formatMessage(msg, args));
        }
    }

    public static void d(String tag, String msg, Throwable throwable, Object... args) {
        if (DEBUG) {
            String logTag = DEBUG_TAG != null ? DEBUG_TAG : tag;
            android.util.Log.d(logTag, formatMessage(msg, args), throwable);
        }
    }

    public static void i(String tag, String msg, Object... args) {
        if (DEBUG) {
            String logTag = DEBUG_TAG != null ? DEBUG_TAG : tag;
            android.util.Log.i(logTag, formatMessage(msg, args));
        }
    }

    public static void i(String tag, String msg, Throwable throwable, Object... args) {
        if (DEBUG) {
            String logTag = DEBUG_TAG != null ? DEBUG_TAG : tag;
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
