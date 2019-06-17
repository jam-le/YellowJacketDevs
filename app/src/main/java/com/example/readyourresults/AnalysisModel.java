package com.example.readyourresults;

import android.content.Context;
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
    private String lable;

    public AnalysisModel(Bitmap btm, Context con) {
        bitmapImage = btm;
        FirebaseApp.initializeApp(con);
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
                            float max = 0f;
                            String maxLable = "";
                            for (FirebaseVisionImageLabel label: labels) {
                                String text = label.getText();
                                float confidence = label.getConfidence();
                                Log.d("CameraXApp", text + ": " + confidence);
                                if (confidence > max) {
                                    max = confidence;
                                    maxLable = lable;
                                }
                            }
                            setLable(maxLable);
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
        return this.getLable();
    }
    public String getLable() {
        return this.lable;
    }
    private void setLable(String l) {
        this.lable = l;
    }
}
