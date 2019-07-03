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
import static org.opencv.imgcodecs.Imgcodecs.imread;
import static org.opencv.imgcodecs.Imgcodecs.imwrite;
import static org.opencv.imgproc.Imgproc.CHAIN_APPROX_SIMPLE;
import static org.opencv.imgproc.Imgproc.COLOR_BGR2GRAY;
import static org.opencv.imgproc.Imgproc.Canny;
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
    private Bitmap btm;
    String path;
    public ImageProcessor(File img) {
        //OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_2_4_11, this, baseLoaderCallback );

        Mat src = new Mat();
        src = imread(img.getAbsolutePath());
        Mat out = new Mat();
        out = imread(img.getAbsolutePath());

//        Point a,b;
//        a = new Point(src.rows()/6,src.cols()/4);
//        b = new Point(src.rows()*2/3, src.cols()*13/16);
//        Rect rectCropa = new Rect((int)a.x, (int)a.y , (int)(b.x-a.x+1), (int)(b.y-a.y+1));
//        src = src.submat(rectCropa);


        //70x125
        //70x95
        //bitmapToMat(m,m,false);
        /*
        for (int i = 10; i <= 50; i+=10) {
            for (int j = 15; j <= 55; j+=10) {
                Canny(src, out, i, j, false);
                path = img.getAbsolutePath().substring(0, img.getAbsolutePath().lastIndexOf(".jpg")) + ":" + i + "x" + j + "Gray_Image.bmp";
                imwrite(path, out);
            }
        }
        */
        path = img.getAbsolutePath().substring(0, img.getAbsolutePath().lastIndexOf(".jpg")) + ":" + "corn.bmp";
        Point[] corns = detectSquare(src,path);
        Boolean cropped = false;
        if (corns.length != 2){
            Log.d(TAG,"OOPS");
        } else {
            Log.d(TAG,corns.toString());
            Point a = corns[0];
            Point b = corns[1];
            Rect rectCrop = new Rect(a, b);
            out = out.submat(rectCrop);
            cropped = true;
        }
        //Circle Detection
        Mat gray = new Mat();
        Imgproc.cvtColor(out, gray, COLOR_BGR2GRAY);
        Log.d(TAG,"type1:" + out.type());
        Log.d(TAG,"type2:" + gray.type());
        equalizeHist(gray,gray);
        //Canny(gray, out,20,25,false);
        path = img.getAbsolutePath().substring(0, img.getAbsolutePath().lastIndexOf(".jpg")) + ":" + "new.bmp";
        imwrite(path, gray);
        Log.d(TAG,"written");
        Mat edge = new Mat();
        Point top = new Point(gray.width()*2/9,gray.height()*1/5);
        Point bot = new Point(gray.width()*7/9,gray.height()*7/10);
        Rect crop = new Rect(top, bot);

        edge = out.clone();//edge = out.submat(crop);
        Imgproc.cvtColor(edge, edge, COLOR_BGR2GRAY);

        Log.d(TAG,"type1:" + out.type());
        Log.d(TAG,"type2:" + edge.type());
        //equalizeHist(edge,edge);
        path = img.getAbsolutePath().substring(0, img.getAbsolutePath().lastIndexOf(".jpg")) + ":" + ":helpme.bmp";
        imwrite(path, edge);
        Mat circles = new Mat();
//        Imgproc.HoughCircles(edge, circles, Imgproc.HOUGH_GRADIENT, 1.0,
//                (double) gray.rows() / 16, // change this value to detect circles with different distances to each other
//                20.0, 25.0, gray.cols()/6, gray.cols()/3);
        for (int i = 0; i <= 60; i+=10) {
            for (int j = 5; j <= 65; j += 10) {
//                Canny(edge, circles, i, j, false);
//                path = img.getAbsolutePath().substring(0, img.getAbsolutePath().lastIndexOf(".jpg")) + ":"+i+"x"+j + ":edges.bmp";
//                imwrite(path, circles);
            }
        }

        //Imgproc.medianBlur(gray, gray, 5);
        /*
        Mat circles = new Mat();
        //for (int i = 0; circles.get(0,0) == null && i < 60; i += 10) {
            Log.d(TAG,"startCirc");
            Imgproc.HoughCircles(gray, circles, Imgproc.HOUGH_GRADIENT, 1.0,
                    (double) gray.rows() / 16, // change this value to detect circles with different distances to each other
                    20.0, 25.0, gray.cols()/6, gray.cols()/3); // change the last two parameters
            Log.d(TAG,"end");
        //}

        // (min_radius & max_radius) to detect larger circles
        for (int x = 0; x < circles.cols(); x++) {
            double[] c = circles.get(0, x);
            Point center = new Point(Math.round(c[0]), Math.round(c[1]));
            // circle center
            Imgproc.circle(src, center, 1, new Scalar(0,100,100), 2, 8, 0 );
            // circle outline
            int radius = (int) Math.round(c[2]);
            Imgproc.circle(src, center, radius, new Scalar(255,0,255), 2, 8, 0 );
        }
        path = img.getAbsolutePath().substring(0, img.getAbsolutePath().lastIndexOf(".jpg")) + ":" + "circs.bmp";
        imwrite(path, src);

        //Informed crop
        Point p1,p4;
        double[] circle = circles.get(0,0);
        Point center = new Point(Math.round(circle[0]), Math.round(circle[1]));
        int rad = (int) Math.round(circle[2]);
        p1 = new Point(center.x-rad,center.y-rad);
        p4 = new Point(center.x+rad,center.y+rad);
        Rect rectCrop = new Rect((int)p1.x, (int)p1.y , (int)(p4.x-p1.x+1), (int)(p4.y-p1.y+1));
        src = src.submat(rectCrop);
        */
        Imgproc.cvtColor(out, gray, Imgproc.COLOR_BGR2GRAY);
        equalizeHist(gray,out);
        //Mat comb = gray;
        //add(gray,src,comb);

        //Canny(src, src, 100, 60, false);

        path = img.getAbsolutePath().substring(0, img.getAbsolutePath().lastIndexOf(".jpg")) + ":" + "Circle.bmp";
        imwrite(path, out);
        Log.d(TAG,path);
//        int j = 260;
//        for (int i = j; i <= j+70; i += 10) {
//            Canny(src, src, i, i+40, false);
//            path = img.getAbsolutePath().substring(0, img.getAbsolutePath().lastIndexOf(".jpg")) + ":" +i+ "edge.bmp";
//            imwrite(path, src);
//        }
        btm = BitmapFactory.decodeFile(img.getAbsolutePath());
        src = imread(img.getAbsolutePath());
        //Utils.matToBitmap(src, btm);
    }
    public String toString() {
        return path;
    }

    public Bitmap getBtm() {
        return btm;
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
        Mat lines = new Mat();
        HoughLinesP(gray, lines, 1,Math.PI/180,85,minLineLength,maxLineGap);
        lines = linesMerge(lines);
        for (int i = 0; i < lines.rows(); i++) {
            double[] lin = lines.get(i,0);
            line(resized, new Point(lin[0], lin[1]), new Point(lin[2], lin[3]), new Scalar(0, 255, 0));
        }
        //threshold(gray,gray,180,255,THRESH_BINARY);

        imwrite(path, resized);
        path = path + "2.bmp";
        List<MatOfPoint> conts = new ArrayList<>();
        Mat hierarchy = new Mat();
        findContours(gray,conts,hierarchy,RETR_EXTERNAL,CHAIN_APPROX_SIMPLE);
        Log.d(TAG,"Start Looking");
        Mat circles = new Mat();
//        Imgproc.HoughCircles(gray, circles, Imgproc.HOUGH_GRADIENT, 1.0,
//                1, // change this value to detect circles with different distances to each other
//                1, 150, 0, 0); // change the last two parameters
//        Log.d(TAG,"end");


        // (min_radius & max_radius) to detect larger circles
//        for (int x = 0; x < circles.cols(); x++) {
//            double[] c = circles.get(0, x);
//            Point center = new Point(Math.round(c[0]), Math.round(c[1]));
//            // circle center
//            Imgproc.circle(resized, center, 1, new Scalar(0,100,100), 3, 8, 0 );
//            // circle outline
//            int radius = (int) Math.round(c[2]);
//            Imgproc.circle(resized, center, radius, new Scalar(255,0,255), 3, 8, 0 );
//        }
        for (MatOfPoint cur : conts) {
            MatOfPoint2f curve = new MatOfPoint2f(cur.toArray());
            double perimiter = arcLength(curve, true);
            //Log.d(TAG,perimiter + "");
            approxPolyDP(curve, curve, 0.04 * perimiter, true);
            MatOfPoint approx = new MatOfPoint(curve.toArray());

            if (perimiter >= 200/*curve.toArray().length == 4*/) {
                Rect bounds = boundingRect(approx);
                float ar = 1.0f * bounds.width / bounds.height;
                if (ar < 1.8f && ar > .55f) {
                    Point cent = new Point(bounds.x + bounds.width / 2, bounds.y + bounds.height / 2);
                    if (bounds.width < resized.width()*6/8 && cent.x > resized.width()/4 && cent.x < resized.width()*3/4 && cent.y > resized.height()/4 && cent.y < resized.height()*3/4) {
                        points.add(new Point(bounds.x, bounds.y));
                        points.add(new Point(bounds.x + bounds.width, bounds.y + bounds.height));
                        Imgproc.circle(resized, cent, bounds.width / 2, new Scalar(255, 0, 255), 3, 8, 0);
                        Imgproc.circle(resized, cent, bounds.height / 2, new Scalar(255, 0, 255), 3, 8, 0);

                    }
                }
            }
        }
        double avgtlx = 0;
        double avgtly = 0;
        double avgbrx = 0;
        double avgbry = 0;
        int count = 0;
        while (points.size() > 1) {
            Point tl = points.remove(0);
            Point br = points.remove(0);
            avgtlx += tl.x;
            avgtly += tl.y;
            avgbrx += br.x;
            avgbry += br.y;
            count++;
        }
        avgtlx /= count;
        avgtly /= count;
        avgbrx /= count;
        avgbry /= count;
        //TODO weight based on center to img center dist
        avgtlx /= ratio;
        avgtly /= ratio;
        avgbrx /= ratio;
        avgbry /= ratio;
        Point tl = new Point(avgtlx, avgtly);
        Point br = new Point(avgbrx, avgbry);
        Log.d(TAG,ratio + "ra");
        Log.d(TAG,resized.width() + "re");
        Log.d(TAG,img.width() + "im");
        Imgproc.circle(resized, tl, 2, new Scalar(255, 0, 255), 3, 8, 0);
        Imgproc.circle(resized, br, 2, new Scalar(255, 0, 255), 3, 8, 0);
        path = path + "1.bmp";
        imwrite(path, resized);
        Point[] retVal = {tl,br};
        return retVal;
    }

    private Mat linesMerge(Mat lines){
        List<double[]> verts = new ArrayList<>();
        List<double[]> horis = new ArrayList<>();
        for (int i = 0; i < lines.rows(); i++) {
            for (int j = 0; j < lines.cols(); j++) {
                Log.d(TAG, "lines@"+i+", "+j+": " + lines.get(i,j).toString());
                double[] re =  lines.get(i,j);
                double x1,y1,x2,y2;
                x1 = re[0];
                y1 = re[1];
                x2 = re[2];
                y2 = re[3];
                Point[] line = {new Point(x1,y1),new Point(x2,y2)};

                if (x2 == x1) {
                    //vertical
                    verts.add(re);
                } else {
                    double slope = (y2 - y1) / (x2 / x1);
                    if (slope > 11 || slope < -11) {
                        //vertical
                        verts.add(re);
                    }else if (slope < 1.0/11 && slope > -1.0/11) {
                        //horizontal
                        horis.add(re);
                    }
                }
//                if (verts.contains(re) || horis.contains(re))
//                    line(resized,new Point(x1,y1),new Point(x2,y2), new Scalar(255, 0, 0));
            }
        }
        for (int i = 0; i < verts.size() - 1; i++) {
            for (int j = i + 1; j < verts.size(); j++) {
                double[] a = verts.get(i);
                double[] b = verts.get(j);
                double ax1,ay1,ax2,ay2, bx1,by1,bx2,by2;
                ax1 = a[0];
                ay1 = a[1];
                ax2 = a[2];
                ay2 = a[3];
                bx1 = b[0];
                by1 = b[1];
                bx2 = b[2];
                by2 = b[3];
//                double invSlopeA = (ax2 / ax1) / (ay2 - ay1);
//                double invSlopeB = (bx2 / bx1) / (by2 - by1);
                double avgA = ax1+ax2/2;
                double avgB = bx1+bx2/2;
                if (Math.abs(avgA - avgB) < 10) {
                    //merge
                    double maxY = Math.max(Math.max(ay1,ay2),Math.max(by1,by2));
                    double minY = Math.min(Math.min(ay1,ay2),Math.min(by1,by2));
                    double lenA = Math.abs(ay1 - ay2);
                    double lenB = Math.abs(by1 - by2);
                    double avgX = (avgA * lenA + avgB * lenB) / (lenA + lenB);
                    double[] newLine = {avgX, maxY, avgX, minY};
                    verts.remove(a);
                    verts.remove(b);
                    verts.add(newLine);
                }
            }
        }
        for (int i = 0; i < horis.size() - 1; i++) {
            for (int j = i + 1; j < horis.size(); j++) {
                double[] a = horis.get(i);
                double[] b = horis.get(j);
                double ax1,ay1,ax2,ay2, bx1,by1,bx2,by2;
                ax1 = a[0];
                ay1 = a[1];
                ax2 = a[2];
                ay2 = a[3];
                bx1 = b[0];
                by1 = b[1];
                bx2 = b[2];
                by2 = b[3];
//                double invSlopeA = (ax2 / ax1) / (ay2 - ay1);
//                double invSlopeB = (bx2 / bx1) / (by2 - by1);
                double avgA = ay1+ay2/2;
                double avgB = by1+by2/2;
                if (Math.abs(avgA - avgB) < 10) {
                    //merge
                    double maxX = Math.max(Math.max(ax1,ax2),Math.max(bx1,bx2));
                    double minX = Math.min(Math.min(ax1,ax2),Math.min(bx1,bx2));
                    double lenA = Math.abs(ax1 - ax2);
                    double lenB = Math.abs(bx1 - bx2);
                    double avgY = (avgA * lenA + avgB * lenB) / (lenA + lenB);
                    double[] newLine = {avgY, maxX, avgY, minX};
                    verts.remove(a);
                    verts.remove(b);
                    verts.add(newLine);
                }
            }
        }
        int i = 0;
        Mat retVal = new Mat();
        for (double[] line : verts) {
            retVal.put(i,0,line);
            i++;
        }
        for (double[] line : horis) {
            retVal.put(i,0,line);
            i++;
        }
        return retVal;
    }
}
