package com.trapezoidlimited.groundforce.images

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.widget.ImageView
import android.widget.Toast
import com.trapezoidlimited.groundforce.utils.GROUND_FORCE_IMAGE_NAME
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.net.HttpURLConnection
import java.net.URL

class ImageHelperImpl : ImageHelper {

    override fun getImageFromServerAndSave(
        avatarUrl: String,
        image: ImageView,
        activity: Activity
    ) {


        Thread {
            val imageUrl = URL(avatarUrl)
            val connection = imageUrl.openConnection() as HttpURLConnection
            connection.doInput = true

            try {
                connection.connect()
                val inputStream = connection.inputStream

                val path = File(activity.filesDir, "GroundForce${File.separator}Images")
                if (!path.exists()) {
                    path.mkdirs();
                }

                val outFile = File(path, GROUND_FORCE_IMAGE_NAME)
                val outputStream = FileOutputStream(outFile)

                try {
                    outputStream.use { output ->
                        val buffer = ByteArray(4 * 1024)
                        var byteCount = inputStream.read(buffer)
                        while (byteCount > 0) {
                            output.write(buffer, 0, byteCount)
                            byteCount = inputStream.read(buffer)
                        }
                        output.flush()
                        output.close()
                    }
                    //Load The Image from internal storage into View when it's done
                    getImageFromInternalStorage(activity, image)
                } catch (e: FileNotFoundException) {
                    activity.runOnUiThread {
                        Toast.makeText(activity, e.message.toString(), Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                //Handle Error in case where there's no internet connection and no profile image saved.

                activity.runOnUiThread {
//                    Toast.makeText(activity, avatarUrl, Toast.LENGTH_SHORT).show()
                    Toast.makeText(activity, "No profile Picture yet", Toast.LENGTH_SHORT).show()
                }
            }
        }.start()
    }

    override fun getImageFromInternalStorage(activity: Activity, imageView: ImageView) {
        val path = File(activity.filesDir, "GroundForce${File.separator}Images")
        val imageFile = File(path, GROUND_FORCE_IMAGE_NAME)
        displayImage(imageFile.absolutePath, imageView)
    }

    private fun displayImage(imagePath: String, imageView: ImageView) {
        GlobalScope.launch(Dispatchers.Main) {
            val bitmap = loadImageFromFile(imagePath)
            imageView.setImageBitmap(bitmap)
        }
    }

    private suspend fun loadImageFromFile(imagePath: String) = withContext(Dispatchers.IO) {
        BitmapFactory.decodeFile(imagePath)
    }

}
