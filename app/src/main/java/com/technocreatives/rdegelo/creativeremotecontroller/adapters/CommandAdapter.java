package com.technocreatives.rdegelo.creativeremotecontroller.adapters;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.technocreatives.rdegelo.creativeremotecontroller.R;
import com.technocreatives.rdegelo.creativeremotecontroller.model.Command;
import com.technocreatives.rdegelo.creativeremotecontroller.model.Sequence;

import org.w3c.dom.Text;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by rdegelo on 13/04/2017.
 */

public class CommandAdapter extends ArrayAdapter<Command> {
    private final Context context;
    private final LinkedList<Command> values;
    private OnCommandRemovedListener listener;

    public CommandAdapter(Context context, LinkedList<Command> values) {
        super(context, -1, values);
        this.context = context;
        this.values = values;
    }

    public void setOnCommandRemovedListener(OnCommandRemovedListener listener) {
        this.listener = listener;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.row_command_edit, parent, false);

        EditText editText = (EditText)rowView.findViewById(R.id.row_command_text);
        EditText editDelay = (EditText)rowView.findViewById(R.id.row_command_delay);
        ImageButton button = (ImageButton)rowView.findViewById(R.id.row_command_button);

        editText.setText(values.get(position).getText());
        editDelay.setText(values.get(position).getDelayBefore() + "");

        editText.addTextChangedListener(new SyncTextWatcher(values.get(position)));
        editDelay.addTextChangedListener(new SyncDelayWatcher(values.get(position)));

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener != null)
                    listener.commandRemoved(values.get(position));
            }
        });

        return rowView;
    }

    public interface OnCommandRemovedListener {
        void commandRemoved(Command s);
    }

    private class SyncTextWatcher implements TextWatcher {
        Command cmd;

        public SyncTextWatcher(Command cmd) {
            this.cmd = cmd;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            cmd.setText(s.toString());
        }

        @Override
        public void afterTextChanged(Editable s) {
            cmd.setText(s.toString());
        }
    }

    private class SyncDelayWatcher implements TextWatcher {
        Command cmd;

        public SyncDelayWatcher(Command cmd) {
            this.cmd = cmd;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            try {
                if(s.length() > 0)
                    cmd.setDelayBefore(Integer.parseInt(s.toString()));
                else
                    cmd.setDelayBefore(0);
            } catch (NumberFormatException ex) {
                Toast.makeText(context, "Delay value is invalid", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
            try {
                if (s.length() > 0)
                    cmd.setDelayBefore(Integer.parseInt(s.toString()));
                else
                    cmd.setDelayBefore(0);
            } catch (NumberFormatException ex) {
                Toast.makeText(context, "Delay value is invalid", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private static class ViewHolder {
        EditText editText;
        EditText editDelay;
        ImageButton button;

        SyncTextWatcher textWatcher;
        SyncDelayWatcher delayWatcher;
    }
}