
package com.veyronics.app;

import android.app.Activity;
import android.app.ActivityManager;
import android.os.Bundle;
import android.os.StatFs;
import android.net.TrafficStats;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.FileReader;

public class MainActivity extends Activity {

    private final int BG = Color.rgb(10, 17, 32);
    private final int CARD = Color.rgb(20, 32, 52);
    private final int BLUE = Color.rgb(0, 190, 255);
    private final int WHITE = Color.WHITE;
    private final int GREY = Color.LTGRAY;

    private LinearLayout root;
    private long previousRx = 0;
    private long previousTx = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showHome();
    }

    private void setup() {
        root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(24, 32, 24, 24);
        root.setBackgroundColor(BG);

        ScrollView scroll = new ScrollView(this);
        scroll.setFillViewport(true);
        scroll.addView(root);

        setContentView(scroll);
    }

    private TextView text(String value, int size, int color) {
        TextView t = new TextView(this);
        t.setText(value);
        t.setTextSize(size);
        t.setTextColor(color);
        return t;
    }

    private void heading(String value) {
        TextView t = text(value, 27, BLUE);
        t.setTypeface(null, Typeface.BOLD);
        root.addView(t);
        space(8);
    }

    private void subtitle(String value) {
        root.addView(text(value, 14, GREY));
    }

    private void space(int height) {
        View v = new View(this);
        root.addView(v, new LinearLayout.LayoutParams(
                1, height));
    }

    private void card(String title, String description,
                      Runnable action) {

        LinearLayout box = new LinearLayout(this);
        box.setOrientation(LinearLayout.VERTICAL);
        box.setPadding(20, 20, 20, 20);
        box.setBackgroundColor(CARD);
        box.setClickable(true);
        box.setFocusable(true);

        TextView h = text(title, 17, BLUE);
        h.setTypeface(null, Typeface.BOLD);

        TextView d = text(description, 16, WHITE);

        box.addView(h);
        spaceInside(box, 8);
        box.addView(d);

        box.setOnClickListener(v -> action.run());

        root.addView(box, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));

        space(16);
    }

    private void spaceInside(LinearLayout box, int height) {
        View v = new View(this);
        box.addView(v, new LinearLayout.LayoutParams(1, height));
    }

    private void info(String title, String value) {
        LinearLayout box = new LinearLayout(this);
        box.setOrientation(LinearLayout.VERTICAL);
        box.setPadding(18, 16, 18, 16);
        box.setBackgroundColor(CARD);

        TextView h = text(title, 13, BLUE);
        h.setTypeface(null, Typeface.BOLD);

        TextView d = text(value, 17, WHITE);

        box.addView(h);
        spaceInside(box, 6);
        box.addView(d);

        root.addView(box, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));

        space(12);
    }

    private void backButton() {
        card("← BACK TO DASHBOARD",
                "Return to the Veyronics home screen",
                () -> showHome());
    }

    private void showHome() {
        setup();

        heading("VEYRONICS");
        subtitle("Device Health & Security");
        space(30);

        card("DEVICE STATUS",
                "View your device health and storage",
                () -> showDevice());

        card("PERFORMANCE",
                "Monitor RAM, CPU and network statistics",
                () -> showPerformance());

        card("SECURITY",
                "Explore device security information",
                () -> showSecurity());

        subtitle("VEYRONICS v0.02 DEVELOPMENT");
    }

    private String memoryInfo() {
        ActivityManager am =
                (ActivityManager) getSystemService(ACTIVITY_SERVICE);

        ActivityManager.MemoryInfo mi =
                new ActivityManager.MemoryInfo();

        am.getMemoryInfo(mi);

        long total = mi.totalMem / (1024 * 1024);
        long available = mi.availMem / (1024 * 1024);
        long used = total - available;

        return "Used RAM: " + used + " MB\n"
                + "Available RAM: " + available + " MB\n"
                + "Total RAM: " + total + " MB";
    }

    private String storageInfo() {
        StatFs stat = new StatFs(
                android.os.Environment.getDataDirectory().getPath());

        long total = stat.getTotalBytes() /
                (1024 * 1024 * 1024);

        long free = stat.getAvailableBytes() /
                (1024 * 1024 * 1024);

        long used = total - free;

        return "Used storage: " + used + " GB\n"
                + "Free storage: " + free + " GB\n"
                + "Total storage: " + total + " GB";
    }

    private void showDevice() {
        setup();

        heading("DEVICE STATUS");
        subtitle("Device health overview");
        space(25);

        info("DEVICE MODEL",
                android.os.Build.MANUFACTURER + " "
                + android.os.Build.MODEL);

        info("ANDROID VERSION",
                android.os.Build.VERSION.RELEASE);

        info("RAM INFORMATION", memoryInfo());

        info("STORAGE INFORMATION", storageInfo());

        card("REFRESH STATISTICS",
                "Update device information",
                () -> showDevice());

        backButton();
    }

    private void showPerformance() {
        setup();

        heading("PERFORMANCE");
        subtitle("System resource monitoring");
        space(25);

        info("RAM USAGE", memoryInfo());

        info("NETWORK RECEIVED",
                formatBytes(TrafficStats.getTotalRxBytes()));

        info("NETWORK SENT",
                formatBytes(TrafficStats.getTotalTxBytes()));

        info("CPU INFORMATION",
                readCpuInfo());

        card("REFRESH STATISTICS",
                "Update performance information",
                () -> showPerformance());

        backButton();
    }

    private String readCpuInfo() {
        try {
            BufferedReader reader =
                    new BufferedReader(
                            new FileReader("/proc/cpuinfo"));

            String line;
            while ((line = reader.readLine()) != null) {
                if (line.toLowerCase().contains("hardware")
                        || line.toLowerCase().contains("model name")) {
                    reader.close();
                    return line;
                }
            }

            reader.close();
            return "CPU information unavailable";

        } catch (Exception e) {
            return "CPU information unavailable";
        }
    }

    private String formatBytes(long bytes) {
        if (bytes < 0) return "Unavailable";

        return String.format("%.2f MB",
                bytes / (1024.0 * 1024.0));
    }

    private void showSecurity() {
        setup();

        heading("SECURITY");
        subtitle("Device protection information");
        space(25);

        info("SECURITY STATUS",
                "Basic security overview available");

        info("APPLICATION ID",
                getPackageName());

        info("ANDROID SECURITY",
                "Android " + android.os.Build.VERSION.RELEASE);

        info("FILE INSPECTION",
                "Advanced file inspection engine is not yet implemented.");

        info("AUTOMATIC CLEANUP",
                "Not available in this development build.");

        backButton();
    }

    @Override
    public void onBackPressed() {
        showHome();
    }
}
