package org.ukrida.triemas4018week09

import android.Manifest
import android.R.attr.text
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import org.ukrida.triemas4018week09.ImagePlaceholder

@Composable
fun AmbilDataLokasi() {
    val context = LocalContext.current

    var lokasi by remember {
        mutableStateOf("")
    }

    var status by remember {
        mutableStateOf("")
    }

    var fotoBitMap by remember {
        mutableStateOf<Bitmap?>(null)
    }

    var fotoUri by remember {
        mutableStateOf<Uri?>(null)
    }

    val fusedLocationClient = remember {
        LocationServices.getFusedLocationProviderClient(context)
    }

    val cameraLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.TakePicturePreview()
        ) { bitmap ->
            if (bitmap != null) {
                fotoBitMap = bitmap
                fotoUri = null
                status = "Foto berhasil diambil."
            } else {
                status = "Pengambilan foto dibatalkan"
            }
        }

    // Meminta izin lokasi
    val locationPermissionLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            val granted =
                permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                    permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
            if (granted) {
                ambilLokasi(
                    fusedLocationClient = fusedLocationClient,
                    hasFineLocation =
                        permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true,
                    onLocation = { lokasi = it },
                    onStatus = { status = it }
                )
                // Buka kamera setelah permintaan lokasi diproses
                cameraLauncher.launch(null)
            } else {
                lokasi = "Izin lokasi ditolak."
                status = "Izinkan lokasi untuk menampilkan koordinat."
                // Tetap izinkan pengguna mengambil foto
                cameraLauncher.launch(null)
            }
        }

    Column(
    modifier = Modifier.fillMaxSize().padding(16.dp),
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Ambil Gambar", fontSize = 16.sp, color = Color.Black)
        Spacer(modifier = Modifier.height(56.dp))

        // Tombol Camera
        Button(
            onClick = {
                val fineGranted = ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED

                val coarseGranted = ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
                if (fineGranted || coarseGranted) {
                    ambilLokasi(
                        fusedLocationClient = fusedLocationClient,
                        hasFineLocation = fineGranted,
                        onLocation = { lokasi = it },
                        onStatus = { status = it }
                    )
                    cameraLauncher.launch(null)
                } else {
                    locationPermissionLauncher.launch(
                        arrayOf(
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION
                        )
                    )
                }
            },
            modifier = Modifier.size(width = 114.dp, height = 36.dp),
            shape = RoundedCornerShape (7.dp),
            colors = ButtonDefaults.buttonColors (
                containerColor = Color(0xFFFFAD8C), contentColor = Color.Black)
            ) {
            Text(text = "Camera", fontSize = 16.sp, fontWeight = FontWeight.Normal)
        }
        Spacer(modifier = Modifier.height(20.dp))

        // Kotak teks lokasi
        Box(
            modifier = Modifier .fillMaxWidth(0.72f)
                .height(48.dp).border(
                    width = 1.dp,
                    color = Color.DarkGray,
                    shape = RoundedCornerShape(7.dp)
                )
                .padding(horizontal = 10.dp, vertical = 7.dp)
        ) {
            BasicTextField(
                value = lokasi,
                onValueChange = { lokasi = it },
                readOnly = true,
                textStyle = androidx.compose.ui.text.TextStyle(
                    fontSize = 10.sp,
                    color = Color.Black
                ),
                modifier = Modifier.fillMaxWidth().align(Alignment.CenterStart)
                    .padding(top = 8.dp),
                decorationBox = { innerTextField ->
                    Box {
                        if (lokasi.isEmpty()) {
                            Text(text = "", fontSize = 10.sp
                            )
                        }
                        innerTextField()
                    }
                }
            )
        }
        Spacer(modifier = Modifier.height(16.dp))

        // Status pengambilan foto atau lokasi
        if (status.isNotEmpty()) {
            Text(
                text = status,
                fontSize = 11.sp,
                color = Color.DarkGray
            )
        }
        // Tampilkan hasil foto
        fotoBitMap?.let { bitmap ->
            Spacer(modifier = Modifier.height(12.dp))

            Image(
                bitmap = bitmap.asImageBitmap(),
                contentDescription = "Foto dari kamera",
                modifier = Modifier
                    .size(150.dp)
                    .clip(RoundedCornerShape(8.dp))
            )
        }

        fotoUri?.let { uri ->
            Spacer(modifier = Modifier.height(12.dp))
            ImagePlaceholder(uri)
        }
    }
}

private fun ambilLokasi(
    fusedLocationClient:
    com.google.android.gms.location.FusedLocationProviderClient,
    hasFineLocation: Boolean,
    onLocation: (String) -> Unit,
    onStatus: (String) -> Unit) {
        try {
            onStatus("Sedang mengambil lokasi...")
            fusedLocationClient.getCurrentLocation(
                if (hasFineLocation) {
                    Priority.PRIORITY_HIGH_ACCURACY
                } else {
                    Priority.PRIORITY_BALANCED_POWER_ACCURACY
                },
                CancellationTokenSource().token
            ).addOnSuccessListener { location ->
                if (location != null) {
                    onLocation(
                        "Lat: %.6f\nLon: %.6f".format(
                            java.util.Locale.US,
                            location.latitude,
                            location.longitude
                        )
                    )
                    onStatus("Lokasi berhasil diperoleh.")
                } else {
                    onLocation("")
                    onStatus("Lokasi tidak tersedia. Aktifkan GPS lalu coba lagi.")
                }
            }.addOnFailureListener { error ->
                onLocation("")
                onStatus("Gagal mengambil lokasi: ${error.message}")
            }
        } catch (e: SecurityException) {
            onLocation("")
            onStatus("Akses lokasi tidak diizinkan.")
        }
}