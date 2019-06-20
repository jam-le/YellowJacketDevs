package com.example.readyourresults.Preprocessing;

//import java.awt.Color;
//import java.awt.FlowLayout;
//import java.awt.Image;
//import java.awt.image.BufferedImage;
//import java.io.File;
//import java.util.ArrayList;
//
//import javax.imageio.ImageIO;
//import javax.swing.ImageIcon;
//import javax.swing.JFrame;
//import javax.swing.JLabel;

public class ImageEditor {

    public ImageEditor() {
        super();
    }

//    /*****************************   Tools   **********************************/
//    private static int clip(int x, int min, int max) {
//        return (x<min)? min : (x>max)? max : x;
//    }
//    private static int clip(int x) {
//        return (x<0)? 0 : (x>255)? 255 : x;
//    }
//    private static int intensity(int rgb) {
//        Color c = new Color(rgb);
//        int r = c.getRed();
//        int g = c.getGreen();
//        int b = c.getBlue();
//        int ans =(int) ((r+g+b) / 3);
//        if (ans > 255 || ans < 0) System.out.println("hold up");
//        //if(ans>100)System.out.println(ans);
//        return ans;
//    }
//    private static int[] rgb(int rgb) {
//        int[] ans = new int[3];
//        Color c = new Color(rgb);
//        ans[0] = c.getRed();
//        ans[1] = c.getGreen();
//        ans[2] = c.getBlue();
//        return ans;
//    }
//
//    /****************************   Methods   *********************************/
//    private static BufferedImage colorDetection(BufferedImage img) {
//        int width = img.getWidth();
//        int height = img.getHeight();
//        BufferedImage ans = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
//        int[] old = img.getRGB(0, 0, width, height, null, 0, width);
//        ans.setRGB(0, 0, width, height, old, 0, width);
//
//        for (int x = 1; x<ans.getWidth()-1;x++) {
//            for (int y = 1; y < ans.getHeight()-1; y++) {
//                Color myColor = new Color(ans.getRGB(x, y));
//
//                if (myColor.getBlue() > myColor.getRed()
//                    && myColor.getBlue() > 1.5*myColor.getGreen()) {
//
//                    ans.setRGB(x, y, Color.RED.getRGB());
//                    myColor = Color.RED;
//                }
//                if (myColor.getBlue() > myColor.getRed()
//                    && myColor.getBlue() > .5 * myColor.getGreen()
//                    && x > .4 * ans.getWidth()) {
//
//                    ans.setRGB(x, y, Color.GREEN.getRGB());
//                }
//            }
//        }
//        return ans;
//
//    }
//    private static BufferedImage blur(BufferedImage img) {
//        int width = img.getWidth();
//        int height = img.getHeight();
//        BufferedImage ans = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
//        int[] old = img.getRGB(0, 0, width, height, null, 0, width);
//        ans.setRGB(0, 0, width, height, old, 0, width);
//
//        double[][] kernel = {
//            {1.0 / 256,  4.0 / 256,  6.0 / 256,  4.0 / 256, 1.0 / 256},
//            {4.0 / 256, 16.0 / 256, 24.0 / 256, 16.0 / 256, 4.0 / 256},
//            {6.0 / 256, 24.0 / 256, 36.0 / 256, 24.0 / 256, 6.0 / 256},
//            {4.0 / 256, 16.0 / 256, 24.0 / 256, 16.0 / 256, 4.0 / 256},
//            {1.0 / 256,  4.0 / 256,  6.0 / 256,  4.0 / 256, 1.0 / 256}
//        };
//        int offset = kernel.length / 2;
//
//        for (int x = 0; x < width; x++) {
//            for (int y = 0; y < height; y++) {
//                double acc = 0.0;
//                for (int a = 0; a < 5; a++) {
//                    for (int b = 0; b < 5; b++) {
//                        //System.out.println(kernel[b][a]);
//                        int xn = clip(x + a - offset, 0, width - 1);
//                        int yn = clip(y + b - offset, 0, height - 1);
//                        acc += intensity(img.getRGB(xn, yn)) * kernel[b][a];
//                    }
//                }
//                int avg = (int) acc;
//                Color c = new Color(avg, avg, avg);
//                ans.setRGB(x, y, c.getRGB());
//            }
//        }
//        return ans;
//    }
//    private static BufferedImage edgeDetection(BufferedImage img) {
//        int width = img.getWidth();
//        int height = img.getHeight();
//        BufferedImage ans = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
//        int[] old = img.getRGB(0, 0, width, height, null, 0, width);
//        ans.setRGB(0, 0, width, height, old, 0, width);
//        int[][] pic =new int[width - 1][height - 1];
//
//        for (int x = 1; x<ans.getWidth()-1;x++) {
//            for (int y = 1; y < ans.getHeight()-1; y++) {
//                Color e = new Color(ans.getRGB(x, y));
//
//                int diffx = (intensity(ans.getRGB(x - 1, y)) - intensity(ans.getRGB(x + 1, y)));
//                int diffy = (intensity(ans.getRGB(x, y - 1)) - intensity(ans.getRGB(x, y + 1)));
//                int diff = (int)Math.sqrt(Math.pow(diffx, 2) + Math.pow(diffy, 2));
//                //diff *= 255;
//                //diff /= Math.sqrt(Math.pow(255,2) * 2);
//                //System.out.println(diff);
//                //e = (diff>thresh)? Color.BLACK:Color.WHITE;
//                e = new Color(diff, diff, diff);
//                for (int i = 0; i < 6; i++) {
//                    e = e.brighter();
//                }
//                /*if (intensity(e.getRGB()) > 55) {
//                    e = Color.BLACK;
//                } else {
//                    e = Color.WHITE;
//                }*/
//                pic[x][y] = e.getRGB();
//            }
//        }
//        for (int x = 1; x<ans.getWidth()-1;x++) {
//            for (int y = 1; y < ans.getHeight()-1; y++) {
//                ans.setRGB(x,y,pic[x-1][y-1]);
//            }
//        }
//        return ans;
//
//    }
//    private static BufferedImage greyScale(BufferedImage img) {
//        int width = img.getWidth();
//        int height = img.getHeight();
//        BufferedImage ans = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
//        int[] old = img.getRGB(0, 0, width, height, null, 0, width);
//        ans.setRGB(0, 0, width, height, old, 0, width);
//
//        for (int i = 0; i < width; i++) {
//            for (int j = 0; j < height; j++) {
//                //Color c = new Color(ans.getRGB(i, j));
//                int[] rgb = rgb(ans.getRGB(i, j));
//                int r = rgb[0];
//                int g = rgb[1];
//                int b = rgb[2];
//                // b = (b*2 > 255)? 255 : b*2;
//                // r = (b-r < 0)? 0:b-r;
//                // g = (b-g < 0)? 0:b-g;
//                int avg = intensity(ans.getRGB(i, j));
//                Color c = new Color(avg, avg, avg);
//                ans.setRGB(i, j, c.getRGB());
//            }
//        }
//        return ans;
//    }
//    private static BufferedImage minus(BufferedImage a, BufferedImage b) {
//        int width = a.getWidth();
//        int height = a.getHeight();
//        BufferedImage ans = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
//        int[] old = a.getRGB(0, 0, width, height, null, 0, width);
//        ans.setRGB(0, 0, width, height, old, 0, width);
//        for (int i = 0; i < width; i++) {
//            for (int j = 0; j < height; j++) {
//                int d = Math.abs(
//                    intensity(a.getRGB(i, j))
//                    - intensity(b.getRGB(i, j)));
//                ans.setRGB(i,j,new Color(d,d,d).brighter().brighter().brighter().brighter().brighter().brighter().brighter().brighter().brighter().getRGB());
//            }
//        }
//        return ans;
//    }
//    private static double[][][] gradient(BufferedImage img) {
//
//        int width = img.getWidth();
//        int height = img.getHeight();
//        double[][] gradient = new double[width][height];
//        double[][] direction = new double[width][height];
//        for (int x = 0; x < width; x++) {
//            for (int y = 0; y < height; y++) {
//                if ((0 < x && x < width - 1) && (0 < y && y < height - 1)){
//                    double magx = (intensity(img.getRGB(x - 1, y)) - intensity(img.getRGB(x + 1, y)));
//                    double magy = (intensity(img.getRGB(x, y - 1)) - intensity(img.getRGB(x, y + 1)));
//                    double diff = Math.sqrt(Math.pow(magx, 2) + Math.pow(magy, 2));
//                    gradient[x][y] = diff;
//                    direction[x][y] = Math.atan(magy/ magx);
//                }
//            }
//        }
//        double[][][] ans = new double[2][width][height];
//        for (int x = 0; x < width; x++) {
//            for (int y = 0; y < height; y++) {
//                ans[0][x][y] = gradient[x][y];
//                ans[1][x][y] = direction[x][y];
//            }
//        }
//        return ans;
//
//
//    }
//
//    private static double[][] filter_out_non_maximum(double [][] gradient, double[][] direction, int width, int height) {
//        for (int x = 1; x < width - 1; x++) {
//            for (int y = 1; y < height - 1; y++) {
//                double angle = (direction[x][y] >= 0)? direction[x][y] : direction[x][y] + Math.PI;
//                int rangle = (int)(angle / (Math.PI / 4));
//                double mag = gradient[x][y];
//                if ((rangle == 0 || rangle == 4) && (gradient[x - 1][y] > mag || gradient[x + 1][y] > mag)
//                        || (rangle == 1 && (gradient[x - 1][y - 1] > mag || gradient[x + 1][y + 1] > mag))
//                        || (rangle == 2 && (gradient[x][y - 1] > mag || gradient[x][y + 1] > mag))
//                        || (rangle == 3 && (gradient[x + 1][y - 1] > mag || gradient[x - 1][y + 1] > mag))) {
//                    gradient[x][y] = 0;
//                }
//            }
//        }
//        return gradient;
//    }
//
//
//    /*private static BufferedImage filter_strong_edges(double [][] gradient, int width, int height, int low, int high) {
//        // Keep strong edges
//        keep = set()
//        for x in range(width):
//            for y in range(height):
//                if gradient[x, y] > high:
//                    keep.add((x, y))
//
//        // Keep weak edges next to a pixel to keep
//        lastiter = keep
//        while lastiter:
//            newkeep = set()
//            for x, y in lastiter:
//                for a, b in ((-1, -1), (-1, 0), (-1, 1), (0, -1), (0, 1), (1, -1), (1, 0), (1, 1)):
//                    if gradient[x + a, y + b] > low and (x+a, y+b) not in keep:
//                        newkeep.add((x+a, y+b))
//            keep.update(newkeep)
//            print(len(newkeep))
//            lastiter = newkeep
//
//        return list(keep)
//    }
//
//    private static BufferedImage canny_edge_detector(BufferedImage img) {
//        int width = img.getWidth();
//        int height = img.getHeight();
//        BufferedImage ans = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
//        int[] old = img.getRGB(0, 0, width, height, null, 0, width);
//        ans.setRGB(0, 0, width, height, old, 0, width);
//        System.out.println("greyScale");
//        // Transform the image to grayscale
//        BufferedImage grayscaled = greyScale(img);
//        System.out.println("blur");
//
//        // Blur it to remove noise
//        BufferedImage blurred = blur(grayscaled);
//        System.out.println("gradient");
//
//        // Compute the gradient
//        double[][][] gradAndDir = gradient(blurred);
//        double[][] gradient = gradAndDir[0];
//        double[][] direction = gradAndDir[1];
//        System.out.println("filter_out_non_maximum");
//
//        // Non-maximum suppression
//        gradient = filter_out_non_maximum(gradient, direction, width, height);
//        System.out.println("filter_strong_edges");
//
//        // Filter out some edges
//        BufferedImage keep = filter_strong_edges(gradient, width, height, 20, 25);
//
//        return keep;
//    }*/
//    private static BufferedImage highContrast(BufferedImage img) {
//        int width = img.getWidth();
//        int height = img.getHeight();
//        BufferedImage ans = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
//        int[] old = img.getRGB(0, 0, width, height, null, 0, width);
//        ans.setRGB(0, 0, width, height, old, 0, width);
//
//        for (int x = 0; x < width; x++) {
//            for (int y = 0; y < height; y++) {
//                Color c = new Color(img.getRGB(x, y));
//                if (intensity(img.getRGB(x, y)) > (256/2)) {
//                    c = c.brighter();
//                } else {
//                    c = c.darker();
//                }
//                ans.setRGB(x, y, c.getRGB());
//            }
//        }
//        return ans;
//    }
//
//    public static void main(String[] args) {
//        String file = "";
//
//        try {
//            file = args[0];
//        } catch (IndexOutOfBoundsException e) {
//            System.out.print("please enter an image file argument");
//            return;
//        }
//
//        try {
//            BufferedImage img = ImageIO.read(new File(file));
//            //BufferedImage grey = greyScale(img);
//            BufferedImage img1 = greyScale(img);
//
//            BufferedImage img2 = blur(img);
//
//            BufferedImage diff = highContrast(img);//canny_edge_detector(img);
//            //Color c = new Color(0, 0, 0);
//            //System.out.println(intensity(c.getRGB()));
//
//            // retrieve image
//            BufferedImage bi = diff;
//            File outputfile = new File("saved.png");
//            ImageIO.write(bi, "png", outputfile);
//
//            ImageIcon icon1=new ImageIcon(diff.getScaledInstance(
//                (int)(img.getWidth()*.6), (int)(img.getHeight()*.6), Image.SCALE_DEFAULT));
//            JFrame frame=new JFrame();
//            frame.setLayout(new FlowLayout());
//            frame.setSize(1000,1000);
//            JLabel lbl=new JLabel();
//            lbl.setIcon(icon1);
//            frame.add(lbl);
//            frame.setVisible(true);
//            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//
//        //BufferedImage img = ImageIO.read(new File(file));
//        } catch (Exception e) {
//            //TODO: handle exception
//            e.printStackTrace();
//        }
//
//    }
}