package com.example.pupilmeshassignment.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Matrix
import android.media.Image
import android.os.SystemClock
import android.util.Log
import androidx.camera.core.ImageProxy
import com.google.mediapipe.framework.image.BitmapImageBuilder
import com.google.mediapipe.framework.image.MPImage
import com.google.mediapipe.tasks.core.BaseOptions
import com.google.mediapipe.tasks.core.Delegate
import com.google.mediapipe.tasks.vision.core.RunningMode
import com.google.mediapipe.tasks.vision.facedetector.FaceDetector
import com.google.mediapipe.tasks.vision.facedetector.FaceDetectorResult

class FaceDetectorHelper(
    var threshHold: Float,
    var currentDelegate: Int,
    var runningMode: RunningMode = RunningMode.LIVE_STREAM,
    val context: Context,
    var faceDetectorListener: DetectorListener? = null
) {
    private var faceDetector: FaceDetector? = null

    companion object {
        const val DELEGATE_CPU = 0
        const val DELEGATE_GPU = 1
        const val THRESHOLD_DEFAULT = 0.5F
        const val OTHER_ERROR = 0
        const val GPU_ERROR = 1
        const val TAG = "FaceDetectorHelper"
    }

    init {
        setUpFaceDetector()
    }

    fun clearFaceDetector() {
        faceDetector?.close()
        faceDetector = null
    }

    private fun setUpFaceDetector() {
        //set general detection option, including number of used threads
        val baseOptionsBuilder = BaseOptions.builder()

        when (currentDelegate) {
            DELEGATE_CPU -> baseOptionsBuilder.setDelegate(Delegate.CPU)
            DELEGATE_GPU -> baseOptionsBuilder.setDelegate(Delegate.GPU)
        }

        val modelName = "blaze_face_short_range.tflite"
        baseOptionsBuilder.setModelAssetPath(modelName)

        when (runningMode) {
            RunningMode.LIVE_STREAM -> {
                if (faceDetectorListener == null) {
                    throw IllegalStateException("faceDetectorListener must be set when running mode is LIVE_STREAM")
                }
            }

            RunningMode.IMAGE,
            RunningMode.VIDEO -> {
                //no-op
            }
        }
        try {
            val optionBuilder = FaceDetector.FaceDetectorOptions.builder()
                .setBaseOptions(baseOptionsBuilder.build())
                .setRunningMode(runningMode)
                .setMinDetectionConfidence(threshHold)

            when (runningMode) {
                RunningMode.IMAGE,
                RunningMode.VIDEO -> optionBuilder.setRunningMode(runningMode)

                RunningMode.LIVE_STREAM ->
                    optionBuilder.setRunningMode(runningMode)
                        .setResultListener(this::returnLivestreamResult)
                        .setErrorListener(this::returnLivestreamError)
            }
            val options = optionBuilder.build()
            faceDetector = FaceDetector.createFromOptions(context, options)
        } catch (e: IllegalStateException) {
            faceDetectorListener?.onError("Face Detector failed to initialize. see more logs for details")
            Log.e(TAG, "TFLite failed to load model with error: " + e.message)
        } catch (e: RuntimeException) {
            faceDetectorListener?.onError(
                "Face detector failed to initialize. See error logs for " +
                        "details", GPU_ERROR
            )
            Log.e(
                TAG,
                "Face detector failed to load model with error: " + e.message
            )
        }
    }

    // Return running status of recognizer helper
    fun isClosed(): Boolean {
        return faceDetector == null
    }

    fun detectLiveStreamFrame(imageProxy: ImageProxy) {
        if (runningMode != RunningMode.LIVE_STREAM) {
            throw IllegalStateException("Attempting to call detectLiveStreamFrame while not using RunningMode.LIVE_STREAM")
        }

        val frameTime = SystemClock.uptimeMillis()

        //copy out RGB bits from the frame to a bitmap buffer
        val bitmapBuffer = Bitmap.createBitmap(
            imageProxy.width, imageProxy.height, Bitmap.Config.ARGB_8888
        )
        imageProxy.use { bitmapBuffer.copyPixelsFromBuffer(imageProxy.planes[0].buffer) }
        imageProxy.close()

        //Rotate the frame received from the camera to be in the same direction as it'll be shown
        val matrix = Matrix().apply {
            postRotate(imageProxy.imageInfo.rotationDegrees.toFloat())
            postScale(-1f, 1f, imageProxy.width.toFloat(), imageProxy.height.toFloat())
        }

        val rotatedBitmap = Bitmap.createBitmap(
            bitmapBuffer, 0, 0, bitmapBuffer.width, bitmapBuffer.height, matrix, true
        )

        //convert the input bitmap face to an MPImage face to run inference
        val mpImage = BitmapImageBuilder(rotatedBitmap).build()
        detectAsync(mpImage, frameTime)
    }

    //Run face detection using MediaPipe Face Detector API
    fun detectAsync(mpImage: MPImage, frameTime: Long) {
        faceDetector?.detectAsync(mpImage, frameTime)

    }

    // Return the detection result to this FaceDetectorHelper's caller
    private fun returnLivestreamResult(
        result: FaceDetectorResult,
        input: MPImage
    ) {
        val finishTimeMs = SystemClock.uptimeMillis()
        val inferenceTime = finishTimeMs - result.timestampMs()

        faceDetectorListener?.onResults(
            ResultBundle(
                listOf(result),
                inferenceTime,
                input.height,
                input.width
            )
        )
    }

    // Return errors thrown during detection to this FaceDetectorHelper's caller
    private fun returnLivestreamError(error: RuntimeException) {
        faceDetectorListener?.onError(
            error.message ?: "An unknown error has occurred"
        )
    }


    data class ResultBundle(
        val results: List<FaceDetectorResult>,
        val inferenceTime: Long,
        val inputImageHeight: Int,
        val inputImageWidth: Int,
    )

    interface DetectorListener {
        fun onError(error: String, errorCode: Int = OTHER_ERROR)
        fun onResults(resultBundle: ResultBundle)
    }
}