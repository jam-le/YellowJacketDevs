package com.example.readyourresults;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.readyourresults.TestResult.TestResultActivity;
import com.google.android.material.snackbar.Snackbar;

public class BufferActivity extends AppCompatActivity {
    Button viewResultsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buffer);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,
                WindowManager.LayoutParams.FLAG_SECURE);
        final String testType = getIntent().getStringExtra("Test Type");
        final String imagePath = getIntent().getStringExtra("Image Path");
        String msg = getIntent().getStringExtra("IMAGE_SUCCESSFULLY_CAPTURED");
        final String maxLabel = getIntent().getStringExtra("MAXLABEL");
        final Float maxConfidence = getIntent().getFloatExtra("MAXCONFIDENCE", 0f);
        Snackbar.make(findViewById(R.id.activity_buffer_layout), msg, Snackbar.LENGTH_SHORT).show();

        TextView analyzingResult = (TextView) findViewById(R.id.summary);
        analyzingResult.setText("ANALYSIS COMPLETE");
        TextView viewResults = (TextView) findViewById(R.id.askresults);
        viewResults.setVisibility(View.VISIBLE);
        LinearLayout results_button_container = (LinearLayout) findViewById(R.id.results_button_container);
        results_button_container.setVisibility(View.VISIBLE);

        viewResultsButton = findViewById(R.id.view_result);
        viewResultsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), TestResultActivity.class);
                intent.putExtra("Test Type", testType);
                intent.putExtra("Image Path", imagePath);
                intent.putExtra("RESULTS_AND_CONFIDENCES",
                        getIntent().getStringExtra("RESULTS_AND_CONFIDENCES"));
                intent.putExtra("MAXLABEL", maxLabel);
                intent.putExtra("MAXCONFIDENCE", maxConfidence);
                startActivity(intent);
            }
        });
    }
}
