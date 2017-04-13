package com.technocreatives.rdegelo.creativeremotecontroller;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ListViewCompat;
import android.support.v7.widget.SwitchCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.technocreatives.rdegelo.creativeremotecontroller.model.Settings;

public class ManageActivity extends AppCompatActivity {
    private RelativeLayout ip_layout;
    private RelativeLayout port_layout;
    private SwitchCompat enable_broadcast_switch;
    private EditText ip_edit;
    private EditText port_edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage);

        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        ip_layout = (RelativeLayout)findViewById(R.id.manage_ip_layout);
        port_layout = (RelativeLayout)findViewById(R.id.manage_port_layout);
        enable_broadcast_switch = (SwitchCompat)findViewById(R.id.manage_enable_broadcast);
        ip_edit = (EditText)findViewById(R.id.manage_ip_edit);
        port_edit = (EditText)findViewById(R.id.manage_port_edit);

        enable_broadcast_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    ip_layout.setVisibility(View.INVISIBLE);
                    port_layout.setVisibility(View.INVISIBLE);
                } else {
                    ip_layout.setVisibility(View.VISIBLE);
                    port_layout.setVisibility(View.VISIBLE);
                }
            }
        });

        load();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return false;
    }

    @Override
    protected void onPause() {
        save();

        super.onPause();
    }

    private void load() {
        Settings settings = new Settings();
        settings.Load(this);

        enable_broadcast_switch.setChecked(settings.isUse_broadcast());
        ip_edit.setText(settings.getIp());
        port_edit.setText(settings.getPort() + "");
    }

    private void save() {
        Settings settings = new Settings();
        settings.Load(this);

        settings.setUse_broadcast(enable_broadcast_switch.isChecked());
        settings.setIp(ip_edit.getText().toString());

        if (port_edit.getText().toString().length() > 0)
            settings.setPort(Integer.parseInt(port_edit.getText().toString()));

        settings.Save(this);
    }
}
