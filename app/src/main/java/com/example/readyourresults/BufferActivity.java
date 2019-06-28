package com.example.readyourresults;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.os.AsyncTask;

import com.google.android.material.snackbar.Snackbar;

import org.w3c.dom.Text;

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
        //Snackbar.make(findViewById(R.id.activity_buffer_layout), msg, Snackbar.LENGTH_SHORT).show();
    }
}
