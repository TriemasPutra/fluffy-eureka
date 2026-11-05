package org.ukrida.triemas4018week09

import android.graphics.Bitmap
import android.net.Uri
import androidx.compose.animation.core.animateDecay
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext

@Composable
fun ImagePlaceholder(uri: Uri) {
    val context = LocalContext.current
    var bitmap by remember {
        mutableStateOf<Bitmap?>(null)
    }
    androidx.compose.runtime.LaunchedEffect(uri) {
        bitmap = try {
            if (android.os.Build.VERSION.SDK_INT >= 28) {
                val source = android.graphics.ImageDecoder
                    .createSource(context.contentResolver, uri)
                android.graphics.ImageDecoder.decodeBitmap(source)
            } else {
                @Suppress("DEPRECATION")
                context.contentResolver.openInputStream(uri)?.use {
                    BitmapFactoryCompat.decode(it)
                }
            }
        } catch (_: Exception) {
            null
        }
    }
}