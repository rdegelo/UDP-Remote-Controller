package com.technocreatives.rdegelo.creativeremotecontroller;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.technocreatives.rdegelo.creativeremotecontroller.adapters.SequenceAdapter;
import com.technocreatives.rdegelo.creativeremotecontroller.model.Command;
import com.technocreatives.rdegelo.creativeremotecontroller.model.Sequence;
import com.technocreatives.rdegelo.creativeremotecontroller.model.Settings;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ManageActivity extends AppCompatActivity {
    private RelativeLayout ip_layout;
    private RelativeLayout port_layout;
    private SwitchCompat enable_broadcast_switch;
    private EditText ip_edit;
    private EditText port_edit;
    private ListView list_view;

    private List<Sequence> sequencies;

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
        list_view = (ListView)findViewById(R.id.manage_listview);

        enable_broadcast_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    ip_layout.setVisibility(View.GONE);
                } else {
                    ip_layout.setVisibility(View.VISIBLE);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(resultCode == Activity.RESULT_OK){
            Type listType = new TypeToken<LinkedList<Command>>() {}.getType();
            LinkedList<Command> commands = (LinkedList<Command>)new Gson().fromJson(data.getStringExtra("commands"), listType);

            sequencies.get(requestCode).setCommands(commands);
        }
    }

    private void load() {
        final Settings settings = new Settings();
        settings.Load(this);

        enable_broadcast_switch.setChecked(settings.isUse_broadcast());
        ip_edit.setText(settings.getIp());
        port_edit.setText(settings.getPort() + "");

        sequencies = settings.getSequencies();

        SequenceAdapter adapter = new SequenceAdapter(this, sequencies, R.layout.row_sequence_edit);
        adapter.setOnSequenceExecutedListener(new SequenceAdapter.OnSequenceExecutedListener() {
            @Override
            public void sequenceExecuted(Sequence s) {
                Intent i = new Intent(ManageActivity.this, EditSequenceActivity.class);
                i.putExtra("commands", new Gson().toJson(s.getCommands()));
                i.putExtra("title", s.getTitle());
                startActivityForResult(i, settings.getSequencies().indexOf(s));
            }
        });

        list_view.setAdapter(adapter);
    }

    private void save() {
        Settings settings = new Settings();
        settings.Load(this);

        settings.setUse_broadcast(enable_broadcast_switch.isChecked());
        settings.setIp(ip_edit.getText().toString());

        if (port_edit.getText().toString().length() > 0)
            settings.setPort(Integer.parseInt(port_edit.getText().toString()));

        settings.setSequencies(sequencies);

        settings.Save(this);
    }
}
