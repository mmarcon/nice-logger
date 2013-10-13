package me.marcon.nicelogger;

import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;

public class L {

    private static final boolean ENABLED = true;

    private static Reporter sReporter = null;
    
    public static void init(ViewGroup parentView) {
        if(sReporter == null) {
            sReporter = new NiceReporter();
        }
        sReporter.initView(parentView);
    }
    
    public static void stop() {
        if(sReporter != null) {
            sReporter.stop();
        }
    }
    
    public static void onCreate(Bundle bundle) {
        if(sReporter != null) {
            sReporter.onCreate(bundle);
        }
    }
    
    public static void onSaveInstanceState(Bundle bundle) {
        if(sReporter != null) {
            sReporter.onSaveInstanceState(bundle);
        }
    }

    public static void v(Throwable t, String... args) {
        if (!ENABLED) {
            return;
        }

        String[] tagAndArgs = tagAndArgs(args);
        Log.v(tagAndArgs[0], tagAndArgs[1], t);
        sReporter.v(tagAndArgs[0], tagAndArgs[1], t);
    }

    public static void v(String... args) {
        if (!ENABLED) {
            return;
        }

        String[] tagAndArgs = tagAndArgs(args);
        Log.v(tagAndArgs[0], tagAndArgs[1]);
        sReporter.v(tagAndArgs[0], tagAndArgs[1]);
    }

    public static void d(Throwable t, String... args) {
        if (!ENABLED) {
            return;
        }

        String[] tagAndArgs = tagAndArgs(args);
        Log.d(tagAndArgs[0], tagAndArgs[1], t);
        sReporter.d(tagAndArgs[0], tagAndArgs[1], t);
    }

    public static void d(String... args) {
        if (!ENABLED) {
            return;
        }

        String[] tagAndArgs = tagAndArgs(args);
        Log.d(tagAndArgs[0], tagAndArgs[1]);
        sReporter.d(tagAndArgs[0], tagAndArgs[1]);
    }

    public static void i(Throwable t, String... args) {
        if (!ENABLED) {
            return;
        }

        String[] tagAndArgs = tagAndArgs(args);
        Log.i(tagAndArgs[0], tagAndArgs[1], t);
        sReporter.i(tagAndArgs[0], tagAndArgs[1], t);
    }

    public static void i(String... args) {
        if (!ENABLED) {
            return;
        }

        String[] tagAndArgs = tagAndArgs(args);
        Log.i(tagAndArgs[0], tagAndArgs[1]);
        sReporter.i(tagAndArgs[0], tagAndArgs[1]);
    }

    public static void w(Throwable t, String... args) {
        if (!ENABLED) {
            return;
        }

        String[] tagAndArgs = tagAndArgs(args);
        Log.w(tagAndArgs[0], tagAndArgs[1], t);
        sReporter.w(tagAndArgs[0], tagAndArgs[1], t);
    }

    public static void w(String... args) {
        if (!ENABLED) {
            return;
        }

        String[] tagAndArgs = tagAndArgs(args);
        Log.w(tagAndArgs[0], tagAndArgs[1]);
        sReporter.w(tagAndArgs[0], tagAndArgs[1]);
    }

    public static void e(Throwable t, String... args) {
        if (!ENABLED) {
            return;
        }

        String[] tagAndArgs = tagAndArgs(args);
        Log.e(tagAndArgs[0], tagAndArgs[1], t);
        sReporter.e(tagAndArgs[0], tagAndArgs[1], t);
    }

    public static void e(String... args) {
        if (!ENABLED) {
            return;
        }

        String[] tagAndArgs = tagAndArgs(args);
        Log.e(tagAndArgs[0], tagAndArgs[1]);
        sReporter.e(tagAndArgs[0], tagAndArgs[1]);
    }

    public static void wtf(Throwable t, String... args) {
        if (!ENABLED) {
            return;
        }

        String[] tagAndArgs = tagAndArgs(args);
        Log.wtf(tagAndArgs[0], tagAndArgs[1], t);
        sReporter.wtf(tagAndArgs[0], tagAndArgs[1], t);
    }

    public static void wtf(String... args) {
        if (!ENABLED) {
            return;
        }

        String[] tagAndArgs = tagAndArgs(args);
        Log.wtf(tagAndArgs[0], tagAndArgs[1]);
        sReporter.wtf(tagAndArgs[0], tagAndArgs[1]);
    }
    
    public static void location(String tag, Location location) {
        if (!ENABLED) {
            return;
        }
        
        sReporter.location(tag, location);
    }

    private static String[] tagAndArgs(String... strings) {
        String[] tagAndArgs = new String[2];
        tagAndArgs[0] = strings[0];
        StringBuilder builder = new StringBuilder();
        for (int i = 1; i < strings.length; i++) {
            builder.append(strings[i]);
        }
        tagAndArgs[1] = builder.toString();
        return tagAndArgs;
    }
}
