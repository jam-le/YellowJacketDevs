package com.example.readyourresults.Camera;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.CameraX;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureConfig;
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
import android.widget.Toast;

import com.example.readyourresults.AnalysisModel;
import com.example.readyourresults.BufferActivity;
import com.example.readyourresults.R;

import java.io.File;

public class CamActivity extends AppCompatActivity implements LifecycleOwner {
    private final int REQUEST_CODE_PERMISSIONS = 10;
    private final String[] REQUIRED_PERMISSIONS = new String[1];
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

    private void startCamera() {
        // TODO: Implement CameraX operations
        // Create configuration object for the viewfinder use case

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

        final ImageCapture imageCapture = new ImageCapture(config);
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
                                AnalysisModel model = new AnalysisModel(bitmapImage);
                                String label = model.interpret();

                                // TODO: Create conditional code that directs user
                                // to buffer activity screen only if image processing
                                // completes successfully

                                // TODO: Africa

                                // Create dialog that alerts user when result
                                // analysis is complete
                                // double-clicking creates a problem of loading two screens
                                Intent intent = new Intent(getApplicationContext(), BufferActivity.class);
                                intent.putExtra("IMAGE_SUCCESSFULLY_CAPTURED", msg);
                                startActivity(intent);
                            }
                            @Override
                            public void onError(
                                    ImageCapture.UseCaseError useCaseError,
                                    String message,
                                    Throwable cause) {
                                // insert your code here.

                            }
                        });
                    }
                });

        // Bind use cases to lifecycle
        // If Android Studio complains about "this" being not a LifecycleOwner
        // try rebuilding the project or updating the appcompat dependency to
        // version 1.1.0 or higher.
        CameraX.bindToLifecycle(this, preview, imageCapture);


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
                viewFinder.post(new StartCameraRunnable());
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

