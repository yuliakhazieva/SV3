package com.hsehhh.sv3;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hsehhh.sv3.data.Event;
import com.hsehhh.sv3.data.User;
import com.hsehhh.sv3.fragments.CreateEventFragment;
import com.hsehhh.sv3.fragments.EventDetailFragment;
import com.hsehhh.sv3.fragments.MyEventsFragment;
import com.hsehhh.sv3.fragments.NewEventDetail;
import com.hsehhh.sv3.fragments.ProfileFragment;
import com.hsehhh.sv3.fragments.ScrollingFragment;
import com.hsehhh.sv3.interfaces.FragmentSwitcher;

public class MainActivity extends AppCompatActivity implements FragmentSwitcher {

    //constants
    private static final int RC_SIGN_IN = 1;

    //UI objects
    Toolbar mainToolbar;
    FrameLayout mainFrame;
    FrameLayout detailFrame;

    //Fragments
    ScrollingFragment scrollingFragment;
    CreateEventFragment createEventFragment;
    MyEventsFragment myEventsFragment;
    EventDetailFragment eventDetailFragment;
    ProfileFragment profileFragment;
    NewEventDetail newEventDetail;


    private Fragment lastViewedFragment;

    FragmentTransaction fragmentTransaction;

    //Firebase objects
    private FirebaseAuth.AuthStateListener firebaseAuthStateListener;
    private FirebaseAuth firebaseAuth;
    public FirebaseDatabase firebaseDatabase;
    public DatabaseReference userReference;

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
        detailFrame = findViewById(R.id.frame_det);

        //Create all fragments
//        if (savedInstanceState == null)
        initFragments(); // надо починить чтобы после не было реинициализации просто так

        mainFrame = findViewById(R.id.frame_main);
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.frame_main, scrollingFragment, "scroll");
        fragmentTransaction.commit();

        lastViewedFragment = getSupportFragmentManager().findFragmentById(R.id.frame_main);

        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

                if (firebaseUser == null || firebaseUser.isAnonymous()) {
                    startActivityForResult(
                            AuthUI.getInstance()
                                    .createSignInIntentBuilder()
                                    .setIsSmartLockEnabled(false)
                                    .setLogo(R.drawable.trilist)
                                    .build(),
                            RC_SIGN_IN);
                } else {
                    onSignedInInitialize(firebaseUser);
                }
            }
        };
    }

    private void onSignedInInitialize(FirebaseUser firebaseUser) {
        userReference = firebaseDatabase.getReference("users").child(firebaseUser.getUid());
        userReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot == null || dataSnapshot.getValue() == null) {
                    // user is new
                    createUser();
                } else {
                    // user already existed
                    user = dataSnapshot.getValue(User.class);
                }
            }

            private void createUser() {
                user = new User("fuck", "myass");
                userReference.setValue(user);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // TODO: Посмотреть, что такое OnCancelled
                Toast.makeText(MainActivity.this, "Cancelled", Toast.LENGTH_SHORT).show();
            }
        });
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case RC_SIGN_IN:{
                if (resultCode == RESULT_OK){
                    Toast.makeText(this, "Signed in!", Toast.LENGTH_SHORT).show();
                } else if (resultCode == RESULT_CANCELED){
                    Toast.makeText(this, "Cancelled", Toast.LENGTH_SHORT).show();
                }
            }
            default:{
                super.onActivityResult(requestCode, resultCode, data);
            }
        }
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
        profileFragment = new ProfileFragment();
        newEventDetail = new NewEventDetail();
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

    @Override
    public void switchToProfile() {
        lastViewedFragment = getSupportFragmentManager().findFragmentById(R.id.frame_main);

        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
        fragmentTransaction.replace(R.id.frame_main, profileFragment);
        fragmentTransaction.commit();
    }

    public void addDetail(Event e)
    {
        Fragment fragmentA = getSupportFragmentManager().findFragmentByTag("detail");
        if (fragmentA == null) {
            fragmentTransaction = getSupportFragmentManager().beginTransaction();

            lastViewedFragment = getSupportFragmentManager().findFragmentById(R.id.frame_main);

            Bundle eventArgs = new Bundle();
            eventArgs.putParcelable("event", e);
            newEventDetail.setArguments(eventArgs);

            fragmentTransaction.add(R.id.frame_det, newEventDetail, "detail");
            fragmentTransaction.commit();
        }
    }

    @Override
    public void removeDetail()
    {
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.remove(newEventDetail);
        fragmentTransaction.commit();
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sign_out:{
                firebaseAuth.signOut();
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
