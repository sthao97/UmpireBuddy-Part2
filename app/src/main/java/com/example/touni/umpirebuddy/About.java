package com.example.touni.umpirebuddy;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

public class About extends AppCompatActivity {

    protected TextView AboutText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        AboutText = findViewById(R.id.aboutD);
    }

    public void exit_about_activity(View view) {

        Intent intent = new Intent(About.this, MainActivity.class );
        startActivity(intent);

    }
}
