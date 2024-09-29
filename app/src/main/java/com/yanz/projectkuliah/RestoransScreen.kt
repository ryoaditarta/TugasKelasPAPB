package com.yanz.projectkuliah

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Place
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.clickable
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.runtime.*
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.viewmodel.compose.viewModel
import com.yanz.projectkuliah.ui.theme.projectkuliah

@Composable
fun RestaurantsScreen() {
    val viewModel: RestaurantsViewModel = viewModel()
    LaunchedEffect(key1 = "request_restaurants") {
        viewModel.getRestaurants()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "I Gusti Ngurah Ryo Adi Tarta",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.SansSerif,
            modifier = Modifier.padding(bottom = 6.dp)
        )
        Text(
            text = "225150200111011",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.SansSerif,
            modifier = Modifier.padding(bottom = 10.dp)
        )

        LazyColumn(
            contentPadding = PaddingValues(
                vertical = 8.dp,
                horizontal = 8.dp
            )
        ) {
            items(viewModel.state.value) { restaurant ->
                RestaurantItem(restaurant) { id ->
                    viewModel.toggleFavorite(id)
                }
            }
        }
    }
}

@Composable
fun RestaurantItem(item: Restauran, onClick: (id: Int) -> Unit) {
    val icon = if (item.isFavorite)
        Icons.Filled.Favorite
    else
        Icons.Filled.FavoriteBorder
    Card(
        elevation = CardDefaults.cardElevation(4.dp),  // Updated to CardDefaults.cardElevation
        modifier = Modifier.padding(8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(8.dp)
        ) {
            RestaurantIcon(Icons.Filled.Place, Modifier.weight(0.15f))
            RestaurantDetails(item.title, item.description, Modifier.weight(0.7f))
            RestaurantIcon(icon, Modifier.weight(0.15f)) {
                onClick(item.id)
            }
        }
    }
}

@Composable
private fun RestaurantIcon(icon: ImageVector, modifier: Modifier, onClick: () -> Unit = { }) {
    Image(
        imageVector = icon,
        contentDescription = "Restaurant icon",
        modifier = modifier
            .padding(8.dp)
            .clickable { onClick() })
}

@Composable
private fun RestaurantDetails(title: String, description: String, modifier: Modifier) {
    Column(modifier = modifier) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge  // Updated to Material 3's titleLarge
        )
        Text(
            text = description,
            style = MaterialTheme.typography.bodyMedium,  // Updated to Material 3's bodyMedium
            modifier = Modifier.alpha(0.6f)  // Applying alpha directly
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    projectkuliah {
        RestaurantsScreen()
    }
}
