package com.hsehhh.sv3;

import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity implements SwitchToMyEvents, SwitchToScrolling {

    //UI objects
    Toolbar mainToolbar;
    FrameLayout mainFrame;
    Button showEventsButton;

    //Fragments
    public ScrollingFragment scrollingFragment;
    CreateEventFragment createEventFragment;
    MyEventsFragment myEventsFragment;
    FragmentTransaction fragmentTransaction;

    //Firebase objects
    private FirebaseAuth.AuthStateListener firebaseAuthStateListener;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Init toolbar
        mainToolbar = findViewById(R.id.toolbar_main);
        setSupportActionBar(mainToolbar);

        //Create all fragments
        if (savedInstanceState == null)
            initFragments();

        //Place scrolling fragment into the view
        mainFrame = findViewById(R.id.frame_main);
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.frame_main, scrollingFragment);
        fragmentTransaction.commit();

        showEventsButton = findViewById(R.id.button_show_events);
        showEventsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchToMyEvents();
            }
        });


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
    }

    @Override
    protected void onResume() {
        super.onResume();
        firebaseAuth.addAuthStateListener(firebaseAuthStateListener);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (firebaseAuthStateListener != null) {
            firebaseAuth.removeAuthStateListener(firebaseAuthStateListener);
        }
    }

    // Utility methods
    void initFragments(){
        scrollingFragment = new ScrollingFragment();
        myEventsFragment = new MyEventsFragment();
        createEventFragment = new CreateEventFragment();
    }

    @Override
    public void switchToMyEvents() {
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame_main, myEventsFragment);
        fragmentTransaction.commit();
    }

    @Override
    public void switchToScrolling() {
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame_main, scrollingFragment);
        fragmentTransaction.commit();
    }
}
