package com.hsehhh.sv3.viewholders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.hsehhh.sv3.R;

/**
 * Created by Tima on 22.02.2018.
 */

public class EventViewHolder extends RecyclerView.ViewHolder {
    public TextView title;
    public TextView desciption;
    public TextView published_by;

    public EventViewHolder(View v) {
        super(v);

        title = v.findViewById(R.id.text_view_title);
        desciption =  v.findViewById(R.id.text_view_description);
        published_by = v.findViewById(R.id.text_view_user_id);
    }
}