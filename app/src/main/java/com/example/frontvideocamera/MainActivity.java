package com.example.frontvideocamera;

import android.Manifest;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Bundle;
import android.view.TextureView;
import android.view.View;
import android.widget.Button;
import android.widget.VideoView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class MainActivity extends AppCompatActivity {

    private Camera camera;
    private TextureView textureView;
    private VideoView videoView;
    private Button switchButton;
    private Button switchToVideoButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textureView = findViewById(R.id.textureView);
        videoView = findViewById(R.id.videoView);
        switchButton = findViewById(R.id.switchButton);
        switchToVideoButton = findViewById(R.id.switchToVideoButton);


        switchButton.setOnClickListener(v -> {
            if (videoView.isPlaying()) {
                videoView.stopPlayback();
            }
            videoView.setVisibility(android.view.View.GONE);
            textureView.setVisibility(android.view.View.VISIBLE);
            switchToVideoButton.setVisibility(View.VISIBLE);
        });

        switchToVideoButton.setOnClickListener(v -> {
            textureView.setVisibility(View.GONE);
            switchToVideoButton.setVisibility(View.GONE);
            videoView.setVisibility(View.VISIBLE);
            videoView.setVideoPath("android.resource://" + getPackageName() + "/" + R.raw.abc);
            videoView.start();
        });

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 1);
        } else {
            initializeCamera();
        }
    }

    private void initializeCamera() {
        camera = Camera.open(Camera.CameraInfo.CAMERA_FACING_FRONT);
        camera.setDisplayOrientation(90);

        // Set the preview display on the TextureView
        try {
            camera.setPreviewTexture(textureView.getSurfaceTexture());
            camera.startPreview();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Show the video when the camera preview is clicked
        textureView.setOnClickListener(v -> {
            textureView.setVisibility(android.view.View.GONE);
            videoView.setVisibility(android.view.View.VISIBLE);
            videoView.setVideoPath("android.resource://" + getPackageName() + "/" + R.raw.abc);
            videoView.start();
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            initializeCamera();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (camera != null) {
            camera.stopPreview();
            camera.release();
            camera = null;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (camera != null) {
            camera.stopPreview();
            camera.release();
            camera = null;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {
            initializeCamera();
        }
    }

}
