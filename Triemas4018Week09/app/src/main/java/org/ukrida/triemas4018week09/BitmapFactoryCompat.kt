package org.ukrida.triemas4018week09

import android.graphics.Bitmap

object BitmapFactoryCompat {
    fun decode(input: java.io.InputStream): Bitmap? =
        android.graphics.BitmapFactory.decodeStream(input)
}