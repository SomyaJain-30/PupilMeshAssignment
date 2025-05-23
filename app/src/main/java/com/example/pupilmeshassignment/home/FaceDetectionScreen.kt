package com.example.pupilmeshassignment.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun FaceDetectionScreen(paddingValues: PaddingValues) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(paddingValues)
    ) {
        Card(
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .height(200.dp)
                .width(200.dp)
                .align(Alignment.Center)
        ) {

        }
    }
}

@Composable
@Preview(showBackground = true)
fun FaceDetectionScreenPreview() {
    FaceDetectionScreen(PaddingValues())
}