package com.technocreatives.rdegelo.creativeremotecontroller;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.technocreatives.rdegelo.creativeremotecontroller.adapters.CommandAdapter;
import com.technocreatives.rdegelo.creativeremotecontroller.model.Command;
import com.technocreatives.rdegelo.creativeremotecontroller.model.Sequence;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class EditSequenceActivity extends AppCompatActivity {
    private ListView list_view;
    private Button add_button;
    private LinkedList<Command> commands;

    private CommandAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_sequence);

        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        Type listType = new TypeToken<LinkedList<Command>>() {}.getType();
        commands = new Gson().fromJson(getIntent().getStringExtra("commands"), listType);

        setTitle(getIntent().getStringExtra("title"));

        list_view = (ListView)findViewById(R.id.command_list_view);
        add_button = (Button)findViewById(R.id.command_add);
        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commands.add(new Command());
                adapter.notifyDataSetChanged();
            }
        });
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
    protected void onResume() {
        adapter = new CommandAdapter(this, commands);
        adapter.setOnCommandRemovedListener(new CommandAdapter.OnCommandRemovedListener() {
            @Override
            public void commandRemoved(Command s) {
                commands.remove(s);
                adapter.notifyDataSetChanged();
            }
        });

        list_view.setAdapter(adapter);

        super.onResume();
    }

    @Override
    public void onBackPressed() {
        Intent returnIntent = new Intent();
        returnIntent.putExtra("commands" , new Gson().toJson(commands));
        setResult(Activity.RESULT_OK , returnIntent);
        finish();
    }
}
