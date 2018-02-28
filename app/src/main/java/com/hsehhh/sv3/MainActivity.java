package com.hsehhh.sv3;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.hsehhh.sv3.data.Event;
import com.hsehhh.sv3.data.User;
import com.hsehhh.sv3.fragments.CreateEventFragment;
import com.hsehhh.sv3.fragments.EventDetailFragment;
import com.hsehhh.sv3.fragments.MyAlertDialogFragment;
import com.hsehhh.sv3.fragments.MyEventsFragment;
import com.hsehhh.sv3.fragments.ProfileFragment;
import com.hsehhh.sv3.fragments.ProfileSettingsFragment;
import com.hsehhh.sv3.fragments.ScrollingFragment;
import com.hsehhh.sv3.interfaces.FragmentSwitcher;

public class MainActivity extends AppCompatActivity implements FragmentSwitcher {

    //constants
    public static final int RC_SIGN_IN = 1;
    public static final int RC_USER_CHANGED = 2;

    //UI objects
    Toolbar mainToolbar;
    FrameLayout mainFrame;

    //Fragments
    FragmentTransaction fragmentTransaction;

    ScrollingFragment scrollingFragment;
    CreateEventFragment createEventFragment;
    MyEventsFragment myEventsFragment;
    EventDetailFragment eventDetailFragment;
    ProfileFragment profileFragment;
    ProfileSettingsFragment profileSettingsFragment;


    Fragment lastViewedFragment;

    //  NewEventDetail newEventDetail;
    DialogFragment dialogFragment;


    //Firebase objects
    private FirebaseAuth.AuthStateListener firebaseAuthStateListener;
    private FirebaseAuth firebaseAuth;

    public FirebaseDatabase firebaseDatabase;
    public DatabaseReference usersReference;
    public DatabaseReference chatsReference;
    public DatabaseReference eventsReference;

    // User objects
    public FirebaseUser firebaseUser;
    public User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Init toolbar and frame
        mainToolbar = findViewById(R.id.toolbar_main);
        setSupportActionBar(mainToolbar);
        mainFrame = findViewById(R.id.frame_main);

        if(!isNetworkAvailable())
            Toast.makeText(MainActivity.this, "Нет подключения к сети", Toast.LENGTH_LONG).show();

//        //Fullscreen/hidden status bar
//        View decorView = getWindow().getDecorView();
//        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
//
//        ActionBar actionBar = this.getActionBar();
//        actionBar.hide();


            //Init database reference
            initDatabaseReferences();

            //Init authorization
            firebaseAuth = FirebaseAuth.getInstance();
            firebaseAuthStateListener = new FirebaseAuth.AuthStateListener() {
                @Override
                public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                    firebaseUser = firebaseAuth.getCurrentUser();
                    if (firebaseUser == null || firebaseUser.isAnonymous())
                        onSignedOut();
                    else
                        onSignedIn();
                }
            };


        initFragments(); //
        showDefaultFragment();
    }

    private void onSignedIn() {
        usersReference.child(firebaseUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot == null || dataSnapshot.getValue() == null) {
                    // user is new
                    switchToProfileSettings();
                } else {                     // user already existed
                    user = dataSnapshot.getValue(User.class);
                    user.ID = firebaseUser.getUid();
                    if(isNetworkAvailable())
                    {
                        Query q = getUsersReference().child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                        q.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                Toast.makeText(MainActivity.this, "Вы вошли как " + dataSnapshot.getValue(User.class).name, Toast.LENGTH_SHORT).show();
                            }
                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // TODO: Посмотреть, что такое OnCancelled
                Toast.makeText(MainActivity.this, "Cancelled", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void onSignedOut() {
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setIsSmartLockEnabled(false)
                        .setLogo(R.drawable.trilist)
                        .build(),
                RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, final Intent data) {
        switch (requestCode) {
            case RC_SIGN_IN: {
                if (resultCode == RESULT_OK) {
                    firebaseUser = firebaseAuth.getCurrentUser();
                    Toast.makeText(this, "Signed in!", Toast.LENGTH_SHORT).show();
                } else if (resultCode == RESULT_CANCELED) {
                    Toast.makeText(this, "Cancelled", Toast.LENGTH_SHORT).show();
                }
                break;
            }
            case RC_USER_CHANGED: {
                if (resultCode == RESULT_OK) {
                    usersReference.child(firebaseUser.getUid()).setValue(user);
                    Toast.makeText(this, "User changed!", Toast.LENGTH_SHORT).show();
                } else if (resultCode == RESULT_CANCELED) {
                    Toast.makeText(this, "Cancelled", Toast.LENGTH_SHORT).show();
                }
                break;
            }
            default: {
                super.onActivityResult(requestCode, resultCode, data);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        firebaseAuth.addAuthStateListener(firebaseAuthStateListener);
        if(!isNetworkAvailable())
            Toast.makeText(MainActivity.this, "Нет подключения к сети", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (firebaseAuthStateListener != null) {
            firebaseAuth.removeAuthStateListener(firebaseAuthStateListener);
        }
    }

    // Uitility database references methods
    void initDatabaseReferences() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        eventsReference = firebaseDatabase.getReference("events");
        usersReference = firebaseDatabase.getReference("users");
        chatsReference = firebaseDatabase.getReference("chats");
    }

    public DatabaseReference getEventsReference() {
        return eventsReference;
    }

    public DatabaseReference getUsersReference() {
        return usersReference;
    }

    public DatabaseReference getChatsReference() {
        return chatsReference;
    }


    // Utility fragment methods
    void initFragments() {
        scrollingFragment = new ScrollingFragment();
        myEventsFragment = new MyEventsFragment();
        dialogFragment = new MyAlertDialogFragment();
        createEventFragment = new CreateEventFragment();
        eventDetailFragment = new EventDetailFragment();
        profileFragment = new ProfileFragment();
        profileSettingsFragment = new ProfileSettingsFragment();
        // newEventDetail = new NewEventDetail();
    }

    void showDefaultFragment() {
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.frame_main, scrollingFragment);
        fragmentTransaction.commitAllowingStateLoss();

        lastViewedFragment = getSupportFragmentManager().findFragmentById(R.id.frame_main);
    }

    // TODO: Посмотреть, есть ли более гуманный способ переключения на предыдущий фрагмент.
    @Override
    public void switchToPrevious() {
        getSupportFragmentManager().popBackStack();
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame_main, lastViewedFragment);
        fragmentTransaction.commitAllowingStateLoss();
    }

    @Override
    public void switchToMyEvents() {
        lastViewedFragment = getSupportFragmentManager().findFragmentById(R.id.frame_main);

        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_up, R.anim.slide_down);
        fragmentTransaction.replace(R.id.frame_main, myEventsFragment);
        fragmentTransaction.addToBackStack("main");
        fragmentTransaction.commitAllowingStateLoss();
    }

    @Override
    public void switchToScrolling() {
        lastViewedFragment = getSupportFragmentManager().findFragmentById(R.id.frame_main);

        getSupportFragmentManager().popBackStack("main", 0);
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame_main, scrollingFragment);
        fragmentTransaction.commitAllowingStateLoss();

    }

    @Override
    public void switchToCreateEvent() {
        lastViewedFragment = getSupportFragmentManager().findFragmentById(R.id.frame_main);

        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
        fragmentTransaction.replace(R.id.frame_main, createEventFragment);

        fragmentTransaction.addToBackStack("main");

        fragmentTransaction.commitAllowingStateLoss();
    }

    @Override
    public void switchToEventDetails(Event e) {
        lastViewedFragment = getSupportFragmentManager().findFragmentById(R.id.frame_main);

        Bundle eventArgs = new Bundle();
        eventArgs.putParcelable("event", e);
        eventDetailFragment.setArguments(eventArgs);

        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame_main, eventDetailFragment);

        fragmentTransaction.addToBackStack("frag");

        fragmentTransaction.commitAllowingStateLoss();
    }

    @Override
    public void switchToProfile() {
        lastViewedFragment = getSupportFragmentManager().findFragmentById(R.id.frame_main);

//        if (wasChanged)
//            profileFragment.onInfoChanged();
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
        fragmentTransaction.replace(R.id.frame_main, profileFragment);

        fragmentTransaction.addToBackStack("main");

        fragmentTransaction.commitAllowingStateLoss();
    }

    @Override
    public void switchToProfileSettings() {

        lastViewedFragment = getSupportFragmentManager().findFragmentById(R.id.frame_main);

        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
        fragmentTransaction.replace(R.id.frame_main, profileSettingsFragment);

        fragmentTransaction.addToBackStack("main");

        fragmentTransaction.commitAllowingStateLoss();

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

    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

}

