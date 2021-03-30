package com.houyewei.cameraxapp;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.graphics.Rect;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.util.Size;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageProxy;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.mlkit.vision.barcode.Barcode;
import com.google.mlkit.vision.barcode.BarcodeScanner;
import com.google.mlkit.vision.barcode.BarcodeScannerOptions;
import com.google.mlkit.vision.barcode.BarcodeScanning;
import com.google.mlkit.vision.common.InputImage;

import java.io.File;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@androidx.camera.core.ExperimentalGetImage
public class CameraFragment extends Fragment {

    private ImageCapture imageCapture;
    private File outputDirectory;
    private ExecutorService cameraExecutor;

    private String TAG = "CameraXBasic";
    private String FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS";
    private int REQUEST_CODE_PERMISSIONS = 10;
    private String REQUIRED_PERMISSIONS[]  = new String[]{ Manifest.permission.CAMERA };
    private PreviewView previewView;
    private Button camera_capture_button;
    private CameraSelector cameraSelector;
    private Image mediaImage;
    private TextView textView;
    private boolean isNavgating = false;
    private BarcodeViewModel viewModel;
    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.camera, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (allPermissionsGranted()) {
            startCamera();
        } else {
            ActivityCompat.requestPermissions(
                    getActivity(), REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS
            );
        }

        camera_capture_button = (Button) view.findViewById(R.id.camera_capture_button);

        cameraSelector = new CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                .build();

        textView = (TextView) view.findViewById(R.id.textView);
        previewView = (PreviewView) view.findViewById(R.id.view_finder);

        camera_capture_button.setOnClickListener(v -> {
            System.out.println("on click Listener");
            takePhoto();
        });

        outputDirectory = getOutputDirectory();
        cameraExecutor = Executors.newSingleThreadExecutor();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        MainActivity activity = (MainActivity)getActivity();
        viewModel = activity.viewModel;
    }

    private void takePhoto() {
        System.out.println("takePhoto");

    }

    @Override
    public void onResume() {
        super.onResume();
        this.isNavgating = false;
        System.out.println("resume");
    }

    @Override
    public void onPause() {
        super.onPause();
        System.out.println("pause");
    }

    private void startCamera() {
        System.out.println("startCamera");

        ListenableFuture<ProcessCameraProvider> cameraProviderFuture = ProcessCameraProvider.getInstance(getContext());

        cameraProviderFuture.addListener((Runnable) () -> {
            try {
                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                bindPreview(cameraProvider);
            } catch (ExecutionException | InterruptedException error) {

            }

        },  ContextCompat.getMainExecutor(getContext()));

    }


    private void bindPreview(ProcessCameraProvider cameraProvider) {
        Preview preview = new Preview.Builder()
                .build();



        // Connect the preview use case to the previewView
        preview.setSurfaceProvider(
                previewView.createSurfaceProvider());

        ImageAnalysis imageAnalysis =
                new ImageAnalysis.Builder()
                        .setTargetResolution(new Size(1280, 720))
                        .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                        .build();

        imageAnalysis.setAnalyzer(cameraExecutor, // Pass image to an ML Kit Vision API
                // ...
                // insert your code here.
                imageProxy-> {
                    Log.d(TAG, "analyzer");
                    System.out.println("analyzer");

                    mediaImage = imageProxy.getImage();
                    if (mediaImage != null) {
                        InputImage image =
                                InputImage.fromMediaImage(mediaImage, imageProxy.getImageInfo().getRotationDegrees());
                        // Pass image to an ML Kit Vision API
                        scanBarcodes(image, imageProxy);
                        // ...
                    }
                    // imageProxy.close();
                });
        cameraProvider.unbindAll();
        Camera camera = cameraProvider.bindToLifecycle((LifecycleOwner) this, cameraSelector, imageAnalysis, preview);
        camera.getCameraInfo();
    }

    private File getOutputDirectory() {
        return null;
    }

    private boolean allPermissionsGranted() {
        Context baseContext = getActivity().getBaseContext();
        for (String permission: REQUIRED_PERMISSIONS) {
            if(ContextCompat.checkSelfPermission(
                    baseContext, permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }



    private void scanBarcodes(InputImage image, ImageProxy imageProxy) {
        // [START set_detector_options]
        BarcodeScannerOptions options =
                new BarcodeScannerOptions.Builder()
                        .setBarcodeFormats(
                                Barcode.FORMAT_QR_CODE,
                                Barcode.FORMAT_AZTEC)
                        .build();
        // [END set_detector_options]

        // [START get_detector]
        // BarcodeScanner scanner = BarcodeScanning.getClient();
        // Or, to specify the formats to recognize:
        BarcodeScanner scanner = BarcodeScanning.getClient(options);
        // [END get_detector]

        // [START run_detector]
        Task<List<Barcode>> result = scanner.process(image)
                .addOnSuccessListener(new OnSuccessListener<List<Barcode>>() {
                    @Override
                    public void onSuccess(List<Barcode> barcodes) {
                        // Task completed successfully
                        // [START_EXCLUDE]
                        // [START get_barcodes]
                        for (Barcode barcode: barcodes) {
                            viewModel.setBarcode(barcode);
                            Rect bounds = barcode.getBoundingBox();
                            Point[] corners = barcode.getCornerPoints();

                            String rawValue = barcode.getRawValue();

                            int valueType = barcode.getValueType();
                            // See API reference for complete list of supported types
                            switch (valueType) {
                                case Barcode.TYPE_WIFI:
                                    String ssid = barcode.getWifi().getSsid();

                                    String password = barcode.getWifi().getPassword();
                                    int type = barcode.getWifi().getEncryptionType();
                                    break;
                                case Barcode.TYPE_URL:
                                    String title = barcode.getUrl().getTitle();
                                    String url = barcode.getUrl().getUrl();
                                    Toast.makeText(getActivity(),
                                            "title:" + title,
                                            Toast.LENGTH_SHORT).show();
                                    Toast.makeText(getActivity(),
                                            "url:" + url,
                                            Toast.LENGTH_SHORT).show();
                                    System.out.println("url:" + url);
                                    textView.setText(url);
                                    break;
                                case Barcode.TYPE_TEXT:
                                    String text = barcode.getDisplayValue();
                                    System.out.println("text: " + text);
                                    Toast.makeText(getActivity(),
                                            "text:" + text,
                                            Toast.LENGTH_SHORT).show();
                                    textView.setText(text);
                                    showResult(text);
                                    break;
                                default:
                                    String value = barcode.getDisplayValue();
                                    System.out.println("value: " + value);
                                    Toast.makeText(getActivity(),
                                            "value:" + value,
                                            Toast.LENGTH_SHORT).show();
                                    textView.setText(value);

                                    break;
                            }
                        }
                        imageProxy.close();
                    }
                })
                .addOnFailureListener(e -> {
                    e.printStackTrace();
                    imageProxy.close();
                });
        // [END run_detector]
    }

    private void showResult(String message) {
        if (!isNavgating) {
            isNavgating = true;
            NavController navController = NavHostFragment.findNavController(CameraFragment.this);
            Log.d("TAG", "id" + navController.getCurrentDestination().getId());
            navController.navigate(R.id.action_FirstFragment_to_SecondFragment);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        System.out.println("destroy");
        if(cameraExecutor != null && !cameraExecutor.isShutdown()) {
            cameraExecutor.shutdown();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        System.out.println("destroy view");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                startCamera();
            } else {
                Toast.makeText(getActivity().getApplicationContext(),
                        "Permissions not granted by the user.",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }
}