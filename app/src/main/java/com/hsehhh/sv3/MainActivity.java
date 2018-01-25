package com.hsehhh.sv3;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.io.Console;

public class MainActivity extends AppCompatActivity {

    //UI objects
    Toolbar mainToolbar;

    // Firebase objects
    private FirebaseAuth.AuthStateListener firebaseAuthStateListener;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;

    public scrollingFragment fragment;
    public createEventFragment eventFragment;
    public myEventsFrag myEventsFragment;
    public ImageButton closeMyEvents;

    public FrameLayout frame2;

    FragmentManager fragmentManager = getSupportFragmentManager();
    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseApp.initializeApp(this);

        mainToolbar = findViewById(R.id.main_tool_bar);
        setSupportActionBar(mainToolbar);

        final FrameLayout frame = findViewById(R.id.frame);
        frame2 = findViewById(R.id.frame2);

        fragment = new scrollingFragment();
        myEventsFragment = new myEventsFrag();
        eventFragment = new createEventFragment();

        fragmentTransaction.add(R.id.frame, fragment);

        fragmentTransaction.add(R.id.frame2, myEventsFragment, "myEvents");
        fragmentTransaction.commit();
        findViewById(myEventsFragment.getId()).setY(1900);


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

        frame2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(myEventsFrag.getCloseVisibility() != View.VISIBLE)
                {
                    frame2.animate().translationY(1).start();
                    myEventsFrag.setCloseClickability(true);
                    myEventsFrag.setCloseVisibility(View.VISIBLE);
                }
            }
        });
    }


    @Override
    protected void onStart()
    {
        super.onStart();
        myEventsFragment.setCloseVisibility(View.INVISIBLE);
       // myEventsFragment.setClickability(false);
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

}
