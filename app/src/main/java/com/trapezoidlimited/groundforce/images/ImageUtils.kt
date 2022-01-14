package com.trapezoidlimited.groundforce.images

import android.graphics.Bitmap
import java.io.ByteArrayOutputStream

fun getBytesFromBitmap(bitmap: Bitmap): ByteArray {
    val stream = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream)
    return stream.toByteArray()
}

fun getStringByByte(bytes: ByteArray?): String {
    val ret = StringBuilder()
    if (bytes != null) {
        for (b in bytes) {
            ret.append(Integer.toBinaryString(b.toInt() and 255 or 256).substring(1))
        }
    }
    return ret.toString()
}