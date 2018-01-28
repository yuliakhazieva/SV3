package com.hsehhh.sv3;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

/**
 * Created by a1 on 20.01.18.
 */

public class ScrollingFragment extends android.support.v4.app.Fragment
{
    Toolbar mainToolbar;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.content_scrolling, container, false);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
//        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_scroll_frag, menu);
//        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings: {
                Toast.makeText(getContext(), "Settings", Toast.LENGTH_SHORT).show();
                return true;
            }
            case R.id.action_add:{
                FragmentTransaction fragmentTransaction =getFragmentManager().beginTransaction();
                fragmentTransaction.replace(this.getId(), new CreateEventFragment());
                fragmentTransaction.remove(this);
                fragmentTransaction.hide(getFragmentManager().findFragmentByTag("myEvents"));
                fragmentTransaction.commit();
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
