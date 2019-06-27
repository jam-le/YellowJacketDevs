package com.example.readyourresults.TestResult;

import android.app.AlertDialog;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.readyourresults.Database.DatabaseHelper;
import com.example.readyourresults.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class TestResultFragment extends Fragment {

    DatabaseHelper database;
    ImageView imageView;
    Bitmap bitmapImage;
    TextView testType;
    TextView testOutcome;
    TextView testDate;
    Button saveButton;
    Date date;
    DateFormat dateFormat;
    String imagePath;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_test_result, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        imageView = getView().findViewById(R.id.savedImage);
        testType = getView().findViewById(R.id.testType);
        testOutcome = getView().findViewById(R.id.testOutcome);
        testDate = getView().findViewById(R.id.testDate);
        saveButton = getView().findViewById(R.id.save_button);

        imagePath = getActivity().getIntent().getStringExtra("Image Path");
        Log.d(TAG, "TEST IMAGE " + imagePath);
        bitmapImage = BitmapFactory.decodeFile(imagePath);
        Matrix matrix = new Matrix();
        matrix.postRotate(90);
        if (bitmapImage != null) {
            Bitmap rotatedBitmap = Bitmap.createBitmap(bitmapImage, 0, 0, bitmapImage.getWidth(),
                    bitmapImage.getHeight(), matrix, true);
            imageView.setImageBitmap(rotatedBitmap);
        } else {
            imageView.setImageBitmap(bitmapImage);
        }

        String testTypeText = getActivity().getIntent().getStringExtra("Test Type");
        testType.setText(testTypeText);
        testOutcome.setText("Inconclusive");

        dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        date = new Date();
        testDate.setText(dateFormat.format(date));

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                database.insertData(testType.getText().toString(), "Inconclusive",
                        dateFormat.format(date),
                        imagePath);
                Toast toast = Toast.makeText(getContext(), "Saved", Toast.LENGTH_SHORT);
                toast.show();
            }
        });
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        database = new DatabaseHelper(getContext());
    }

    public void showMessage(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }
}