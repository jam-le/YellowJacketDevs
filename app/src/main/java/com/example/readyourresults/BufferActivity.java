package com.example.readyourresults;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;

import com.google.android.material.snackbar.Snackbar;

public class BufferActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buffer);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,
                WindowManager.LayoutParams.FLAG_SECURE);


        String msg = getIntent().getStringExtra("IMAGE_SUCCESSFULLY_CAPTURED");
        Snackbar.make(findViewById(R.id.activity_buffer_layout), msg, Snackbar.LENGTH_SHORT).show();
    }
}
