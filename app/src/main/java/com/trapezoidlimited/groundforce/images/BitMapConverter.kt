package com.trapezoidlimited.groundforce.images

import android.graphics.Bitmap
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.trapezoidlimited.groundforce.EntryApplication
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream


object BitMapConverter {
    fun toJpg(bitmap: Bitmap): File {

        val filesDir: File = EntryApplication.applicationContext().filesDir
        val imageFile = File(filesDir, "ground_force_profile_image" + ".jpg")

        val os: OutputStream
        try {
            os = FileOutputStream(imageFile)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os)
            os.flush()
            os.close()
        } catch (e: Exception) {
            Log.e(javaClass.simpleName, "Error writing bitmap", e)
        }

        return imageFile
    }
}
