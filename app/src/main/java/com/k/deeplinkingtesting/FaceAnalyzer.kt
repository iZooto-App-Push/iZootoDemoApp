package com.k.deeplinkingtesting

import android.annotation.SuppressLint
import android.util.Log
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.*

class FaceAnalyzer(
    private val overlay: FaceOverlayView
) : ImageAnalysis.Analyzer {

    private val detector: FaceDetector

    // 🔥 Prevent over-processing (VERY IMPORTANT)
    private var lastAnalyzedTime = 0L

    init {
        val options = FaceDetectorOptions.Builder()
            .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_ACCURATE)
            .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_ALL)
            .setContourMode(FaceDetectorOptions.CONTOUR_MODE_ALL)
            .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_ALL)
            .enableTracking()
            .build()

        detector = FaceDetection.getClient(options)
    }

    @SuppressLint("UnsafeOptInUsageError")
    override fun analyze(imageProxy: ImageProxy) {

        // 🔥 Throttle (process every 200ms)
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastAnalyzedTime < 200) {
            imageProxy.close()
            return
        }
        lastAnalyzedTime = currentTime

        val mediaImage = imageProxy.image ?: run {
            imageProxy.close()
            return
        }

        val image = InputImage.fromMediaImage(
            mediaImage,
            imageProxy.imageInfo.rotationDegrees
        )

        detector.process(image)
            .addOnSuccessListener { faces ->

                if (faces.isEmpty()) {
                    overlay.setFaces(emptyList()) // clear overlay
                    return@addOnSuccessListener
                }

                // 🔥 Update overlay
                overlay.setFaces(faces)

                val face = faces[0]

                // 👁 Landmarks (safe)
                val leftEye = face.getLandmark(FaceLandmark.LEFT_EYE)?.position
                val rightEye = face.getLandmark(FaceLandmark.RIGHT_EYE)?.position
                val nose = face.getLandmark(FaceLandmark.NOSE_BASE)?.position
                val mouth = face.getLandmark(FaceLandmark.MOUTH_BOTTOM)?.position
                val leftEar = face.getLandmark(FaceLandmark.LEFT_EAR)?.position
                val rightEar = face.getLandmark(FaceLandmark.RIGHT_EAR)?.position

                // 🔥 Contours (more reliable)
                val faceContour = face.getContour(FaceContour.FACE)?.points
                val leftEyeContour = face.getContour(FaceContour.LEFT_EYE)?.points
                val rightEyeContour = face.getContour(FaceContour.RIGHT_EYE)?.points
                val lipsContour = face.getContour(FaceContour.UPPER_LIP_TOP)?.points

                // 😀 Classification
                val smile = face.smilingProbability ?: -1f
                val leftEyeOpen = face.leftEyeOpenProbability ?: -1f
                val rightEyeOpen = face.rightEyeOpenProbability ?: -1f

                // 🔄 Head rotation
                val rotX = face.headEulerAngleX
                val rotY = face.headEulerAngleY
                val rotZ = face.headEulerAngleZ

                // 🔥 Logs
                Log.d(
                    "FACE_DATA", """
                    
                    ===== FACE DETECTED =====
                    
                    👁 Landmarks:
                    LeftEye: ${leftEye ?: "null"}
                    RightEye: ${rightEye ?: "null"}
                    Nose: ${nose ?: "null"}
                    Mouth: ${mouth ?: "null"}
                    LeftEar: ${leftEar ?: "null"}
                    RightEar: ${rightEar ?: "null"}
                    
                    🔵 Contours:
                    FacePoints: ${faceContour?.size ?: 0}
                    LeftEyePoints: ${leftEyeContour?.size ?: 0}
                    RightEyePoints: ${rightEyeContour?.size ?: 0}
                    LipsPoints: ${lipsContour?.size ?: 0}
                    
                    😀 Smile: $smile
                    👁 LeftEyeOpen: $leftEyeOpen
                    👁 RightEyeOpen: $rightEyeOpen
                    
                    🔄 Rotation:
                    X=$rotX Y=$rotY Z=$rotZ
                    
                    =========================
                    
                """.trimIndent()
                )
            }
            .addOnFailureListener {
                Log.e("FACE_ERROR", "Detection failed", it)
            }
            .addOnCompleteListener {
                imageProxy.close() // 🔥 MUST
            }
    }
}