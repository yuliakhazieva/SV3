package com.hsehhh.sv3;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentContainer;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    //UI objects
    Toolbar mainToolbar;

    // Firebase objects
    private FirebaseAuth.AuthStateListener firebaseAuthStateListener;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseApp.initializeApp(this);

        mainToolbar = findViewById(R.id.main_tool_bar);
        setSupportActionBar(mainToolbar);

        FrameLayout frame = findViewById(R.id.frame);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        scrollingFragment fragment = new scrollingFragment();
        fragmentTransaction.add(R.id.frame, fragment);
        fragmentTransaction.commit();

        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        firebaseAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                  //  startActivity(new Intent(MainActivity.this, LoginActivity.class));
                  //  finish();
                } else {
                    // if user exists
                    // do something
                }
            }
        };

        if (savedInstanceState != null) {
            return;
        }

        // Create a new Fragment to be placed in the activity layout
      //  scrollingFragment firstFragment = new scrollingFragment();

        // In case this activity was started with special instructions from an
        // Intent, pass the Intent's extras to the fragment as arguments

//        if (savedInstanceState == null) {
//            scrollingFragment newFragment = new scrollingFragment();
//            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//            ft.add(frame.getId(), newFragment).commit();
//        }



      //  ImageButton addEvent = findViewById(R.id.addEvent);
//        addEvent.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view)
//            {
//                // Begin the transaction
//                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//             //   ft.replace(, createEventFrag);
//                ft.commit();
//            }
//        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        firebaseAuth.addAuthStateListener(firebaseAuthStateListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (firebaseAuthStateListener != null) {
            firebaseAuth.removeAuthStateListener(firebaseAuthStateListener);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_scroll_frag, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings: {
                return true;
            }
            case R.id.action_add:{

                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }


//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_scroll_frag, menu);
//        return true;
//    }
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_scroll_frag, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.action_settings: {
//                startActivity(new Intent(this, SettingsActivity.class));
//                finish();
//                return true;
//            }
//            case R.id.action_add:{
//                Toast.makeText(this, "Add new event", Toast.LENGTH_SHORT).show();
//                return true;
//            }
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }

}
