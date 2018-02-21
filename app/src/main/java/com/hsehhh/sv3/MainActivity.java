package com.hsehhh.sv3;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.hsehhh.sv3.data.Event;
import com.hsehhh.sv3.data.User;

public class MainActivity extends AppCompatActivity implements FragmentSwitcher{
    final static int RC_SIGN_IN = 1;

    //UI objects
    Toolbar mainToolbar;
    FrameLayout mainFrame;

    //Fragments
    ScrollingFragment scrollingFragment;
    CreateEventFragment createEventFragment;
    MyEventsFragment myEventsFragment;
    EventDetailFragment eventDetailFragment;

    private Fragment lastViewedFragment;

    FragmentTransaction fragmentTransaction;

    //Firebase objects
    private FirebaseAuth.AuthStateListener firebaseAuthStateListener;
    private FirebaseAuth firebaseAuth;
    public FirebaseDatabase firebaseDatabase;

    // User object
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Init toolbar
        mainToolbar = findViewById(R.id.toolbar_main);
        setSupportActionBar(mainToolbar);

        mainFrame = findViewById(R.id.frame_main);

        //Create all fragments
        if (savedInstanceState == null)
            initFragments();

        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.frame_main, scrollingFragment, "scroll");
        fragmentTransaction.commit();

        lastViewedFragment = getSupportFragmentManager().findFragmentById(R.id.frame_main);

        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        firebaseAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    onSignedInInitialize(user);
                } else {
//                    startActivityForResult(
//                            AuthUI.getInstance()
//                                    .createSignInIntentBuilder()
//                                    .setIsSmartLockEnabled(false)
//                                    .build(),
//                            RC_SIGN_IN);
                }
            }
        };
    }

    private void onSignedInInitialize(FirebaseUser user) {
//        user.getProviderData().
        firebaseDatabase.getReference("users").child(user.getUid());
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
        eventDetailFragment = new EventDetailFragment();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case RC_SIGN_IN:{
                if (resultCode == RESULT_OK){
                    Toast.makeText(this, "Signed in!", Toast.LENGTH_SHORT).show();
                } else if (resultCode == RESULT_CANCELED){
                    Toast.makeText(this, "Cancelled", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
            default:{
                super.onActivityResult(requestCode, resultCode, data);
            }
        }
    }

    // TODO: Посмотреть, есть ли более гуманный способ переключения на предыдущий фрагмент.
    public void switchToPrevious() {
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame_main, lastViewedFragment);
        fragmentTransaction.commit();
    }

    @Override
    public void switchToMyEvents() {
        lastViewedFragment = getSupportFragmentManager().findFragmentById(R.id.frame_main);

        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_up, R.anim.slide_down);
        fragmentTransaction.replace(R.id.frame_main, myEventsFragment);
        fragmentTransaction.commit();
    }

    @Override
    public void switchToScrolling() {
        lastViewedFragment = getSupportFragmentManager().findFragmentById(R.id.frame_main);

        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame_main, scrollingFragment);
        fragmentTransaction.commit();

    }

    @Override
    public void switchToCreateEvent() {
        lastViewedFragment = getSupportFragmentManager().findFragmentById(R.id.frame_main);

        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
        fragmentTransaction.replace(R.id.frame_main, createEventFragment);
        fragmentTransaction.commit();
    }

    @Override
    public void switchToEventDetails(Event e) {
        lastViewedFragment = getSupportFragmentManager().findFragmentById(R.id.frame_main);

        Bundle eventArgs = new Bundle();
        eventArgs.putParcelable("event", e);
        eventDetailFragment.setArguments(eventArgs);

        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame_main, eventDetailFragment);
        fragmentTransaction.commit();
    }

}
