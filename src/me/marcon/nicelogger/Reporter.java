package me.marcon.nicelogger;

import android.location.Location;
import android.os.Bundle;

public interface Reporter {
   
    public void onCreate(Bundle bundle);

    public void v(String tag, String message, Throwable t);

    public void v(String tag, String message);

    public void d(String tag, String message, Throwable t);

    public void d(String tag, String message);

    public void i(String tag, String message, Throwable t);

    public void i(String tag, String message);

    public void w(String tag, String message, Throwable t);

    public void w(String tag, String message);

    public void e(String tag, String message, Throwable t);

    public void e(String tag, String message);

    public void wtf(String tag, String message, Throwable t);

    public void wtf(String tag, String message);

    public void location(String tag, Location location);
    
    public void onSaveInstanceState(Bundle bundle);
    
    public void stop();

}