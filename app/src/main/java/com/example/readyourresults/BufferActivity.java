package com.example.readyourresults;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.os.AsyncTask;

import com.google.android.material.snackbar.Snackbar;

public class BufferActivity extends AppCompatActivity {

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

        String msg = getIntent().getStringExtra("IMAGE_SUCCESSFULLY_CAPTURED");
        Snackbar.make(findViewById(R.id.activity_buffer_layout), msg, Snackbar.LENGTH_SHORT).show();

        Button viewMyResultsNow = findViewById(R.id.yes);
        viewMyResultsNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Go to results page

                Bundle bundle = new Bundle();
                bundle.putString("CONFIDENCES", getIntent().getStringExtra("RESULTS_AND_CONFIDENCES"));
                Fragment newResultFragment = new NewResultFragment();
                newResultFragment.setArguments(bundle);
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.activity_buffer_layout, newResultFragment)
                        .commit();
            }
        });

    }

    @Override
    public void onBackPressed() {
        //need to fix the back press behavior for this activity
        super.onBackPressed();
    }
}
