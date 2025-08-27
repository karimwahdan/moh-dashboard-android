package com.kwdevs.hospitalsdashboard.views

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.Paint
import android.net.Uri
import androidx.core.content.FileProvider
import com.googlecode.tesseract.android.TessBaseAPI
import java.io.File
import java.io.FileOutputStream

class ImageHelper {

    private fun ensureBitmapConfig(bitmap: Bitmap): Bitmap {
        return if (bitmap.config != Bitmap.Config.ARGB_8888) {
            bitmap.copy(Bitmap.Config.ARGB_8888, true)
        } else bitmap
    }
    fun createTempImageUri(context: Context): Uri {
        val file = File.createTempFile("camera_image", ".jpg", context.cacheDir)
        return FileProvider.getUriForFile(context, "${context.packageName}.provider", file)
    }
    fun copyTessData(context: Context) {
        val tessDir = File(context.filesDir, "tessdata")
        if (!tessDir.exists()) tessDir.mkdirs()
        val file = File(tessDir, "ara.traineddata")
        if (!file.exists()) {
            context.assets.open("tessdata/ara.traineddata").use { input ->
                FileOutputStream(file).use { output ->
                    input.copyTo(output)
                }
            }
        }
    }

    fun preprocessBitmap(input: Bitmap): Bitmap {
        val bmp = ensureBitmapConfig(input)
        val gray = Bitmap.createBitmap(bmp.width, bmp.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(gray)
        val paint = Paint()
        paint.colorFilter = ColorMatrixColorFilter(ColorMatrix().apply { setSaturation(0f) })
        canvas.drawBitmap(bmp, 0f, 0f, paint)
        return gray
    }
/*
    fun otsuBinarize(srcGray: Bitmap): Bitmap {
        val width = srcGray.width
        val height = srcGray.height
        val mat = Mat()
        Utils.bitmapToMat(srcGray, mat)
        Imgproc.cvtColor(mat, mat, Imgproc.COLOR_BGR2GRAY)
        Imgproc.threshold(mat, mat, 0.0, 255.0, Imgproc.THRESH_BINARY + Imgproc.THRESH_OTSU)
        val dst = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        Utils.matToBitmap(mat, dst)
        mat.release()
        return dst
    }
*/
    fun normalizeArabicDigits(input: String): String {
        val builder = StringBuilder()
        for (char in input) {
            val normalizedChar = when (char) {
                in '٠'..'٩' -> '0' + (char - '٠')
                else -> char
            }
            builder.append(normalizedChar)
        }
        return builder.toString()
    }

    fun ocrArabic(context: Context, bitmap: Bitmap): String {
        copyTessData(context)
        val tess = TessBaseAPI()
        if (!tess.init(context.filesDir.absolutePath, "ara")) {
            tess.recycle()
            throw RuntimeException("Tesseract init failed")
        }
        //tess.setVariable(TessBaseAPI.VAR_CHAR_WHITELIST, " ٠١٢٣٤٥٦٧٨٩0123456789ءاأإآبتثج...")

        tess.setVariable(TessBaseAPI.VAR_CHAR_WHITELIST, "٠١٢٣٤٥٦٧٨٩ءابتثجحخدذرزسشصضطظعغفقكلمنهوي ")
        tess.pageSegMode = TessBaseAPI.PageSegMode.PSM_AUTO_OSD
        tess.setImage(ensureBitmapConfig(bitmap)) // Use raw image (not preprocessed for OCR if needed)
        val result = tess.utF8Text ?: ""
        tess.recycle()
        return normalizeArabicDigits(result)
    }

    fun extractNationalID(lines: List<String>): String? {
        return lines.map { normalizeArabicDigits(it) }
            .firstOrNull { it.matches(Regex("\\d{14}")) }
    }

    fun parseEgyptianNationalID(id: String): Map<String, String> {
        if (id.length != 14) return mapOf("خطأ" to "رقم غير صالح")
        val century = if (id[0] == '2') "19" else "20"
        val year = century + id.substring(1, 3)
        val month = id.substring(3, 5)
        val day = id.substring(5, 7)
        val gov = id.substring(7, 9)
        val gender = if (id[12].digitToInt() % 2 == 0) "أنثى" else "ذكر"
        return mapOf("تاريخ الميلاد" to "$day/$month/$year", "النوع" to gender, "كود المحافظة" to gov)
    }
}

