package com.example.cameraapplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
public class SettingsActivity extends AppCompatActivity {
    private EditText ipEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity_menu);

        // Get a reference to the EditText view
        ipEditText = findViewById(R.id.ip_of_server_input);

        // Get the server IP address from SharedPreferences
        SharedPreferences sharedPref = getSharedPreferences("my_settings", Context.MODE_PRIVATE);
        String serverIP = sharedPref.getString("server_ip", null);

        // If the server IP is present in SharedPreferences, display it in the EditText
        if (serverIP != null) {
            ipEditText.setText(serverIP);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        // Save the server IP address to SharedPreferences
        String serverIP = ipEditText.getText().toString();
        SharedPreferences sharedPref = getSharedPreferences("my_settings", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("server_ip", serverIP);
        editor.apply();
    }
}