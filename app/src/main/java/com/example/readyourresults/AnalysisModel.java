package com.example.readyourresults;

import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.ml.common.FirebaseMLException;
import com.google.firebase.ml.common.modeldownload.FirebaseLocalModel;
import com.google.firebase.ml.common.modeldownload.FirebaseModelManager;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.label.FirebaseVisionImageLabel;
import com.google.firebase.ml.vision.label.FirebaseVisionImageLabeler;
import com.google.firebase.ml.vision.label.FirebaseVisionOnDeviceAutoMLImageLabelerOptions;

import java.util.List;

public class AnalysisModel {
    Bitmap bitmapImage;

    public AnalysisModel(Bitmap btm) {
        bitmapImage = btm;
        FirebaseApp.initializeApp(WelcomeActivity.getAppContext());
        FirebaseLocalModel localModel = new FirebaseLocalModel.Builder("app.assets.model")
                .setAssetFilePath("manifest.json")
                .build();
        FirebaseModelManager.getInstance().registerLocalModel(localModel);
    }

    public String interpret() {
        String ans = "";
        FirebaseVisionImage image = FirebaseVisionImage.fromBitmap(bitmapImage);

        FirebaseVisionOnDeviceAutoMLImageLabelerOptions labelerOptions =
                new FirebaseVisionOnDeviceAutoMLImageLabelerOptions.Builder()
                        .setLocalModelName("my_local_model")    // Skip to not use a local model
                        .setConfidenceThreshold(.5f)  // Evaluate your model in the Firebase console
                        // to determine an appropriate value.
                        .build();
        try {
            FirebaseVisionImageLabeler labeler =
                    FirebaseVision.getInstance().getOnDeviceAutoMLImageLabeler(labelerOptions);
            labeler.processImage(image)
                    .addOnSuccessListener(new OnSuccessListener<List<FirebaseVisionImageLabel>>() {
                        @Override
                        public void onSuccess(List<FirebaseVisionImageLabel> labels) {
                            // Task completed successfully
                            for (FirebaseVisionImageLabel label: labels) {
                                String text = label.getText();
                                float confidence = label.getConfidence();
                                Log.d("CameraXApp", text + ": " + confidence);
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Task failed with an exception
                            Log.d("CameraXApp", "hello"+e.getMessage());
                        }
                    });
        } catch (FirebaseMLException e) {
            Log.d("CameraXApp", "FirebaseMLException during getOnDeviceAutoMLImageLabeler()");
        }
        return null;
    }
}
