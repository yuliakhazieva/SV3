package com.hsehhh.sv3;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class CreateEvent extends AppCompatActivity {

    private Button backButt, doneButt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        backButt = (Button) findViewById(R.id.backButt);
        doneButt = (Button) findViewById(R.id.doneButt);

        backButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CreateEvent.this, ScrollingActivity.class));
            }
        });

        doneButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CreateEvent.this, ScrollingActivity.class));
            }
        });
    }
}
