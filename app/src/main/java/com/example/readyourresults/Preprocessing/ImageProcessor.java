package com.example.readyourresults.Preprocessing;

import java.io.File;

public class ImageProcessor {
    String destinationPath;
    ImageProcessor(File file, String dest) {
        //To see actual preprocessing code, checkout the Preprocessing branch
        destinationPath = dest;
    }

    public String getDestinationPath() {
        return destinationPath;
    }
}
