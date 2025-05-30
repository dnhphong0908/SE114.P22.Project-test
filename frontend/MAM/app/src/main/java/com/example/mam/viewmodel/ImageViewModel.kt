package com.example.mam.viewmodel

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import android.util.Log
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

class ImageViewModel() {
    fun prepareFilePart(filePath: String): MultipartBody.Part {
        val file = File(filePath)
        val requestBody = file.asRequestBody("image/*".toMediaTypeOrNull())
        return MultipartBody.Part.createFormData("file", file.name, requestBody)
    }

    fun getMultipartFromUri(context: Context, uri: Uri): MultipartBody.Part? {
        return try {
            val inputStream = context.contentResolver.openInputStream(uri) ?: return null
            val file = File(context.cacheDir, "upload.jpg")

            inputStream.use { input ->
                file.outputStream().use { output -> input.copyTo(output) }
            }

            val requestBody = file.asRequestBody("image/*".toMediaTypeOrNull())
            MultipartBody.Part.createFormData("image", file.name, requestBody)
        } catch (e: Exception) {
            Log.e("Upload", "Error converting Uri to Multipart: ${e.message}")
            null
        }
    }
    fun uriToFile(context: Context, uri: Uri): File? {
        val contentResolver = context.contentResolver
        val fileName = getFileNameFromUri(context, uri) ?: "temp_file"

        val tempFile = File.createTempFile(fileName, null, context.cacheDir)
        tempFile.outputStream().use { output ->
            contentResolver.openInputStream(uri)?.use { input ->
                input.copyTo(output)
            } ?: return null
        }
        return tempFile
    }
    fun getFileNameFromUri(context: Context, uri: Uri): String? {
        var name: String? = null
        if (uri.scheme == ContentResolver.SCHEME_CONTENT) {
            context.contentResolver.query(uri, null, null, null, null)?.use { cursor ->
                val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                if (cursor.moveToFirst() && nameIndex != -1) {
                    name = cursor.getString(nameIndex)
                }
            }
        }
        if (name == null) {
            name = uri.lastPathSegment?.substringAfterLast('/')
        }
        return name
    }
}