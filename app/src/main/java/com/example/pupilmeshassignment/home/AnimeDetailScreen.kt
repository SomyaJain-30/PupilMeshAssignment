package com.example.pupilmeshassignment.home

import android.widget.Scroller
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clipScrollableContainer
import androidx.compose.foundation.gestures.FlingBehavior
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.SubcomposeAsyncImage

@Composable
fun AnimeDetailScreen(
    viewModel: MangaViewModel = getMangaViewModel(),
    onBackClick: () -> Unit
) {

    val manga = viewModel.selectedManga.collectAsState()

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
                .padding(16.dp, 0.dp)
                .verticalScroll(rememberScrollState())
        ) {

            Spacer(modifier = Modifier.height(16.dp))

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
                    SubcomposeAsyncImage(
                        model = manga.value?.thumb,
                        contentScale = ContentScale.FillBounds,
                        contentDescription = "Anime Image",
                        loading = {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator(modifier = Modifier.size(20.dp))
                            }
                        },
                        error = { Text(text = "Image failed to load") },
                        modifier = Modifier.fillMaxSize()

                    )
                }
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 12.dp)
                        .weight(1.3f)
                ) {
                    Text(
                        text = manga.value?.title ?: "",
                        color = Color.White,
                        fontWeight = FontWeight.Medium,
                        fontSize = 16.sp
                    )

                    Text(
                        text = manga.value?.subTitle ?: "",
                        color = Color.LightGray,
                        fontWeight = FontWeight.Normal,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(vertical = 20.dp)
                    )
                }
            }

            Text(
                text = manga.value?.summary ?: "",
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal,
                color = Color.LightGray,
                modifier = Modifier.padding(vertical = 32.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))

        }
    }
}

@Composable
@Preview(showBackground = true)
fun AnimeDetailScreenPreview() {
    AnimeDetailScreen(onBackClick = {})
}