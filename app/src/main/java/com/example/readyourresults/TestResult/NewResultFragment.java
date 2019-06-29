package com.example.readyourresults.TestResult;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.readyourresults.CloseResultDialogFragment;
import com.example.readyourresults.Database.DatabaseHelper;
import com.example.readyourresults.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class NewResultFragment extends Fragment {

    DatabaseHelper database;
    ImageView imageView;
    Bitmap bitmapImage;
    TextView testType;
    TextView testOutcome;
    TextView testDate;
    Button saveButton;
    Button closeButton;
    Date date;
    DateFormat dateFormat;
    String imagePath;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (container != null) {
            container.removeAllViews();
        }
        return inflater.inflate(R.layout.fragment_new_result, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        imageView = getView().findViewById(R.id.test_device_image);
        //testType = getView().findViewById(R.id.testType);
        testOutcome = getView().findViewById(R.id.test_result_outcome_text);
        //testDate = getView().findViewById(R.id.testDate);
        saveButton = getView().findViewById(R.id.save_result_btn);
        closeButton = getView().findViewById(R.id.close_result_btn);

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

        final String testTypeText = getActivity().getIntent().getStringExtra("Test Type");

        dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        date = new Date();
        //testDate.setText(dateFormat.format(date));

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                database.insertData(testTypeText, "Inconclusive",
                        dateFormat.format(date),
                        imagePath);
                Toast toast = Toast.makeText(getContext(), "Saved", Toast.LENGTH_SHORT);
                toast.show();
            }
        });

        //Close Results
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                CloseResultDialogFragment newFragment = new CloseResultDialogFragment();
                newFragment.show(fragmentManager, "closeDialog");
            }
        });

        //show confidences
        String labels = getArguments().getString("CONFIDENCES");
        TextView confidences = getActivity().findViewById(R.id.confidences_text);

        confidences.setText(labels);
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
