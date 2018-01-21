package com.hsehhh.sv3;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by a1 on 21.01.18.
 */

public class myEventsFrag extends android.support.v4.app.Fragment implements View.OnClickListener
{
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

      //  setHasOptionsMenu(true);
        return inflater.inflate(R.layout.my_events, container, false);
    }

    @Override
    public void onClick(View v)
    {
        FragmentManager fragmentManager = getFragmentManager();
        android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_up, R.anim.slide_down);
        fragmentTransaction.show(this);
        fragmentTransaction.commit();
    }

}
