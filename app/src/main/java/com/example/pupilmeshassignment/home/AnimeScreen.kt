package com.example.pupilmeshassignment.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.pupilmeshassignment.R

@Composable
fun AnimeScreen(paddingValues: PaddingValues, onCardClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(paddingValues)
    ) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            contentPadding = PaddingValues(4.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(15) { anime ->
                AnimeCard(){
                    onCardClick()
                }
            }
        }
    }
}

@Composable
fun AnimeCard(onCardClick: () -> Unit) {
    Card(
        modifier = Modifier
            .padding(4.dp)
            .fillMaxWidth()
            .aspectRatio(0.75f),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.outlinedCardElevation(
            defaultElevation = 2.dp,
            hoveredElevation = 4.dp
        ),
        border = CardDefaults.outlinedCardBorder(true),
        onClick = {onCardClick()}
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Image(
                painter = painterResource(id = R.drawable.ic_google_login),
                contentDescription = "Anime Image"
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
fun AnimeScreenPreview() {
    AnimeScreen(PaddingValues(), {})
}