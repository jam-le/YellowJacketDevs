package com.example.readyourresults.Preprocessing;

import android.content.Context;
import android.graphics.Bitmap;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

import com.example.readyourresults.R;

import org.opencv.android.OpenCVLoader;
import org.opencv.core.Mat;

import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.features2d.FeatureDetector;
import org.opencv.imgproc.Imgproc;
import org.opencv.utils.*;

import java.io.File;

import static androidx.constraintlayout.widget.Constraints.TAG;
import static org.opencv.android.Utils.bitmapToMat;
import static org.opencv.android.Utils.matToBitmap;
import static org.opencv.core.Core.add;
import static org.opencv.imgcodecs.Imgcodecs.imread;
import static org.opencv.imgcodecs.Imgcodecs.imwrite;
import static org.opencv.imgproc.Imgproc.Canny;
import static org.opencv.imgproc.Imgproc.HOUGH_GRADIENT;
import static org.opencv.imgproc.Imgproc.HoughCircles;
import static org.opencv.imgproc.Imgproc.equalizeHist;


public class ImageProcessor {
    static {
        if (!OpenCVLoader.initDebug()) {
            Log.d(TAG,"oops");
        }
    }
    String path;
    public ImageProcessor(File img) {
        //OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_2_4_11, this, baseLoaderCallback );

        Mat src = new Mat();
        src = imread(img.getAbsolutePath());
        Mat out = new Mat();
        //70x125
        //70x95
        //bitmapToMat(m,m,false);
//        for (int i = 30; i <= 100; i+=10) {
//            for (int j = 35; j <= 205; j+=10) {
//                //Canny(src, src, i, j, false);
//                //path = img.getAbsolutePath().substring(0, img.getAbsolutePath().lastIndexOf(".jpg")) + ":" + i + "x" + j + "Gray_Image.bmp";
//                //imwrite(path, out);
//            }
//        }

        //Circle Detection
        Mat gray = new Mat();
        Imgproc.cvtColor(src, gray, Imgproc.COLOR_BGR2GRAY);
        Imgproc.medianBlur(gray, gray, 5);
        Mat circles = new Mat();
        Imgproc.HoughCircles(gray, circles, Imgproc.HOUGH_GRADIENT, 1.0,
                (double)gray.rows()/16, // change this value to detect circles with different distances to each other
                100.0, 60.0, gray.cols()/8, gray.cols()/3); // change the last two parameters
        // (min_radius & max_radius) to detect larger circles
        for (int x = 0; x < circles.cols(); x++) {
            double[] c = circles.get(0, x);
            Point center = new Point(Math.round(c[0]), Math.round(c[1]));
            // circle center
            //Imgproc.circle(src, center, 1, new Scalar(0,100,100), 3, 8, 0 );
            // circle outline
            int radius = (int) Math.round(c[2]);
            //Imgproc.circle(src, center, radius, new Scalar(255,0,255), 3, 8, 0 );
        }

        //Informed crop
        Point p1,p2,p3,p4;
        double[] circle = circles.get(0,0);
        Point center = new Point(Math.round(circle[0]), Math.round(circle[1]));
        int rad = (int) Math.round(circle[2]);
        p1 = new Point(center.x-rad,center.y-rad);
        p2 = new Point(center.x+rad,center.y-rad);
        p3 = new Point(center.x-rad,center.y+rad);
        p4 = new Point(center.x+rad,center.y+rad);
        Rect rectCrop = new Rect((int)p1.x, (int)p1.y , (int)(p4.x-p1.x+1), (int)(p4.y-p1.y+1));
        src = src.submat(rectCrop);

        Imgproc.cvtColor(src, gray, Imgproc.COLOR_BGR2GRAY);
        equalizeHist(gray,src);
        Mat comb = gray;
        //add(gray,src,comb);
        //Canny(src, src, 70, 95, false);

        path = img.getAbsolutePath().substring(0, img.getAbsolutePath().lastIndexOf(".jpg")) + ":" + "Circle.bmp";
        imwrite(path, src);
        Log.d(TAG,path);
//        int j = 260;
//        for (int i = j; i <= j+70; i += 10) {
//            Canny(src, src, i, i+40, false);
//            path = img.getAbsolutePath().substring(0, img.getAbsolutePath().lastIndexOf(".jpg")) + ":" +i+ "edge.bmp";
//            imwrite(path, src);
//        }

    }
    public String toString() {
        return path;
    }

}
