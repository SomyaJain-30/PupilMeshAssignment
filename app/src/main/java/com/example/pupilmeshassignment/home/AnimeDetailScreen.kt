package com.example.pupilmeshassignment.home

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pupilmeshassignment.R

@Composable
fun AnimeDetailScreen(onBackClick: () -> Unit) {
    BackHandler {
        onBackClick()
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterEnd) {
                Icon(Icons.Default.Star, contentDescription = "Save", tint = Color.White)
            }

            Row(modifier = Modifier.fillMaxWidth()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .height(200.dp)
                        .background(Color.White)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_google_login),
                        contentDescription = "Manga Image"
                    )
                }
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 12.dp)
                        .weight(1.3f)
                ) {
                    Text(
                        text = "Delivery Man from Murim",
                        color = Color.White,
                        fontWeight = FontWeight.Medium,
                        fontSize = 16.sp
                    )

                    Text(
                        text = "Delivery Man from Moorim. The delivery man from moorim. the delivery man from moorim",
                        color = Color.LightGray,
                        fontWeight = FontWeight.Normal,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(vertical = 20.dp)
                    )
                }
            }

            Text(
                text = "The martial god of the murim becomes a super fast deliver man",
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal,
                color = Color.LightGray,
                modifier = Modifier.padding(vertical = 32.dp)
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
fun AnimeDetailScreenPreview() {
    AnimeDetailScreen(onBackClick = {})
}