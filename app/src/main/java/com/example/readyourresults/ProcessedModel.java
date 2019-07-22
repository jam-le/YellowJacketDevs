package com.example.readyourresults;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.readyourresults.Camera.ResultsInterpreted;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.ml.common.FirebaseMLException;
import com.google.firebase.ml.common.modeldownload.FirebaseLocalModel;
import com.google.firebase.ml.common.modeldownload.FirebaseModelManager;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.label.FirebaseVisionImageLabel;
import com.google.firebase.ml.vision.label.FirebaseVisionImageLabeler;
import com.google.firebase.ml.vision.label.FirebaseVisionOnDeviceAutoMLImageLabelerOptions;

import java.util.HashMap;
import java.util.List;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class ProcessedModel {
    Bitmap bitmapImage;
    private String label;
    private float maxConfidence;
    private HashMap<String, Float> labelConfidences = new HashMap<>();

    private ResultsInterpreted resultsInterpreted;

    public ProcessedModel(Bitmap btm, Context con, ResultsInterpreted event) {
        bitmapImage = btm;
        FirebaseApp.initializeApp(con);
        FirebaseLocalModel localModel = new FirebaseLocalModel.Builder("my_processed_model")
                .setAssetFilePath("processed_model/manifest.json")
                .build();
        FirebaseModelManager.getInstance().registerLocalModel(localModel);

        resultsInterpreted = event;

        //Testing different files passed into .setAssetFilePath
        //FirebaseLocalModel localModel2 = new FirebaseLocalModel.Builder("my_local_model2")
        //        .setAssetFilePath("model/model.tflite")
        //        .build();
        //Log.d("FirebaseLocalModels", localModel + " " + localModel2 );
        // the local models do not appear to be null
    }

    public void interpret() {
        Log.e(TAG, "Hello");
        String ans = "";
        FirebaseVisionImage image = FirebaseVisionImage.fromBitmap(bitmapImage);

        FirebaseVisionOnDeviceAutoMLImageLabelerOptions labelerOptions =
                new FirebaseVisionOnDeviceAutoMLImageLabelerOptions.Builder()
                        .setLocalModelName("my_processed_model")    // Skip to not use a local model
                        .setConfidenceThreshold(.5f)  // Evaluate your model in the Firebase console
                        // to determine an appropriate value.
                        .build();
        try {
            FirebaseVisionImageLabeler labeler =
                    FirebaseVision.getInstance().getOnDeviceAutoMLImageLabeler(labelerOptions);
            Log.d("CameraXApp", "Set labeler =" + labeler);

            labeler.processImage(image)
                    .addOnSuccessListener(new OnSuccessListener<List<FirebaseVisionImageLabel>>() {
                        @Override
                        public void onSuccess(List<FirebaseVisionImageLabel> labels) {
                            // Jamie: Somehow labels array is empty. Not sure if this is how
                            // it is supposed to be.
                            Log.d("Labels: ",labels.toString());
                            // Task completed successfully
                            float max = 0f;
                            String maxLabel = "";
                            for (FirebaseVisionImageLabel lab: labels) {
                                String text = lab.getText();
                                float confidence = lab.getConfidence();
                                Log.d("CameraXApp", text + ": " + confidence);
                                labelConfidences.put(text, confidence);
                                if (confidence > max) {
                                    max = confidence;
                                    maxLabel = lab.getText();
                                    setLabel(maxLabel);
                                    setMaxConfidence(max);
                                }
                            }
                            Log.d("ProcessedModel: ", labelConfidences.toString() + "max label:" + maxLabel);

                            resultsInterpreted.resultsInterpreted(labelConfidences, maxLabel, maxConfidence);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Task failed with an exception
                            Log.d("CameraXApp", "hello "+ e.getMessage());
                        }
                    })
                    .addOnCompleteListener(new OnCompleteListener<List<FirebaseVisionImageLabel>>() {
                        @Override
                        public void onComplete(@NonNull Task<List<FirebaseVisionImageLabel>> task) {
                            Log.d("ProcessedModel: ", "interpret() completed. labelConfidences = " + labelConfidences);
                        }
                    });
        } catch (FirebaseMLException e) {
            Log.d("CameraXApp", "FirebaseMLException during getOnDeviceAutoMLImageLabeler()");
        }
    }
    private void setLabel(String l) {
        this.label = l;
    }

    private void setMaxConfidence(float maxConfidence) {
        this.maxConfidence = maxConfidence;
    }
}
