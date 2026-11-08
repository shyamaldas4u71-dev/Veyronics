
package com.deviceguard.app;

import android.app.Activity;
import android.os.Bundle;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends Activity {

    private LinearLayout mainLayout;

    private final int backgroundColor =
            Color.rgb(10, 17, 32);

    private final int cardColor =
            Color.rgb(20, 32, 52);

    private final int accentColor =
            Color.rgb(0, 190, 255);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createDashboard();
    }

    // Veyronics v0.02 development build

    private void createDashboard() {

        mainLayout = new LinearLayout(this);
        mainLayout.setOrientation(LinearLayout.VERTICAL);
        mainLayout.setPadding(24, 32, 24, 24);
        mainLayout.setBackgroundColor(backgroundColor);

        TextView title = new TextView(this);
        title.setText("VEYRONICS");
        title.setTextSize(27);
        title.setTextColor(accentColor);
        title.setTypeface(null, Typeface.BOLD);
        mainLayout.addView(title);

        TextView subtitle = new TextView(this);
        subtitle.setText("Device Health & Security");
        subtitle.setTextSize(14);
        subtitle.setTextColor(Color.LTGRAY);
        mainLayout.addView(subtitle);

        addSpace(30);

        addCard("DEVICE STATUS", "Monitoring ready");
        addSpace(16);

        addCard("PERFORMANCE", "Optimisation system ready");
        addSpace(16);

        addCard("SECURITY", "Protection dashboard ready");

        setContentView(mainLayout);
    }

    private void addCard(String heading, String description) {

        LinearLayout card = new LinearLayout(this);
        card.setOrientation(LinearLayout.VERTICAL);
        card.setPadding(20, 20, 20, 20);
        card.setBackgroundColor(cardColor);

        TextView title = new TextView(this);
        title.setText(heading);
        title.setTextSize(14);
        title.setTextColor(accentColor);
        title.setTypeface(null, Typeface.BOLD);

        TextView detail = new TextView(this);
        detail.setText(description);
        detail.setTextSize(16);
        detail.setTextColor(Color.WHITE);

        card.addView(title);
        card.addView(detail);

        mainLayout.addView(card,
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT));
    }

    private void addSpace(int height) {
        TextView space = new TextView(this);
        space.setHeight(height);
        mainLayout.addView(space);
    }
}
