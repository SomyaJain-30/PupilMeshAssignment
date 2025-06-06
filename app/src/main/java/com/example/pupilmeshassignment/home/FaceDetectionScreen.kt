package com.example.pupilmeshassignment.home

import androidx.camera.core.SurfaceRequest
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale


@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun FaceDetectionScreen(paddingValues: PaddingValues) {
    val context = LocalContext.current
    val cameraPermissionState = rememberPermissionState(android.Manifest.permission.CAMERA)

    LaunchedEffect(Unit) {
        if (!cameraPermissionState.status.isGranted && cameraPermissionState.status.shouldShowRationale) {

        } else {
            cameraPermissionState.launchPermissionRequest()
        }
    }

    if (cameraPermissionState.status.isGranted) {
//        CameraPreviewContent()
    }

}

//@Composable
//fun CameraPreviewContent(
//    viewModel: FaceDetectionViewModel,
//    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current
//) {
//    val context = LocalContext.current
//    val surfaceRequest by viewModel.surfaceRequest.collectAsStateWithLifecycle()
//
//    LaunchedEffect(Unit) {
//        viewModel.bindToCamera(context.applicationContext, lifecycleOwner)
//    }
//    surfaceRequest?.let { request ->
//    }
//}

@Composable
fun CameraXViewFinder(surfaceRequest: SurfaceRequest) {
    AndroidView(
        factory = { context ->
            val previewView = PreviewView(context).apply {
                scaleType = PreviewView.ScaleType.FILL_CENTER
            }
            // Provide the surface to CameraX when it's available
            /*previewView.surfaceProvider = androidx.camera.core.Preview.SurfaceProvider { request ->
                surfaceRequest.provideSurface(
                    request.surface,
                    ContextCompat.getMainExecutor(context)
                ) { result ->
                    Log.d("CameraX", "Surface provided result: $result")
                }
            }*/


            previewView
        }
    )
}

@Composable
@Preview(showBackground = true)
fun FaceDetectionScreenPreview() {
    FaceDetectionScreen(PaddingValues())
}