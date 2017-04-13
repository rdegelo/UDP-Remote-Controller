package com.technocreatives.rdegelo.creativeremotecontroller.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.technocreatives.rdegelo.creativeremotecontroller.R;
import com.technocreatives.rdegelo.creativeremotecontroller.model.Sequence;

import java.util.List;

/**
 * Created by rdegelo on 13/04/2017.
 */

public class SequenceAdapter extends ArrayAdapter<Sequence> {
    private final Context context;
    private final List<Sequence> values;
    private OnSequenceExecutedListener listener;
    private int resource_id;

    public SequenceAdapter(Context context, List<Sequence> values, int resource_id) {
        super(context, -1, values);
        this.context = context;
        this.values = values;
        this.resource_id = resource_id;
    }

    public void setOnSequenceExecutedListener(OnSequenceExecutedListener listener) {
        this.listener = listener;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(resource_id, parent, false);

        TextView textView = (TextView)rowView.findViewById(R.id.row_sequence_title);
        ImageButton button = (ImageButton)rowView.findViewById(R.id.row_sequence_button);

        textView.setText(values.get(position).getTitle());
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener != null)
                    listener.sequenceExecuted(values.get(position));
            }
        });

        return rowView;
    }

    public interface OnSequenceExecutedListener {
        void sequenceExecuted(Sequence s);
    }
}
