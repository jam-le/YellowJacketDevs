package com.example.readyourresults;

import com.google.firebase.ml.common.modeldownload.FirebaseLocalModel;
import com.google.firebase.ml.common.modeldownload.FirebaseModelManager;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;

public class AnalysisModel {
    public static void main(String[] args) {

        FirebaseLocalModel localModel = new FirebaseLocalModel.Builder("app.assets.model")
                .setAssetFilePath("manifest.json")
                .build();
        FirebaseModelManager.getInstance().registerLocalModel(localModel);

        FirebaseVisionImage image = FirebaseVisionImage.fromBitmap(bitmap);
    }
}
