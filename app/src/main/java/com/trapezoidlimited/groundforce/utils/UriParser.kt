package com.trapezoidlimited.groundforce.utils

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.BaseColumns
import android.provider.DocumentsContract
import android.provider.MediaStore

/**
 * Obtain the absolute path of the image based on Uri
 *
 * @param context context object
 * @param uri picture of Uri
 * @return If the image corresponding to Uri exists, return the absolute path of the image, otherwise return null
 */
fun getRealPathFromUri(context: Context, uri: Uri): String? {

    // Uri is divided into three parts. Domain name: hostname/path/id
    // content://media/extenral/images/media/17766
    // content://com.android.providers.media.documents/document/image:2706
    // file://com.xxxx.xxxxx ---- 7.0 has restrictions
    val sdkVersion = Build.VERSION.SDK_INT

    return if (sdkVersion >= 19) { // api >= 19
        getRealPathFromUriAboveApi19(context, uri)
    } else { // api < 19
        getRealPathFromUriBelowAPI19(context, uri)
    }
}

/**
 * Adapt to api19 (excluding api19), get the absolute path of the image according to uri
 *
 * @param context context object
 * @param uri picture of Uri
 * @return If the image corresponding to Uri exists, return the absolute path of the image, otherwise return null
 */
private fun getRealPathFromUriBelowAPI19(context: Context, uri: Uri): String? {
    val scheme: String? = uri.scheme
    var data: String? = null
    if (scheme == null) data = uri.path else if (ContentResolver.SCHEME_FILE == scheme) {
        data = uri.path
    } else if (ContentResolver.SCHEME_CONTENT == scheme) {
        data = getDataColumn(context, uri, null, null)
    }
    return data
}

/**
 * Adapt api19 and above, get the absolute path of the image according to uri
 *
 * @param context context object
 * @param uri picture of Uri
 * @return If the image corresponding to Uri exists, return the absolute path of the image, otherwise return null
 */
@SuppressLint("NewApi")
private fun getRealPathFromUriAboveApi19(context: Context, uri: Uri): String? {
    var filePath: String? = null
    if (DocumentsContract.isDocumentUri(context, uri)) {
        // If it is a document type uri, it is processed by document id
        val documentId = DocumentsContract.getDocumentId(uri)
        if (isMediaDocument(uri)) { // MediaProvider
            val divide = documentId.split(":").toTypedArray()
            val type = divide[0]
            var mediaUri: Uri? = null
            mediaUri = if ("image" == type) {
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            } else if ("video" == type) {
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI
            } else if ("audio" == type) {
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
            } else {
                return null
            }
            val selection = BaseColumns._ID + "=?"
            val selectionArgs = arrayOf(divide[1])
            filePath = getDataColumn(context, mediaUri, selection, selectionArgs)
        } else if (isDownloadsDocument(uri)) { // DownloadsProvider
            val contentUri: Uri = ContentUris.withAppendedId(
                Uri.parse("content://downloads/public_downloads"),
                java.lang.Long.valueOf(documentId)
            )
            filePath = getDataColumn(context, contentUri, null, null)
        } else if (isExternalStorageDocument(uri)) {
            val split = documentId.split(":").toTypedArray()
            if (split.size >= 2) {
                val type = split[0]
                if ("primary".equals(type, ignoreCase = true)) {
                    filePath = Environment.getExternalStorageDirectory().toString() + "/" + split[1]
                }
            }
        }
    } else if (ContentResolver.SCHEME_CONTENT.equals(uri.getScheme(), ignoreCase = true)) {
        // If it is a Uri of content type
        filePath = getDataColumn(context, uri, null, null)
    } else if (ContentResolver.SCHEME_FILE == uri.getScheme()) {
        // If it is a Uri of file type, directly get the path corresponding to the image
        filePath = uri.getPath()
    }
    return filePath
}

/**
 * Get the _data column in the database table, that is, return the file path corresponding to Uri
 * @return
 */
private fun getDataColumn(
    context: Context,
    uri: Uri?,
    selection: String?,
    selectionArgs: Array<String>?
): String? {
    var path: String? = null
    val projection = arrayOf(MediaStore.Images.Media.DATA)
    var cursor: Cursor? = null
    try {
        cursor = uri?.let {
            context.contentResolver.query(
                it,
                projection,
                selection,
                selectionArgs,
                null
            )
        }
        if (cursor != null && cursor.moveToFirst()) {
            val columnIndex: Int = cursor.getColumnIndexOrThrow(projection[0])
            path = cursor.getString(columnIndex)
        }
    } catch (e: Exception) {
        cursor?.close()
    }
    return path
}

/**
 * @param uri the Uri to check
 * @return Whether the Uri authority is MediaProvider
 */
private fun isMediaDocument(uri: Uri): Boolean {
    return "com.android.providers.media.documents" == uri.getAuthority()
}

/**
 * @param uri the Uri to check
 * @return Whether the Uri authority is DownloadsProvider
 */
private fun isDownloadsDocument(uri: Uri): Boolean {
    return "com.android.providers.downloads.documents" == uri.getAuthority()
}

private fun isExternalStorageDocument(uri: Uri): Boolean {
    return "com.android.externalstorage.documents" == uri.getAuthority()
}
