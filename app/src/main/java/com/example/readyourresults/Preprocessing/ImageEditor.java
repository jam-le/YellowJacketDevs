package com.example.readyourresults.Preprocessing;

import android.content.Context;
import android.util.Log;

import java.io.File;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class ImageEditor {
    /*
     * TODO/ Use ImageProcessor to create new training images from the ones we have.
     * TODO/ Treat it as a black box with constructor ImageProcessor(File file, String dest)
     * TODO/ It will take the file and write the processed image to the path dest
     * TODO/ I didn't want to merge yet, but we should start on this
     */
    public ImageEditor(Context context) {
        File dir = new File(context.getExternalMediaDirs()[0] + "/training_data/invalid");
        File dest = new File(context.getExternalMediaDirs()[0] + "/processed_data/invalid");
//        for (File file : dir.listFiles()) {
////            Log.e(TAG, file.getName());
//            ImageProcessor imageProcessor = new ImageProcessor(file,dest);
////            Log.e(TAG, imageProcessor.getPath());
//        }
//        dir = new File(context.getExternalMediaDirs()[0] + "/training_data/reactive");
//        dest = new File(context.getExternalMediaDirs()[0] + "/processed_data/reactive");
//        for (File file : dir.listFiles()) {
////            Log.e(TAG, file.getName());
//            ImageProcessor imageProcessor = new ImageProcessor(file,dest);
////            Log.e(TAG, imageProcessor.getPath());
//        }
        dir = new File(context.getExternalMediaDirs()[0] + "/training_data/nonreactive");
        dest = new File(context.getExternalMediaDirs()[0] + "/processed_data/nonreactive");
        for (File file : dir.listFiles()) {
//            Log.e(TAG, file.getName());
            ImageProcessor imageProcessor = new ImageProcessor(file,dest);
            Log.d(TAG, imageProcessor.getPath());
        }
    }
}

