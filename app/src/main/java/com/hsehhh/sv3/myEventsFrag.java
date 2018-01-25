package com.hsehhh.sv3;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.widget.ImageButton;
import android.widget.ImageView;

/**
 * Created by a1 on 21.01.18.
 */

public class myEventsFrag extends android.support.v4.app.Fragment //implements View.OnClickListener
{
    public static ImageView close;
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.my_events, container, false);
        return v;
    }

    @Override
    public void onViewCreated (final View view, final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        close = getView().findViewById(R.id.closeMyEvents);
        close.setClickable(true);
    }

    public static void setCloseVisibility(int newVal)
    {
        close.setVisibility(newVal);
    }

    public static void setCloseClickability(boolean newVal)
    {
        close.setClickable(newVal);

    }

    public static int getCloseVisibility()
    {
        return close.getVisibility();
    }

    public static int getName()
    {
        return close.getId();
    }

}