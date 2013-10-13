package me.marcon.nicelogger;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import me.marcon.nicelogger.device.Network;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

public class NiceReporter implements Reporter {

    private static final long UPDATE_INTERVAL = 10000;
    private static final int SEMI_OPAQUE = 30;
    private static final int FULLY_OPAQUE = 255;

    private static final String BUNDLED_ENTRIES = "me.marcon.nicelogger.BUNDLED_ENTRIES";

    private static final int ERROR_EXPIRATION = 5 * 60 * 1000;

    private static final String MAP_URL = "http://m.nok.it/?app_id=C2Cse_31xwvbccaAIAaP&token=fjFdGyRjstwqr9iJxLwQ-g&c={LAT},{LON}&nord&t=5&h=768&w=1024&z=16";

    private ListView logText;
    private ImageView logError;
    private ImageView logLocationFound;
    private ImageView logLocationNotFound;
    private ImageView logNetCell;
    private ImageView logNetWifi;
    private Button logClearBtn;
    private ImageView logLocationMap;

    private Location location;

    private Timer timer;
    private Handler handler;

    private long lastError = -1;

    private ArrayAdapter<String> adapter;
    private ArrayList<String> entries;

    private static class Colors {
        static final String VERBOSE = "#333333";
        static final String DEBUG = "#222222";
        static final String INFO = "#111111";
        static final String WARNING = "#ffd700";
        static final String ERROR = "#dc143c";
        static final String WTF = "#0000cd";
    }

    public NiceReporter(ViewGroup parentView) {
        initView(parentView);
    }

    @Override
    public void stop() {
        timer.cancel();
    }

    @Override
    public void onCreate(Bundle bundle) {
        if (bundle != null) {
            entries = bundle.getStringArrayList(BUNDLED_ENTRIES);
            if (entries != null) {
                adapter.addAll(entries);
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle bundle) {
        bundle.putStringArrayList(BUNDLED_ENTRIES, entries);
    }

    private void initView(ViewGroup parentView) {
        this.entries = new ArrayList<String>();
        this.handler = new Handler();
        this.adapter = new ArrayAdapter<String>(parentView.getContext(), R.layout.smart_logger_entry);

        Context parentContext = parentView.getContext();
        LayoutInflater inflater = (LayoutInflater) parentContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View smartReporterView = inflater.inflate(R.layout.smart_logger, null);

        logText = (ListView) smartReporterView.findViewById(R.id.log_text);
        logText.setAdapter(adapter);

        logError = (ImageView) smartReporterView.findViewById(R.id.log_error);
        logLocationFound = (ImageView) smartReporterView.findViewById(R.id.log_location_found);
        logLocationNotFound = (ImageView) smartReporterView.findViewById(R.id.log_location_not_found);
        logNetCell = (ImageView) smartReporterView.findViewById(R.id.log_net_cell);
        logNetWifi = (ImageView) smartReporterView.findViewById(R.id.log_net_wifi);
        logClearBtn = (Button) smartReporterView.findViewById(R.id.log_clear_btn);
        logLocationMap = (ImageView) smartReporterView.findViewById(R.id.log_location_map);

        logClearBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                entries.clear();
                adapter.clear();
                logError.setImageAlpha(SEMI_OPAQUE);
                logLocationMap.setVisibility(View.GONE);
            }
        });

        logLocationFound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (logLocationMap.getVisibility() == View.VISIBLE) {
                    logLocationMap.setVisibility(View.GONE);
                    return;
                }
                new DownloadImageTask(logLocationMap).execute(MAP_URL.replace("{LAT}", String.valueOf(location.getLatitude())).replace("{LON}", String.valueOf(location.getLongitude())));
            }
        });

        logError.setImageAlpha(SEMI_OPAQUE);
        logLocationFound.setVisibility(View.GONE);
        logLocationNotFound.setVisibility(View.VISIBLE);
        logNetCell.setImageAlpha(SEMI_OPAQUE);
        logNetWifi.setImageAlpha(SEMI_OPAQUE);

        parentView.addView(smartReporterView);

        timer = new Timer();
        timer.schedule(new DeviceStateTask(parentContext), 0, UPDATE_INTERVAL);
    }

    private void printMessage(String tag, String message, Throwable t, String color) {
        String text = "[" + tag + "] " + message;
        // text = "<font color='" + color + "'>" + text + "</font>";
        entries.add(text);
        adapter.add(text);
    }

    private void printMessage(String tag, String message, String color) {
        String text = "[" + tag + "] " + message + "\n";
        entries.add(text);
        adapter.add(text);
    }

    @Override
    public void v(String tag, String message, Throwable t) {
        printMessage(tag, message, t, Colors.VERBOSE);
    }

    @Override
    public void v(String tag, String message) {
        printMessage(tag, message, Colors.VERBOSE);
    }

    @Override
    public void d(String tag, String message, Throwable t) {
        printMessage(tag, message, t, Colors.DEBUG);
    }

    @Override
    public void d(String tag, String message) {
        printMessage(tag, message, Colors.DEBUG);
    }

    @Override
    public void i(String tag, String message, Throwable t) {
        printMessage(tag, message, t, Colors.INFO);
    }

    @Override
    public void i(String tag, String message) {
        printMessage(tag, message, Colors.INFO);
    }

    @Override
    public void w(String tag, String message, Throwable t) {
        printMessage(tag, message, t, Colors.WARNING);
    }

    @Override
    public void w(String tag, String message) {
        printMessage(tag, message, Colors.WARNING);
    }

    @Override
    public void e(String tag, String message, Throwable t) {
        logError.setImageAlpha(FULLY_OPAQUE);
        printMessage(tag, message, t, Colors.ERROR);
        lastError = System.currentTimeMillis();
    }

    @Override
    public void e(String tag, String message) {
        logError.setImageAlpha(FULLY_OPAQUE);
        printMessage(tag, message, Colors.ERROR);
        lastError = System.currentTimeMillis();
    }

    @Override
    public void wtf(String tag, String message, Throwable t) {
        printMessage(tag, message, t, Colors.WTF);
    }

    @Override
    public void wtf(String tag, String message) {
        printMessage(tag, message, Colors.WTF);
    }

    @Override
    public void location(String tag, Location location) {
        this.location = location;
        i(tag, "Location (" + location.getLatitude() + ", " + location.getLongitude() + ")");
        logLocationNotFound.setVisibility(View.GONE);
        logLocationFound.setVisibility(View.VISIBLE);

        if (logLocationMap.getVisibility() == View.VISIBLE) {
            new DownloadImageTask(logLocationMap).execute(MAP_URL.replace("{LAT}", String.valueOf(location.getLatitude())).replace("{LON}", String.valueOf(location.getLongitude())));
        }
    }

    private class DeviceStateTask extends TimerTask {
        private Network network;

        public DeviceStateTask(Context context) {
            network = Network.with(context);
        }

        @Override
        public void run() {
            if (network.cellular()) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (logNetCell != null) {
                            logNetCell.setImageAlpha(FULLY_OPAQUE);
                        }
                    }
                });
            } else {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (logNetCell != null) {
                            logNetCell.setImageAlpha(SEMI_OPAQUE);
                        }
                    }
                });
            }
            if (network.wifi()) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (logNetWifi != null) {
                            logNetWifi.setImageAlpha(FULLY_OPAQUE);
                        }
                    }
                });
            } else {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (logNetWifi != null) {
                            logNetWifi.setImageAlpha(SEMI_OPAQUE);
                        }
                    }
                });
            }
            if (System.currentTimeMillis() > lastError + ERROR_EXPIRATION) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        logError.setImageAlpha(SEMI_OPAQUE);
                    }
                });
            }
        }

    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
            bmImage.setVisibility(View.VISIBLE);
        }
    }
}
