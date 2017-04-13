package com.technocreatives.rdegelo.creativeremotecontroller;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

import com.technocreatives.rdegelo.creativeremotecontroller.adapters.SequenceAdapter;
import com.technocreatives.rdegelo.creativeremotecontroller.executor.SequenceExecutor;
import com.technocreatives.rdegelo.creativeremotecontroller.model.Sequence;
import com.technocreatives.rdegelo.creativeremotecontroller.model.Settings;

public class MainActivity extends AppCompatActivity {

    ListView list_view;
    TextView text_log;
    SequenceExecutor currentExecutor;
    ProgressDialog currentProgressDialog;

    public final Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if(msg.getData().getString("stop") == null)
                text_log.setText(text_log.getText() + "\n" + msg.getData().getString("text"));
            else if(currentProgressDialog != null) {
                currentProgressDialog.dismiss();
            }

            final int scrollAmount = text_log.getLayout().getLineTop(text_log.getLineCount()) - text_log.getHeight();
            // if there is no need to scroll, scrollAmount will be <=0
            if (scrollAmount > 0)
                text_log.scrollTo(0, scrollAmount);
            else
                text_log.scrollTo(0, 0);
        };
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        list_view = (ListView)findViewById(R.id.main_listview);
        text_log = (TextView)findViewById(R.id.main_log);
        text_log.setMovementMethod(new ScrollingMovementMethod());
    }

    @Override
    protected void onResume() {
        final Settings settings = new Settings();
        settings.Load(this);

        SequenceAdapter adapter = new SequenceAdapter(this, settings.getSequencies());

        list_view.setAdapter(adapter);

        adapter.setOnSequenceExecutedListener(new SequenceAdapter.OnSequenceExecutedListener() {
            @Override
            public void sequenceExecuted(Sequence s) {
                text_log.setText("");

                currentProgressDialog = new ProgressDialog(MainActivity.this);
                currentProgressDialog.setMessage("Executing...");
                currentProgressDialog.setCancelable(false);
                currentProgressDialog.show();

                currentExecutor = new SequenceExecutor(settings, s, handler);
                currentExecutor.startExecutionAsync();
            }
        });

        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_activity_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_manage:
                Intent intent = new Intent(this, ManageActivity.class);
                startActivity(intent);
                break;
        }

        return  false;
    }
}
