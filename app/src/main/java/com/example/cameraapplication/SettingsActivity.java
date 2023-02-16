package com.example.cameraapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceFragmentCompat;

public class SettingsActivity extends AppCompatActivity {

    private EditText ipinput;
    private Button savebutton;

    public String ipaddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity_menu);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        ipinput = findViewById(R.id.ip_of_server_input);
        savebutton = findViewById(R.id.save_bt);

        savebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ipaddress = ipinput.getText().toString();

                Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
                intent.putExtra("keyip",ipaddress);

                startActivity(intent);
            }
        });
    }




}