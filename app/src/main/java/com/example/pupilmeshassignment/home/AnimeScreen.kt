package com.example.pupilmeshassignment.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.SubcomposeAsyncImage
import com.example.pupilmeshassignment.MyApp
import com.example.pupilmeshassignment.data.entity.MangaResponse

@Composable
fun AnimeScreen(
    viewModel: MangaViewModel = getMangaViewModel(),
    paddingValues: PaddingValues,
    onCardClick: () -> Unit
) {

    val mangas = viewModel.mangas.collectAsLazyPagingItems()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {
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
                items(mangas.itemCount) { index ->
                    mangas[index]?.let { manga ->
                        AnimeCard(manga) { id ->
                            onCardClick()
                            viewModel.selectManga(id)
                        }
                    }
                }

                // loading indicator at the bottom while scrolling
                if (mangas.loadState.append is LoadState.Loading) {
                    item(span = { GridItemSpan(3) }) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                }

                //show error if any
                if (mangas.loadState.append is LoadState.Error || mangas.loadState.refresh is LoadState.Error) {
                    item(span = { GridItemSpan(3) }) {
                        val error = when {
                            mangas.loadState.append is LoadState.Error -> {
                                (mangas.loadState.append as LoadState.Error).error
                            }

                            else -> {
                                (mangas.loadState.refresh as LoadState.Error).error
                            }
                        }

                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = "Error: ${error.localizedMessage}",
                                    color = MaterialTheme.colorScheme.error
                                )
                                Spacer(
                                    modifier = Modifier.height(8.dp)
                                )
                                Button({
                                    mangas.retry()
                                }) {
                                    Text("Retry")
                                }
                            }
                        }
                    }
                }
            }
        }
        //show initial loading state
        if (mangas.loadState.refresh is LoadState.Loading) {
            CircularProgressIndicator(
                modifier = Modifier
                    .width(26.dp)
                    .height(26.dp)
                    .align(Alignment.Center)
            )
        }
    }
}

@Composable
fun AnimeCard(manga: MangaResponse.MangaDto, onCardClick: (id: String) -> Unit) {
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
        onClick = {
            onCardClick(manga.id)
        }
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            SubcomposeAsyncImage(
                model = manga.thumb,
                loading = {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(modifier = Modifier.size(16.dp))
                    }
                },
                contentDescription = "Anime Thumbnail",
                error = { Text("Image Failed to load") },
                contentScale = ContentScale.FillBounds,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
fun AnimeScreenPreview() {
    AnimeScreen(paddingValues = PaddingValues(), onCardClick = {})
}

@Composable
fun getMangaViewModel(): MangaViewModel {
    val context = LocalContext.current
    val appContainer = (context.applicationContext as MyApp).container
    val repository = appContainer.mangaRepository
    return viewModel(factory = MangaViewModelFactory(repository))
}
