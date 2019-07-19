package com.example.readyourresults.Camera;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.CameraX;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageAnalysisConfig;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureConfig;
import androidx.camera.core.ImageProxy;
import androidx.camera.core.Preview;
import androidx.camera.core.PreviewConfig;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Rational;
import android.util.Size;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.example.readyourresults.AnalysisModel;
import com.example.readyourresults.BufferActivity;
import com.example.readyourresults.Preprocessing.ImageProcessor;
import com.example.readyourresults.R;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class CamActivity extends AppCompatActivity implements LifecycleOwner, ResultsInterpreted {
    private final int REQUEST_CODE_PERMISSIONS = 10;
    private final String[] REQUIRED_PERMISSIONS = new String[1];
    private String TAG = "CamActivity";
    String testName;
    Activity thisActivity = this;
    Intent intent;
    ProgressBar progressBar;
    TextView analyzingText;
    float maxConfidence;
    String maxLabel;

    public void resultsInterpreted(HashMap<String, Float> labelConfidences) {
        String formattedLabels;
        if (labelConfidences.isEmpty()) {
            formattedLabels = "Could not determine a result based on the provided image. An inconclusive result is commonly caused by poor image quality factors such as bad lighting or blurriness.";
        } else {
            formattedLabels = formatLabels(labelConfidences);
        }
        intent.putExtra("hash", labelConfidences);
        intent.putExtra("RESULTS_AND_CONFIDENCES", formattedLabels);
        progressBar.setVisibility(View.GONE);
        analyzingText.setVisibility(View.GONE);
    }

    public void resultsInterpreted(HashMap<String, Float> labelConfidences, String maxLabel) {
        String formattedLabels;
        if (labelConfidences.isEmpty()) {
            formattedLabels = "Could not determine a result based on the provided image. An inconclusive result is commonly caused by poor image quality factors such as bad lighting or blurriness.";
        } else {
            formattedLabels = formatLabels(labelConfidences);
        }
        intent.putExtra("hash", labelConfidences);
        intent.putExtra("RESULTS_AND_CONFIDENCES", formattedLabels);
        intent.putExtra("MAXLABEL", maxLabel);
        progressBar.setVisibility(View.GONE);
        analyzingText.setVisibility(View.GONE);
    }

    public void resultsInterpreted(HashMap<String, Float> labelConfidences, String maxLabel, float maxConfidence) {
        String formattedLabels;
        if (labelConfidences.isEmpty()) {
            formattedLabels = "Could not determine a result based on the provided image. An inconclusive result is commonly caused by poor image quality factors such as bad lighting or blurriness.";
        } else {
            formattedLabels = formatLabels(labelConfidences);
        }
        intent.putExtra("hash", labelConfidences);
        intent.putExtra("RESULTS_AND_CONFIDENCES", formattedLabels);
        intent.putExtra("MAXLABEL", maxLabel);
        intent.putExtra("MAXCONFIDENCE", maxConfidence);
        this.maxConfidence = maxConfidence;
        this.maxLabel =  maxLabel;
        progressBar.setVisibility(View.GONE);
        analyzingText.setVisibility(View.GONE);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cam);
        REQUIRED_PERMISSIONS[0] = Manifest.permission.CAMERA;

        viewFinder = findViewById(R.id.view_finder);
        // Request camera permissions
        if (allPermissionsGranted()) {
            viewFinder.post(new StartCameraRunnable());
            // Inflate the guiding overlay view
            overlayView = View.inflate(getApplicationContext(), R.layout.overlay_view, (ViewGroup) viewFinder.getParent());
        } else {
            ActivityCompat.requestPermissions(
                    this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS);
        }
        getIntent().getSerializableExtra("Test Name");
        intent = new Intent(getApplicationContext(), BufferActivity.class);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        analyzingText = (TextView) findViewById(R.id.analyzing_text);

        // Every time the provided texture view changes, recompute layout
        //viewFinder.addOnLayoutChangeListener {
                //updateTransform();
        //}
    }

    private class StartCameraRunnable implements Runnable {
        @Override
        public void run() {
            startCamera();
        }
    }

    private TextureView viewFinder;
    View overlayView;


    ImageCapture imageCapture;
    private void startCamera() {
        // TODO: Implement CameraX operations
        // Create configuration object for the viewfinder use case

        Log.d(TAG, "startCamera() called.");

        PreviewConfig previewConfig = new PreviewConfig.Builder()
                .setTargetAspectRatio(new Rational(1, 1))
                .setTargetResolution(new Size(640, 640))
                .build();

        // Build the viewfinder use case
        Preview preview = new Preview(previewConfig);

        // Every time the viewfinder is updated, recompute layout
        preview.setOnPreviewOutputUpdateListener(new Preview.OnPreviewOutputUpdateListener() {
            @Override
            public void onUpdated(Preview.PreviewOutput output) {
                // To update the SurfaceTexture, we have to remove it and re-add it
                ViewGroup parent = (ViewGroup) viewFinder.getParent();
                parent.removeView(viewFinder);
                parent.addView(viewFinder, 0);

                viewFinder.setSurfaceTexture(output.getSurfaceTexture());

                updateTransform();
            }
        });

        ImageCaptureConfig config =
                new ImageCaptureConfig.Builder()
                        .setTargetRotation(getWindowManager().getDefaultDisplay().getRotation())
                        .build();

        imageCapture = new ImageCapture(config);
        testName = getIntent().getStringExtra("Test Type");
        Button captureImageButton = overlayView.findViewById(R.id.capture_image_button);
        captureImageButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        File directory = new File(getExternalMediaDirs()[0] + "/RYR");
                        directory.mkdir();
                        File file = new File(directory, System.currentTimeMillis() + ".jpg");

                        imageCapture.takePicture(file, new ImageCapture.OnImageSavedListener() {
                            @Override
                            public void onImageSaved(File file) {
                                Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                                Uri contentUri = Uri.fromFile(file);
                                mediaScanIntent.setData(contentUri);
                                sendBroadcast(mediaScanIntent);

                                Bitmap bitmapImage = BitmapFactory.decodeFile(file.getAbsolutePath());

                                String msg = "Photo capture succeeded: " + file.getAbsolutePath();
                                Log.d("CameraXApp", msg);

                                // TODO: Results processing dialog should go here

                                // TODO: Process Image
                                ImageProcessor imp = new ImageProcessor(file);
                                Toast.makeText(CamActivity.this, imp.toString(),
                                        Toast.LENGTH_LONG).show();
                                AnalysisModel model = new AnalysisModel(bitmapImage, getApplicationContext(),(ResultsInterpreted) thisActivity);

                                // TODO: Create conditional code that directs user
                                // to buffer activity screen only if image processing
                                // completes successfully

                                // Create dialog that alerts user when result
                                // analysis is complete
                                // double-clicking creates a problem of loading two screens
                                intent = new Intent(getApplicationContext(), BufferActivity.class);
                                intent.putExtra("IMAGE_SUCCESSFULLY_CAPTURED", msg);
                                intent.putExtra("Test Type", testName);
                                intent.putExtra("Image Path", "" + file.getAbsoluteFile());

                                // start progress dialog
                                progressBar.setVisibility(View.VISIBLE);
                                analyzingText.setVisibility(View.VISIBLE);
                                model.interpret();
                                startActivity(intent);
                                finish();
                            }
                            @Override
                            public void onError(
                                    ImageCapture.UseCaseError useCaseError,
                                    String message,
                                    Throwable cause) {
                                // insert your code here.
                                Log.e(TAG, "Error occurred in startCamera()");

                            }
                        });
                    }
                });

        ImageAnalysisConfig analysisConfig =
                new ImageAnalysisConfig.Builder()
                        .setTargetResolution(new Size(1280, 720))
                        .setImageReaderMode(
                            ImageAnalysis.ImageReaderMode.ACQUIRE_LATEST_IMAGE)
                        .build();

        ImageAnalysis imageAnalysis = new ImageAnalysis(analysisConfig);

        imageAnalysis.setAnalyzer(new TestDeviceAnalyzer());


        // Bind use cases to lifecycle
        // If Android Studio complains about "this" being not a LifecycleOwner
        // try rebuilding the project or updating the appcompat dependency to
        // version 1.1.0 or higher.
        CameraX.bindToLifecycle(this, preview, imageCapture, imageAnalysis);


    }

    class TestDeviceAnalyzer implements ImageAnalysis.Analyzer {
        private long lastAnalyzedTimestamp = 0L;

        @Override
        public void analyze(ImageProxy image, int rotationDegrees) {
            long currentTimestamp = System.currentTimeMillis();
            // Calculate the confidences no more often than every second
            if (currentTimestamp - lastAnalyzedTimestamp >=
                    TimeUnit.SECONDS.toMillis(1)) {
                Bitmap bitmapImage = imageToBitmap(image.getImage(), rotationDegrees);

                intent = new Intent(getApplicationContext(), BufferActivity.class);
                AnalysisModel model = new AnalysisModel(bitmapImage, getApplicationContext(),(ResultsInterpreted) thisActivity);
                model.interpret();


                if (maxConfidence > .7 && !(maxLabel.equalsIgnoreCase("Inconclusive") ||  maxLabel.equalsIgnoreCase("Invalid"))) {

                    Log.d("CameraXApp", "Max confidence: " + maxConfidence);
                    // If maxConfidence of most likely label is > .95, autocapture picture.
                    File directory = new File(getExternalMediaDirs()[0] + "/RYR");
                    directory.mkdir();
                    File file = new File(directory, System.currentTimeMillis() + ".jpg");

                    imageCapture.takePicture(file, new ImageCapture.OnImageSavedListener() {
                        @Override
                        public void onImageSaved(File file) {
                            Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                            Uri contentUri = Uri.fromFile(file);
                            mediaScanIntent.setData(contentUri);
                            sendBroadcast(mediaScanIntent);

                            String msg = "Photo capture succeeded: " + file.getAbsolutePath();
                            Log.d("CameraXApp", msg);

                            intent.putExtra("IMAGE_SUCCESSFULLY_CAPTURED", msg);
                            intent.putExtra("Test Type", testName);
                            intent.putExtra("Image Path", "" + file.getAbsoluteFile());
                            startActivity(intent);
                            finish();
                        }

                        @Override
                        public void onError(
                                ImageCapture.UseCaseError useCaseError,
                                String message,
                                Throwable cause) {
                            // insert your code here.
                            Log.e(TAG, "Error occurred in startCamera()");

                        }
                    });
                }

                // Update timestamp of last analyzed frame
                lastAnalyzedTimestamp = currentTimestamp;
            }
        }
    }

    public Bitmap imageToBitmap(Image image, float rotationDegrees) {

        assert (image.getFormat() == ImageFormat.NV21);

        // NV21 is a plane of 8 bit Y values followed by interleaved  Cb Cr
        ByteBuffer ib = ByteBuffer.allocate(image.getHeight() * image.getWidth() * 2);

        ByteBuffer y = image.getPlanes()[0].getBuffer();
        ByteBuffer cr = image.getPlanes()[1].getBuffer();
        ByteBuffer cb = image.getPlanes()[2].getBuffer();
        ib.put(y);
        ib.put(cb);
        ib.put(cr);

        YuvImage yuvImage = new YuvImage(ib.array(),
                ImageFormat.NV21, image.getWidth(), image.getHeight(), null);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        yuvImage.compressToJpeg(new Rect(0, 0,
                image.getWidth(), image.getHeight()), 50, out);
        byte[] imageBytes = out.toByteArray();
        Bitmap bm = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
        Bitmap bitmap = bm;

        // On android the camera rotation and the screen rotation
        // are off by 90 degrees, so if you are capturing an image
        // in "portrait" orientation, you'll need to rotate the image.
        if (rotationDegrees != 0) {
            Matrix matrix = new Matrix();
            matrix.postRotate(rotationDegrees);
            Bitmap scaledBitmap = Bitmap.createScaledBitmap(bm,
                    bm.getWidth(), bm.getHeight(), true);
            bitmap = Bitmap.createBitmap(scaledBitmap, 0, 0,
                    scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);
        }
        return bitmap;
    }

    private String formatLabels(HashMap<String, Float> labelConfidences) {
        String formattedString = labelConfidences.toString();
        Log.d("CamActivity: ", formattedString);
        return formattedString;
    }

    public static class CustomOverlayView extends View {
        public CustomOverlayView(Context context, AttributeSet attrs) {
            super(context, attrs);
        }
        public CustomOverlayView(Context context, AttributeSet attrs, int defStyle) {
            super(context, attrs, defStyle);
        }

        Paint paint = new Paint();

        @Override
        protected void onDraw(Canvas canvas) {
            float width = getMeasuredWidth();
            float height = getMeasuredHeight();
            float corner_edge_len = 100;
            float control_indicator_len = 64;
            paint.setColor(Color.WHITE);
            paint.setStrokeWidth(50);

            //top left bracket
            canvas.drawLine(0,0,0, corner_edge_len,paint);
            canvas.drawLine(0,0,corner_edge_len, 0,paint);

            //bottom left bracket
            canvas.drawLine(0,height, corner_edge_len,height, paint);
            canvas.drawLine(0,height-corner_edge_len, 0,height, paint);

            //top right bracket
            canvas.drawLine(width-corner_edge_len, 0, width, 0, paint);
            canvas.drawLine(width, 0, width, corner_edge_len, paint);

            //bottom right bracket
            canvas.drawLine(width-corner_edge_len, height, width, height, paint);
            canvas.drawLine(width, height-corner_edge_len, width, height, paint);

            paint.setStrokeWidth(5);
            paint.setAlpha(150);
            float control_indicator_len_top = 110;
            canvas.drawRoundRect((width/2)-(control_indicator_len/2),
                    control_indicator_len_top,
                    width/2+(control_indicator_len/2)-1,
                    control_indicator_len_top + control_indicator_len, 10, 10, paint);
        }
    }

    private void updateTransform() {
        // TODO: Implement camera viewfinder transformations
    }

    /**
     * Process result from permission request dialog box, has the request
     * been granted? If yes, start Camera. Otherwise display a toast
     */
    @Override
    public void onRequestPermissionsResult(
            int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                viewFinder = findViewById(R.id.view_finder);
                viewFinder.post(new StartCameraRunnable());
                overlayView = View.inflate(getApplicationContext(), R.layout.overlay_view, (ViewGroup) viewFinder.getParent());
            } else {
                Toast.makeText(this,
                        "Permissions not granted by the user.",
                        Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    /**
     * Check if all permission specified in the manifest have been granted
     */
    private Boolean allPermissionsGranted() {
        for (String permission : REQUIRED_PERMISSIONS) {
            if (ContextCompat.checkSelfPermission(
                    this, permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }
}

