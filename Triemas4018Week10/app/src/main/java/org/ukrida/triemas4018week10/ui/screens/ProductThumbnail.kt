package org.ukrida.triemas4018week10.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BrokenImage
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import coil3.compose.SubcomposeAsyncImage
import coil3.compose.SubcomposeAsyncImageContent

@Composable
fun ProductThumbnail(
    imageUrl: String?,
    modifier: Modifier = Modifier
) {
    val imageSize = 50.dp
    val shape = RoundedCornerShape(10.dp)

    val containerColor =
        MaterialTheme.colorScheme.surfaceVariant

    val borderColor =
        MaterialTheme.colorScheme.outlineVariant

    val iconColor =
        MaterialTheme.colorScheme.onSurfaceVariant

    // Empty URL placeholder
    if (imageUrl.isNullOrBlank()) {
        Icon(
            imageVector = Icons.Default.BrokenImage,
            contentDescription = "Image unavailable",
            tint = iconColor,
            modifier = Modifier.size(28.dp)
        )
    } else {
        // The image URL is present, but network or
        // parsing failures may prevent the image from loading
        SubcomposeAsyncImage(
            model = imageUrl,
            contentDescription = "Product Image",
            modifier = modifier
                .size(imageSize)
                .clip(shape)
                .border(
                    width = 1.dp,
                    color = borderColor,
                    shape = shape
                ),
            loading = {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(containerColor),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.BrokenImage,
                        contentDescription = "Loading product image",
                        tint = iconColor,
                        modifier = Modifier.size(24.dp)
                    )
                }
            },
            error = {
                Icon(
                    imageVector = Icons.Default.BrokenImage,
                    contentDescription = "Failed to load image",
                    tint = MaterialTheme.colorScheme.error,
                    modifier = Modifier.size(28.dp)
                )
            },
            success = {
                SubcomposeAsyncImageContent()
            }
        )
    }
}