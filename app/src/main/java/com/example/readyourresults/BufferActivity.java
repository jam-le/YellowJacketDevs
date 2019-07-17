package com.example.readyourresults;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.readyourresults.Database.DatabaseHelper;
import com.example.readyourresults.Password.CreatePasswordActivity;
import com.example.readyourresults.Password.PasswordDialogueFragment;
import com.example.readyourresults.TestResult.TestResultActivity;
import com.google.android.material.snackbar.Snackbar;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import static androidx.camera.core.CameraX.getContext;

public class BufferActivity extends AppCompatActivity implements
        PasswordDialogueFragment.PasswordDialogueListener {

    Button viewResultsButton;
    Button discardButton;
    Button saveResultButton;
    DatabaseHelper database;
    DateFormat dateFormat;
    Date date;
    String password;

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

        database = new DatabaseHelper(getApplicationContext());
        dateFormat = new SimpleDateFormat("yyyy/MM/dd");

        TextView analyzingResult = (TextView) findViewById(R.id.summary);
        analyzingResult.setText("ANALYSIS COMPLETE");
        TextView viewResults = (TextView) findViewById(R.id.askresults);
        viewResults.setVisibility(View.VISIBLE);
        LinearLayout results_button_container = (LinearLayout) findViewById(R.id.results_button_container);
        results_button_container.setVisibility(View.VISIBLE);

        viewResultsButton = findViewById(R.id.view_result);
        discardButton = findViewById(R.id.discard);
        saveResultButton = findViewById(R.id.saveresult);

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

        discardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        saveResultButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handlePasswordProtection(imagePath);
            }
        });
    }

    public void handlePasswordProtection(String imagePath) {
        //load password
        SharedPreferences settings = getSharedPreferences("PREFS", 0 );
        String storedPassword = settings.getString("password", "");
        if(storedPassword.equals("")) {
            //if there is no password
            Intent intent = new Intent(BufferActivity.this, CreatePasswordActivity.class);
            startActivity(intent);
            finish();
        } else {
            Log.d("Buffer", "pass" + storedPassword);
            date = new Date();
            final String testTypeText = getIntent().getStringExtra("Test Type");
            database.insertData(testTypeText, "Inconclusive",
                    dateFormat.format(date),
                    imagePath);
            openPasswordDialog();
        }
    }

    public void openPasswordDialog() {
        PasswordDialogueFragment passwordDialogueFragment = new PasswordDialogueFragment("Saved");
        passwordDialogueFragment.show(getSupportFragmentManager(), "password Dialogue");
    }

    @Override
    public void applyText(String password) {
        this.password = password;
    }
}
