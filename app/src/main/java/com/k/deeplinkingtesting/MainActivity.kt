package com.k.deeplinkingtesting

import android.Manifest
import android.animation.ObjectAnimator
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Rect
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetectorOptions

class MainActivity : AppCompatActivity() {
    private lateinit var previewView: PreviewView
    private lateinit var faceOverlay: FaceOverlayView
    private lateinit var selectedImage: ImageView
    private lateinit var scannerLine: View
    private lateinit var txtScanning: TextView
    private lateinit var faceImage: ImageView
    private val pickImageLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri?.let {

                faceImage.visibility= View.GONE
                previewView.visibility = View.GONE
                selectedImage.visibility = View.VISIBLE

                selectedImage.setImageURI(it)

                // 🔥 Start scanner
                startScannerAnimation()

                detectFaceWithDelay(it)
                detectFaceAndCrop(it)
            }
        }
    private fun detectFaceWithDelay(uri: Uri) {

        val image = InputImage.fromFilePath(this, uri)

        val detector = FaceDetection.getClient(
            FaceDetectorOptions.Builder()
                .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_ACCURATE)
                .build()
        )

        detector.process(image)
            .addOnSuccessListener { faces ->

                Handler(Looper.getMainLooper()).postDelayed({

                    stopScannerAnimation()

                    if (faces.isNotEmpty()) {
                        faceImage.visibility = View.VISIBLE
                        Toast.makeText(this, "Face Found ✅", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, "No Face Found ❌", Toast.LENGTH_SHORT).show()
                    }

                }, 2000)
            }
    }
    private fun stopScannerAnimation() {
        scannerLine.clearAnimation()
        scannerLine.visibility = View.GONE
        txtScanning.visibility = View.GONE
    }
    private fun startScannerAnimation() {

        scannerLine.visibility = View.VISIBLE
        txtScanning.visibility = View.VISIBLE

        val animator = ObjectAnimator.ofFloat(
            scannerLine,
            "translationY",
            0f,
            800f // adjust based on height
        )

        animator.duration = 1500
        animator.repeatCount = ObjectAnimator.INFINITE
        animator.repeatMode = ObjectAnimator.REVERSE
        animator.start()
    }
    private fun getBitmapFromUri(uri: Uri): Bitmap {
        return MediaStore.Images.Media.getBitmap(contentResolver, uri)
    }
    private fun detectFaceAndCrop(uri: Uri) {

        val image = InputImage.fromFilePath(this, uri)

        val detector = FaceDetection.getClient(
            FaceDetectorOptions.Builder()
                .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_ACCURATE)
                .build()
        )

        val bitmap = getBitmapFromUri(uri)

        detector.process(image)
            .addOnSuccessListener { faces ->

                if (faces.isNotEmpty()) {

                    val face = faces[0] // first face

                    val bounds = face.boundingBox

                    val cropped = cropBitmap(bitmap, bounds)

                    faceImage.setImageBitmap(cropped) // 🔥 SHOW FACE

                   // Toast.makeText(this, "Face Cropped ✅", Toast.LENGTH_SHORT).show()

                }
            }
    }
    private fun cropBitmap(bitmap: Bitmap, rect: Rect): Bitmap {

        val left = rect.left.coerceAtLeast(0)
        val top = rect.top.coerceAtLeast(0)
        val right = rect.right.coerceAtMost(bitmap.width)
        val bottom = rect.bottom.coerceAtMost(bitmap.height)

        return Bitmap.createBitmap(
            bitmap,
            left,
            top,
            right - left,
            bottom - top
        )
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        previewView = findViewById(R.id.previewView)
        selectedImage = findViewById(R.id.selectedImage)
        scannerLine = findViewById(R.id.scannerLine)
        txtScanning = findViewById(R.id.txtScanning)
        faceImage = findViewById(R.id.faceImage)
        faceOverlay = findViewById(R.id.faceOverlay)
        previewView.implementationMode = PreviewView.ImplementationMode.COMPATIBLE
        findViewById<Button>(R.id.btnPickImage).setOnClickListener {
            pickImageLauncher.launch("image/*")
        }
        if (allPermissionsGranted()) {
            startCamera()
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.CAMERA),
                101
            )
        }
    }
    private fun startCamera() {

        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener({

            val cameraProvider = cameraProviderFuture.get()

            val preview = Preview.Builder().build().also {
                it.setSurfaceProvider(previewView.surfaceProvider)
            }

            val imageAnalyzer = ImageAnalysis.Builder()
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build()

            // 🔥 THIS IS WHERE YOUR CLASS IS USED
            imageAnalyzer.setAnalyzer(
                ContextCompat.getMainExecutor(this),
                FaceAnalyzer(faceOverlay)
            )

            val cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA

            try {
                cameraProvider.unbindAll() // 🔥 IMPORTANT

                cameraProvider.bindToLifecycle(
                    this,
                    cameraSelector,
                    preview,
                    imageAnalyzer
                )

            } catch (e: Exception) {
                e.printStackTrace()
            }

        }, ContextCompat.getMainExecutor(this))
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == 101 &&
            grantResults.isNotEmpty() &&
            grantResults[0] == PackageManager.PERMISSION_GRANTED
        ) {
            startCamera() // 🔥 THIS IS CRITICAL
        }
    }

    private fun allPermissionsGranted() =
        ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
}



