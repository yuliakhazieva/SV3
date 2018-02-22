package com.hsehhh.sv3.viewholders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.hsehhh.sv3.R;

/**
 * Created by Tima on 22.02.2018.
 */

public class MessageViewHolder extends RecyclerView.ViewHolder {
    public TextView user_id;
    public TextView message;

    public MessageViewHolder(View v) {
        super(v);

        user_id = v.findViewById(R.id.text_view_user_id);
        message =  v.findViewById(R.id.text_view_message);
    }
}