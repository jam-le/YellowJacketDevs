package com.example.readyourresults.Preprocessing;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

import androidx.constraintlayout.solver.widgets.Rectangle;

import com.example.readyourresults.R;
import com.google.android.gms.vision.L;

import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Mat;

import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.features2d.FeatureDetector;
import org.opencv.imgproc.Imgproc;
import org.opencv.utils.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static androidx.constraintlayout.widget.Constraints.TAG;
import static org.opencv.android.Utils.bitmapToMat;
import static org.opencv.android.Utils.matToBitmap;
import static org.opencv.core.Core.add;
import static org.opencv.core.Core.bitwise_and;
import static org.opencv.core.Core.hconcat;
import static org.opencv.core.Core.inRange;
import static org.opencv.core.Core.split;
import static org.opencv.core.Core.vconcat;
import static org.opencv.imgcodecs.Imgcodecs.imread;
import static org.opencv.imgcodecs.Imgcodecs.imwrite;
import static org.opencv.imgproc.Imgproc.CHAIN_APPROX_SIMPLE;
import static org.opencv.imgproc.Imgproc.COLOR_BGR2GRAY;
import static org.opencv.imgproc.Imgproc.COLOR_GRAY2BGR;
import static org.opencv.imgproc.Imgproc.COLOR_RGB2HSV;
import static org.opencv.imgproc.Imgproc.CV_HOUGH_GRADIENT;
import static org.opencv.imgproc.Imgproc.Canny;
import static org.opencv.imgproc.Imgproc.GaussianBlur;
import static org.opencv.imgproc.Imgproc.HOUGH_GRADIENT;
import static org.opencv.imgproc.Imgproc.HoughCircles;
import static org.opencv.imgproc.Imgproc.HoughLinesP;
import static org.opencv.imgproc.Imgproc.RETR_EXTERNAL;
import static org.opencv.imgproc.Imgproc.THRESH_BINARY;
import static org.opencv.imgproc.Imgproc.approxPolyDP;
import static org.opencv.imgproc.Imgproc.arcLength;
import static org.opencv.imgproc.Imgproc.boundingRect;
import static org.opencv.imgproc.Imgproc.cvtColor;
import static org.opencv.imgproc.Imgproc.equalizeHist;
import static org.opencv.imgproc.Imgproc.findContours;
import static org.opencv.imgproc.Imgproc.line;
import static org.opencv.imgproc.Imgproc.medianBlur;
import static org.opencv.imgproc.Imgproc.resize;
import static org.opencv.imgproc.Imgproc.threshold;


public class ImageProcessor {
    static {
        if (!OpenCVLoader.initDebug()) {
            Log.d(TAG,"oops");
        }
    }
    String path;
    static int index = 0;
    public ImageProcessor(File img, File destDir) {
        try {
            index++;
            Mat src = new Mat();
            src = imread(img.getAbsolutePath());
            Mat out = new Mat();
            out = imread(img.getAbsolutePath());

            path = destDir.getAbsolutePath() + "/img" + index + ".bmp";
            Point[] corns = detectSquare(src, path);
            Boolean cropped = false;
            if (corns.length != 2) {
                Log.d(TAG, "OOPS");
            } else {
                Log.d(TAG, corns.toString());
                Point a = corns[0];
                Point b = corns[1];
                Rect rectCrop = new Rect(a, b);

                out = out.submat(rectCrop);
                cropped = true;
            }
            //Circle Detection
            Mat gray = new Mat();
            Imgproc.cvtColor(out, gray, COLOR_BGR2GRAY);
//        Log.d(TAG,"type1:" + out.type());
//        Log.d(TAG,"type2:" + gray.type());

            //crop to inner
//        Point top = new Point(gray.width()*4/16,gray.height()*4/16);
//        Point bot = new Point(gray.width()*12/16,gray.height()*12/16);
//        Rect crop = new Rect(top, bot);
//        gray = gray.submat(crop);
//        out = out.submat(crop);
            Mat equ = new Mat();
            equalizeHist(gray, equ);

            Mat blur = new Mat();
            Size b = new Size(9, 9);
            GaussianBlur(equ, blur, b, 2, 2);
            GaussianBlur(equ, blur, b, 2, 2);
            GaussianBlur(equ, blur, b, 2, 2);
            Double bratio = 600.0 / blur.width();
            resize(blur, blur, new Size(bratio * blur.width(), bratio * blur.height()));
            resize(equ, equ, new Size(blur.width(), blur.height()));

            Mat circles = new Mat();
            /// Apply the Hough Transform to find the circles
            HoughCircles(blur, circles, CV_HOUGH_GRADIENT, 1, 20, 80, 25, blur.rows() / 10, blur.rows() / 2);
            Canny(blur, blur, 80, 25, false);
            Imgproc.cvtColor(blur, blur, COLOR_GRAY2BGR);
            /// Draw the circles detected
            int avgx = blur.rows();
            int avgy = blur.cols();
            int avgr = blur.rows() / 2;
            int count = 1;
            for (int x = 0; x < circles.cols(); x++) {
                double[] c = circles.get(0, x);
                Point center = new Point(Math.round(c[0]), Math.round(c[1]));
                // circle center
                Imgproc.circle(blur, center, 1, new Scalar(0, 100, 100), 3, 8, 0);
                // circle outline
                int radius = (int) Math.round(c[2]);
                Imgproc.circle(blur, center, radius, new Scalar(255, 0, 255), 3, 8, 0);
                avgx += center.x;
                avgy += center.y;
                avgr += radius;
                count += 1;
            }
            avgx /= count;
            avgy /= count;
            avgr /= count;
            Point top = new Point((avgx - avgr < 0) ? 0 : avgx - avgr, (avgy - avgr < 0) ? 0 : avgy - avgr);
            Point bot = new Point((avgx + avgr > blur.width()) ? blur.width() : avgx + avgr, (avgy + avgr > blur.height()) ? blur.height() : avgy + avgr);
            Rect crop = new Rect(top, bot);
            equ = equ.submat(crop);
            equalizeHist(equ, equ);
//        String circPath = destDir.getAbsolutePath() + "/circs" +index + ".bmp";
//        imwrite(circPath,equ);
//        Log.e(TAG,circPath + ": " + circles.cols());

            Imgproc.cvtColor(equ, equ, COLOR_GRAY2BGR);
            Mat retVal = new Mat();
            ArrayList<Mat> mats = new ArrayList<>();
            //mats.add(out);
            mats.add(equ);
            //mats.add(BlueCrop(out));
            hconcat(mats, retVal);
            retVal = equ;
            if (retVal.width() > 600) {
                Double ratio = 600.0 / retVal.width();
                resize(retVal, retVal, new Size(ratio * retVal.width(), ratio * retVal.height()));
            }
            imwrite(path, retVal);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }

    public ImageProcessor(File img) {

        try {
            index++;
            Mat src = new Mat();
            src = imread(img.getAbsolutePath());
            Mat out = new Mat();
            out = imread(img.getAbsolutePath());

            path = img.getAbsolutePath().substring(0, img.getAbsolutePath().lastIndexOf(".")) + ":" + "processed.bmp";
            Point[] corns = detectSquare(src, path);
            Boolean cropped = false;
            if (corns.length != 2) {
                Log.d(TAG, "OOPS");
            } else {
                Log.d(TAG, corns.toString());
                Point a = corns[0];
                Point b = corns[1];
                Rect rectCrop = new Rect(a, b);

                out = out.submat(rectCrop);
                cropped = true;
            }
            //Circle Detection
            Mat gray = new Mat();
            Imgproc.cvtColor(out, gray, COLOR_BGR2GRAY);
//        Log.d(TAG,"type1:" + out.type());
//        Log.d(TAG,"type2:" + gray.type());

            //crop to inner
//        Point top = new Point(gray.width()*4/16,gray.height()*4/16);
//        Point bot = new Point(gray.width()*12/16,gray.height()*12/16);
//        Rect crop = new Rect(top, bot);
//        gray = gray.submat(crop);
//        out = out.submat(crop);
            Mat equ = new Mat();
            equalizeHist(gray, equ);

            Mat blur = new Mat();
            Size b = new Size(9, 9);
            GaussianBlur(equ, blur, b, 2, 2);
            GaussianBlur(equ, blur, b, 2, 2);
            GaussianBlur(equ, blur, b, 2, 2);
            Double bratio = 600.0 / blur.width();
            resize(blur, blur, new Size(bratio * blur.width(), bratio * blur.height()));
            resize(equ, equ, new Size(blur.width(), blur.height()));

            Mat circles = new Mat();
            /// Apply the Hough Transform to find the circles
            HoughCircles(blur, circles, CV_HOUGH_GRADIENT, 1, 20, 80, 25, blur.rows() / 10, blur.rows() / 2);
            Canny(blur, blur, 80, 25, false);
            Imgproc.cvtColor(blur, blur, COLOR_GRAY2BGR);
            /// Draw the circles detected
            int avgx = blur.rows();
            int avgy = blur.cols();
            int avgr = blur.rows() / 2;
            int count = 1;
            for (int x = 0; x < circles.cols(); x++) {
                double[] c = circles.get(0, x);
                Point center = new Point(Math.round(c[0]), Math.round(c[1]));
                // circle center
                Imgproc.circle(blur, center, 1, new Scalar(0, 100, 100), 3, 8, 0);
                // circle outline
                int radius = (int) Math.round(c[2]);
                Imgproc.circle(blur, center, radius, new Scalar(255, 0, 255), 3, 8, 0);
                avgx += center.x;
                avgy += center.y;
                avgr += radius;
                count += 1;
            }
            avgx /= count;
            avgy /= count;
            avgr /= count;
            Point top = new Point((avgx - avgr < 0) ? 0 : avgx - avgr, (avgy - avgr < 0) ? 0 : avgy - avgr);
            Point bot = new Point((avgx + avgr > blur.width()) ? blur.width() : avgx + avgr, (avgy + avgr > blur.height()) ? blur.height() : avgy + avgr);
            Rect crop = new Rect(top, bot);
            equ = equ.submat(crop);
            equalizeHist(equ, equ);
//        String circPath = destDir.getAbsolutePath() + "/circs" +index + ".bmp";
//        imwrite(circPath,equ);
//        Log.e(TAG,circPath + ": " + circles.cols());

            Imgproc.cvtColor(equ, equ, COLOR_GRAY2BGR);
            Mat retVal = new Mat();
            ArrayList<Mat> mats = new ArrayList<>();
            //mats.add(out);
            mats.add(equ);
            //mats.add(BlueCrop(out));
            hconcat(mats, retVal);
            retVal = equ;
            if (retVal.width() > 600) {
                Double ratio = 600.0 / retVal.width();
                resize(retVal, retVal, new Size(ratio * retVal.width(), ratio * retVal.height()));
            }
            imwrite(path, retVal);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }



    public String toString() {
        return path;
    }

    public String getPath() { return path; }

    private Point[] detectSquare(Mat img, String path) {
        Log.d(TAG,"Start detectSquare" + path);
        ArrayList<Point> points = new ArrayList<>();
        Mat resized =new Mat();
        Double ratio = 300.0 / img.width();
        resize(img, resized, new Size(ratio * img.width(), ratio * img.height()));
        Mat gray = new Mat();
        cvtColor(resized, gray,COLOR_BGR2GRAY);
        medianBlur(gray,gray,5);
        equalizeHist(gray,gray);
        Canny(gray,gray,0,100, false);
        int minLineLength = 8;
        int maxLineGap = 4;
        List<double[]> newlines = new ArrayList<>();
        Mat lines = new Mat();
        HoughLinesP(gray, lines, 1,Math.PI/180,85,minLineLength,maxLineGap);
        Log.d(TAG, "before" +lines.height());

        newlines = linesMerge(lines);
        Mat all = resized.clone();
        for (int i = 0; i < lines.rows(); i++) {
            double[] lin = lines.get(i,0);
            //Log.d(TAG, i +"out" +lin[1]);
            line(all, new Point(lin[0], lin[1]), new Point(lin[2], lin[3]), new Scalar(0, 255, 0));
        }
        path = path.substring(0, path.lastIndexOf(".")) + ":" + ":all.bmp";
        //imwrite(path, all);
        path = path.substring(0, path.lastIndexOf(":")) + ":" + ":new.bmp";
        for (double[] lin : newlines) {
            Log.d(TAG, "out" +lin[1]);
            line(resized, new Point(lin[0], lin[1]), new Point(lin[2], lin[3]), new Scalar(0, 255, 255));
        }

        //threshold(gray,gray,180,255,THRESH_BINARY);
        Point[] rect = insideCrop(newlines,resized);
        rect[0].x /= ratio;
        rect[0].y /= ratio;
        rect[1].x /= ratio;
        rect[1].y /= ratio;
        Rect crop = new Rect(rect[0], rect[1]);


        return rect;
    }

    private List<double[]> linesMerge(Mat lines) {
        List<double[]> verts = new ArrayList<>();
        List<double[]> horis = new ArrayList<>();
        for (int i = 0; i < lines.rows(); i++) {
            for (int j = 0; j < lines.cols(); j++) {
                //Log.d(TAG, "lines@"+i+", "+j+": " + lines.get(i,j).toString());
                double[] re = lines.get(i, j);
                double x1, y1, x2, y2;
                x1 = re[0];
                y1 = re[1];
                x2 = re[2];
                y2 = re[3];

                Point[] line = {new Point(x1, y1), new Point(x2, y2)};

                if (x2 == x1) {
                    //vertical
                    Log.d(TAG, i + ", "+x1+", v, perfect");
                    verts.add(re);
                } else {
                    double slope = (y2 - y1) / (x2 / x1);
                    if (slope > 15 || slope < -15) {
                        //vertical
                        verts.add(re);
                        Log.d(TAG, i + ", "+x1+", v, " + slope);
                    } else if (slope < 1.0 / 15 && slope > -1.0 / 15) {
                        //horizontal
                        horis.add(re);
                        Log.d(TAG, i + ", "+x1+", h, " + slope);
                    } else {
                        Log.d(TAG, i + ", "+x1+", oth, " + slope);
                    }
                }
            }
        }
        Log.d(TAG, "Merging");


        Log.d(TAG, "verts" + verts.size());
        Log.d(TAG, "horis" + horis.size());
        double[][] ans = new double[verts.size() + horis.size()][4];
        for (int i = 0; i < verts.size(); i++) {
            ans[i] = verts.get(i);
        }
        for (int i = verts.size(); i < verts.size() + horis.size(); i++) {
            ans[i] = horis.get(i - verts.size());
        }

        Log.d(TAG, "ans" + ans.length);
        List<double[]> retVal = new ArrayList<>();
        for (double[] line : ans) {
            retVal.add(line);
        }
        Log.d(TAG, "retVal" + retVal.size());
        return retVal;
    }

    private Point[] insideCrop(List<double[]> lines, Mat img) {
        Point tl = new Point();
        Point br = new Point();


        Point cent = new Point(img.width()/2,img.height()*.4375);

        List<Point[]> verts = new ArrayList<>();
        List<Point[]> horis = new ArrayList<>();
        for (double[] re : lines) {
            double x1, y1, x2, y2;
            x1 = re[0];
            y1 = re[1];
            x2 = re[2];
            y2 = re[3];

            Point[] line = {new Point(x1, y1), new Point(x2, y2)};

            if (x2 == x1) {
                //vertical
                verts.add(line);
            } else {
                double slope = (y2 - y1) / (x2 / x1);
                if (slope > 15 || slope < -15) {
                    //vertical
                    verts.add(line);
                } else if (slope < 1.0 / 15 && slope > -1.0 / 15) {
                    //horizontal
                    horis.add(line);
                } else {
                    //other
                }
            }

        }
        int maxDist = img.width()/6;
        int minPosDist = img.width()/2;
        int minNegDist = img.width()/2;
        for (Point[] line : verts) {
            int pos = (int)line[0].x;
            if (pos > cent.x && pos < cent.x + minPosDist && pos - cent.x > maxDist) {
                minPosDist = (int) (pos - cent.x);
            } else if (pos < cent.x && pos > cent.x - minNegDist && cent.x - pos > maxDist) {
                minNegDist = (int) (cent.x - pos);
            }
        }
        tl.x = cent.x - minNegDist;
        br.x = cent.x + minPosDist;
        minPosDist = img.width()/2;
        minNegDist = img.width()/2;
        for (Point[] line : horis) {
            int pos = (int)line[0].y;
            if (pos > cent.y && pos < cent.y + minPosDist && pos - cent.y > maxDist) {
                minPosDist = (int) (pos - cent.y);
            } else if (pos < cent.y && pos > cent.y - minNegDist && cent.y - pos > maxDist) {
                minNegDist = (int) (cent.y - pos);
            }
        }
        tl.y = cent.y - minNegDist;
        br.y = cent.y + minPosDist;

        return new Point[]{tl, br};
    }

    private Mat BlueCrop(Mat img) {
//        Imgproc.cvtColor(img, img, COLOR_RGB2HSV);
        ArrayList<Mat> planes = new ArrayList<>(3);
        split(img,planes);  // planes[2] is the red channel
        return planes.get(0);
    }
}
