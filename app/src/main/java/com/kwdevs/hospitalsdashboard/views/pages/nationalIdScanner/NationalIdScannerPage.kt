package com.kwdevs.hospitalsdashboard.views.pages.nationalIdScanner

import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.ImageDecoder
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.kwdevs.hospitalsdashboard.models.patients.IDScanResult
import com.kwdevs.hospitalsdashboard.routes.HospitalHomeRoute
import com.kwdevs.hospitalsdashboard.views.ImageHelper
import com.kwdevs.hospitalsdashboard.views.assets.BLUE
import com.kwdevs.hospitalsdashboard.views.assets.CustomButton
import com.kwdevs.hospitalsdashboard.views.assets.Label
import com.kwdevs.hospitalsdashboard.views.assets.VerticalSpacer
import com.kwdevs.hospitalsdashboard.views.assets.container.Container
import com.yalantis.ucrop.UCrop
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.util.UUID

// 📸 Image Picker Composable
@Composable
fun ImageInputPicker(
    previewBitmap: MutableState<Bitmap?>,
    onImageSelected: (Bitmap) -> Unit,
    scanResult: MutableState<IDScanResult?>
) {
    val context = LocalContext.current
    val cropLauncher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        result.data?.let {
            val uri = UCrop.getOutput(it)
            if (uri != null) {
                val source = ImageDecoder.createSource(context.contentResolver, uri)
                val bitmap = ImageDecoder.decodeBitmap(source).copy(Bitmap.Config.ARGB_8888, true)
                previewBitmap.value = bitmap
                onImageSelected(bitmap)
                File(uri.path ?: "").delete()

            }
        }
    }

    val galleryLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            val destUri = Uri.fromFile(File(context.cacheDir, "crop_${UUID.randomUUID()}.jpg"))
            val cropIntent = UCrop.of(it, destUri)
                .withAspectRatio(4f, 3f)
                .withOptions(UCrop.Options().apply {
                    setToolbarTitle("قص الصورة")
                    setFreeStyleCropEnabled(true)
                    setToolbarColor(Color.parseColor("#2196F3"))
                    setStatusBarColor(Color.parseColor("#1976D2"))
                    setActiveControlsWidgetColor(Color.parseColor("#FF4081"))
                })
                .getIntent(context)

            cropLauncher.launch(cropIntent)
        }
    }

    val cameraImageUri = remember { mutableStateOf<Uri?>(null) }

    val cameraLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        if (success) {
            cameraImageUri.value?.let { uri ->
                val destUri = Uri.fromFile(File(context.cacheDir, "crop_${UUID.randomUUID()}.jpg"))
                val cropIntent = UCrop.of(uri, destUri)
                    .withAspectRatio(4f, 3f)
                    .withOptions(UCrop.Options().apply {
                        setToolbarTitle("قص الصورة")
                        setFreeStyleCropEnabled(true)
                        setToolbarColor(Color.parseColor("#2196F3"))
                        setStatusBarColor(Color.parseColor("#1976D2"))
                        setActiveControlsWidgetColor(Color.parseColor("#FF4081"))
                    })
                    .getIntent(context)
                cropLauncher.launch(cropIntent)
            }
        }
    }

    Column {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            CustomButton(label = "Camera", buttonShape = RectangleShape) {
                val uri = ImageHelper().createTempImageUri(context)

                cameraLauncher.launch(uri) }
            CustomButton(label = "Gallery", buttonShape = RectangleShape) { galleryLauncher.launch("image/*") }
        }
        previewBitmap.value?.let {
            VerticalSpacer(8)
            Image(bitmap = it.asImageBitmap(), contentDescription = "Selected Image",
                modifier = Modifier.fillMaxWidth().padding(8.dp))
        }
        scanResult.value?.let { NationalIDCardResultScreen(it) }
    }
}



// 🧾 Full OCR Result Parser
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NationalIdScannerPage(navHostController: NavHostController) {
    val helper = remember { ImageHelper() }
    val context = LocalContext.current
    val selectedBitmap = remember { mutableStateOf<Bitmap?>(null) }
    val scanResult = remember { mutableStateOf<IDScanResult?>(null) }

    LaunchedEffect(selectedBitmap.value) {
        selectedBitmap.value?.let { bitmap ->
            withContext(Dispatchers.IO) {
                try {
                    val grayscaleBitmap = helper.preprocessBitmap(bitmap)
                    val text = helper.ocrArabic(context, bitmap)
                    val normalizedText = helper.normalizeArabicDigits(text)
                    val lines = normalizedText.lines().map { it.trim() }.filter { it.isNotEmpty() }
                    lines.forEachIndexed { index, s -> Log.e("Line $index", s) }
                    val nationalId = helper.extractNationalID(lines)
                    //val arabicLines = lines.filter { it.contains(Regex("[\\u0600-\\u06FF\\u0750-\\u077F\\u08A0-\\u08FF\\uFB50-\\uFDFF\\uFE70-\\uFEFF\\u0660-\\u0669]")) }
                    val arabicLines = lines.filter {
                        it.any { ch -> Character.UnicodeBlock.of(ch) == Character.UnicodeBlock.ARABIC }
                                || it.any { ch -> ch in '٠'..'٩' } // Arabic digits
                                // || it.any { ch -> ch in '0'..'9' }   // Western digits
                    }
                    val firstName = arabicLines.getOrNull(0)
                    val fullName = arabicLines.getOrNull(1)
                    val parsed = nationalId?.let { helper.parseEgyptianNationalID(it) } ?: emptyMap()
                    withContext(Dispatchers.Main) { scanResult.value = IDScanResult(firstName, fullName, nationalId, parsed) }
                }
                catch (e: Exception) { Log.e("OCR", "Failed: ${e.message}") }
            }
        }
    }
    val showSheet = remember { mutableStateOf(false) }

    Container(title = "Scan ID",
        headerShowBackButton = true,
        headerIconButtonBackground = BLUE,
        showSheet = showSheet,
        headerOnClick = {navHostController.navigate(HospitalHomeRoute.route)}) {
        ImageInputPicker(
            previewBitmap = selectedBitmap,
            onImageSelected = { selectedBitmap.value = it },
            scanResult = scanResult
        )
    }
}
// 📋 Result UI
@Composable
fun NationalIDCardResultScreen(result: IDScanResult) {
    Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
        Label("الاسم الأول: ${result.firstName ?: "غير متوفر"}", fontSize = 20)
        Label("باقي الاسم: ${result.fullNameLine ?: "غير متوفر"}", fontSize = 20)
        Label("الاسم الكامل: ${listOfNotNull(result.firstName, result.fullNameLine).joinToString(" ")}", fontSize = 20)
        Label("الرقم القومي: ${result.nationalId ?: "غير متوفر"}", fontSize = 20)
        VerticalSpacer(16)
        result.parsedFields.forEach { (key, value) ->
            Label("$key: $value", fontSize = 18)
        }
    }
}
