package com.example.readyourresults;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

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
        FirebaseLocalModel localModel = new FirebaseLocalModel.Builder("my_local_model")
                .setAssetFilePath("model/manifest.json")
                .build();
        FirebaseModelManager.getInstance().registerLocalModel(localModel);


        //Testing different files passed into .setAssetFilePath
        //FirebaseLocalModel localModel2 = new FirebaseLocalModel.Builder("my_local_model2")
        //        .setAssetFilePath("model/model.tflite")
        //        .build();
        //Log.d("FirebaseLocalModels", localModel + " " + localModel2 );
        // the local models do not appear to be null
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
                            String maxLable = "";
                            for (FirebaseVisionImageLabel lab: labels) {
                                String text = lab.getText();
                                float confidence = lab.getConfidence();
                                Log.d("CameraXApp", text + ": " + confidence);
                                if (confidence > max) {
                                    max = confidence;
                                    maxLable = lab.getText();
                                }
                            }
                            setLable(maxLable);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Task failed with an exception
                            Log.d("CameraXApp", "hello "+ e.getMessage());
                        }
                    });
        } catch (FirebaseMLException e) {
            Log.d("CameraXApp", "FirebaseMLException during getOnDeviceAutoMLImageLabeler()");
        }

        Log.d("Label", ""+this.lable);
        return this.getLable();
    }
    public String getLable() {
        return this.lable;
    }
    private void setLable(String l) {
        this.lable = l;
    }
}
