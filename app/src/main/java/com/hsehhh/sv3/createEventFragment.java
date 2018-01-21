package com.hsehhh.sv3; /**
 * Created by a1 on 18.01.18.
 */
import android.app.ActionBar;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
//import android.widget.Toolbar;
import android.support.v7.widget.Toolbar;

public class createEventFragment extends Fragment
{

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        Toolbar toolbar = getActivity().findViewById(R.id.main_tool_bar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.activity_create_event, container, false);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        FragmentTransaction fragmentTransaction =getFragmentManager().beginTransaction();
        fragmentTransaction.replace(this.getId(), new scrollingFragment());
        fragmentTransaction.remove(this);
        fragmentTransaction.commit();
        return super.onOptionsItemSelected(item);
    }
}
