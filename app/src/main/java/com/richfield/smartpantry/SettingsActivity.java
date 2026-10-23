package com.richfield.smartpantry;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {

    private static final String PREFS = "smart_pantry_prefs";
    private static final String KEY_ALERTS = "expiring_alerts";
    private static final String KEY_METRIC = "prefer_metric";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        setTitle(R.string.settings);

        Switch swAlerts = findViewById(R.id.swAlerts);
        Switch swMetric = findViewById(R.id.swMetric);

        SharedPreferences prefs = getSharedPreferences(PREFS, MODE_PRIVATE);
        swAlerts.setChecked(prefs.getBoolean(KEY_ALERTS, true));
        swMetric.setChecked(prefs.getBoolean(KEY_METRIC, true));

        swAlerts.setOnCheckedChangeListener((buttonView, isChecked) ->
                prefs.edit().putBoolean(KEY_ALERTS, isChecked).apply());

        swMetric.setOnCheckedChangeListener((buttonView, isChecked) ->
                prefs.edit().putBoolean(KEY_METRIC, isChecked).apply());
    }
}
