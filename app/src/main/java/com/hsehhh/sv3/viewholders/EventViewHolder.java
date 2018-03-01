package com.hsehhh.sv3.viewholders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.hsehhh.sv3.R;

/**
 * Created by Tima on 22.02.2018.
 */

public class EventViewHolder extends RecyclerView.ViewHolder {
    public TextView title;
    public TextView description;
    public TextView published_by;
    public Button chat;
    public TextView date;
    public ImageButton delete;

    public EventViewHolder(View v) {
        super(v);

        title = v.findViewById(R.id.text_view_title);
        description =  v.findViewById(R.id.text_view_description);
        published_by = v.findViewById(R.id.text_view_published_by);
        delete = v.findViewById(R.id.delete);
        chat = v.findViewById(R.id.button_chat);
        date = v.findViewById(R.id.text_view_date);

    }
}