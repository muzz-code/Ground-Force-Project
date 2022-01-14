package com.trapezoidlimited.groundforce.utils

import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

fun prepareFilePart(
    partName: String,
    fileUri: String
): MultipartBody.Part {

    var file = File(fileUri)

//         Out of scoped storage Implementation for android 10
//        if (Build.VERSION.SDK_INT < 29) {
//            file = File(fileUri)
//        } else if (Build.VERSION.SDK_INT >= 29) {
//            val uri = Uri.parse(fileUri)
//            val parcelFileDescriptor = context.contentResolver.openFileDescriptor(uri, "r", null)
//            parcelFileDescriptor?.let {
//                val inputStream = FileInputStream(parcelFileDescriptor.fileDescriptor)
//                file = File(context.cacheDir, context.contentResolver.getFileName(uri))
//                val outputStream = FileOutputStream(file)
//                IOUtils.copy(inputStream, outputStream)
//            }
//        }

    val fileRequestBody = file.asRequestBody("image/jpeg".toMediaType())
    return MultipartBody.Part.createFormData(partName, file.name, fileRequestBody)
}


fun createPartFromString(descriptionString: String?): RequestBody? {
    if (descriptionString == null)
        return null
    return descriptionString
        .toRequestBody("image/*".toMediaType())
}