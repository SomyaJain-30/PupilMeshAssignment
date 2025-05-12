package com.example.pupilmeshassignment.home

import android.util.Log
import androidx.compose.foundation.Image
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.pupilmeshassignment.MyApp
import com.example.pupilmeshassignment.R
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
            Log.e("CHECK-->", "AnimeScreen: ${mangas.itemCount}")
            items(mangas.itemCount) { index ->
                mangas[index]?.let { manga ->
                    AnimeCard(manga) {
                        onCardClick()
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
                            .fillMaxWidth()
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

            //show initial loading state
            /* if (mangas.loadState.refresh is LoadState.Loading) {
                 Box(
                     modifier = Modifier.fillMaxSize(),
                     contentAlignment = Alignment.Center
                 ) {
                     CircularProgressIndicator()
                 }
             }

             // Show error message if any
             errorMessage?.let { message ->
                 AlertDialog(
                     onDismissRequest = { viewModel.clearError() },
                     title = { Text("Error") },
                     text = { Text(message) },
                     confirmButton = {
                         TextButton(onClick = { viewModel.clearError() }) {
                             Text("OK")
                         }
                     }
                 )
             }*/
        }
    }
}

@Composable
fun AnimeCard(manga: MangaResponse.MangaDto, onCardClick: () -> Unit) {
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
        onClick = { onCardClick() }
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
    AnimeScreen(paddingValues = PaddingValues(), onCardClick = {})
}

@Composable
fun getMangaViewModel(): MangaViewModel {
    val context = LocalContext.current
    val appContainer = (context.applicationContext as MyApp).container
    val repository = appContainer.mangaRepository
    return viewModel(factory = MangaViewModelFactory(repository))
}
