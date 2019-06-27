package com.example.readyourresults;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.os.AsyncTask;

import com.example.readyourresults.TestResult.TestResultActivity;
import com.google.android.material.snackbar.Snackbar;

public class BufferActivity extends AppCompatActivity {
    Button viewResultsButton;
    Fragment fragment;
    FragmentManager fragmentManager = getSupportFragmentManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buffer);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,
                WindowManager.LayoutParams.FLAG_SECURE);
        final Handler handler = new Handler();

        new AsyncTask<Void, Void, Void>() {
            protected void onPostExecute(Void avoid) {
                super.onPostExecute(avoid);
                final ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
                progressBar.setVisibility(View.GONE);
                final TextView analyzingResult = (TextView) findViewById(R.id.summary);
                analyzingResult.setText("ANALYSIS COMPLETE");
                final TextView viewResults = (TextView) findViewById(R.id.askresults);
                viewResults.setVisibility(View.VISIBLE);
                final LinearLayout results_button_container = (LinearLayout) findViewById(R.id.results_button_container);
                results_button_container.setVisibility(View.VISIBLE);
            }

            @Override
            protected Void doInBackground(Void... voids) {
                try {
                    Thread.sleep(5000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }
        }.execute();
        final String testType = getIntent().getStringExtra("Test Type");
        final String imagePath = getIntent().getStringExtra("Image Path");
        String msg = getIntent().getStringExtra("IMAGE_SUCCESSFULLY_CAPTURED");
        Snackbar.make(findViewById(R.id.activity_buffer_layout), msg, Snackbar.LENGTH_SHORT).show();
        viewResultsButton = findViewById(R.id.view_result);
        viewResultsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), TestResultActivity.class);
                intent.putExtra("Test Type", testType);
                intent.putExtra("Image Path", imagePath);
                intent.putExtra("RESULTS_AND_CONFIDENCES",
                        getIntent().getStringExtra("RESULTS_AND_CONFIDENCES"));
                startActivity(intent);
            }
        });
    }
}
