package dev.snipme.snipmeapp

import android.content.ClipData
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import androidx.core.content.ContextCompat.startActivity
import androidx.core.content.FileProvider
import dev.snipme.snipmeapp.domain.snippets.Snippet
import java.io.File
import java.io.FileNotFoundException
import java.text.DateFormat
import java.util.Date

const val imageMime = "image/png"

class AppService(private val context: Context) {
    private var imageUri: Uri? = null

    fun getCurrentDateFormatted(): String {
        return DateFormat.getDateInstance().format(Date())
    }

    fun storeFile(image: ByteArray, fileName: String, temp: Boolean = false) {
        val directoryFile = if (temp) context.cacheDir else context.getExternalFilesDir(
            Environment.DIRECTORY_PICTURES
        )

        if (directoryFile == null) throw IllegalStateException("Storage not available")
        if (!directoryFile.exists()) {
            directoryFile.mkdirs()
        }

        val imageFile = File(directoryFile, fileName)
        val imageUri = FileProvider.getUriForFile(
            context,
            "dev.snipme.snipmeapp.fileprovider",
            imageFile,
        )

        context.grantUriPermission(
            "dev.snipme.snipmeapp.fileprovider",
            imageUri,
            Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION
        )

        imageFile.writeBytes(image)
        this.imageUri = imageUri
    }

    fun launchShareIntent(snippet: Snippet) {
        if (imageUri == null)
            throw IllegalStateException("Image path is not set. Store image first!")

        val uri = imageUri!! // Store temporary to avoid var change
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            type = imageMime
            clipData = ClipData.newRawUri(snippet.title, uri)
            setDataAndType(uri, context.contentResolver.getType(uri));
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            putExtra(Intent.EXTRA_TITLE, snippet.title)
            putExtra(Intent.EXTRA_STREAM, uri)
        }

        val shareIntent = Intent.createChooser(sendIntent, null)
        shareIntent.addFlags(FLAG_ACTIVITY_NEW_TASK)
        startActivity(context, shareIntent, null)
    }

    fun storeMediaFile(image: ByteArray, name: String) {
        val directory = "Pictures/SnipMeApp"
        val contentValues = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, name)
            put(MediaStore.Images.Media.RELATIVE_PATH, directory)
            put(MediaStore.Images.Media.MIME_TYPE, imageMime)
        }

        val resolver = context.contentResolver
        val existingUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI.buildUpon()
            .appendQueryParameter(MediaStore.Images.Media.DISPLAY_NAME, name)
            .appendQueryParameter(MediaStore.Images.Media.RELATIVE_PATH, directory)
            .build()

        val cursor =
            resolver.query(existingUri, arrayOf(MediaStore.Images.Media._ID), null, null, null)
        val uri: Uri? = if (cursor != null && cursor.moveToFirst()) {
            val id = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID))
            Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id.toString())
        } else {
            resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
        }
        cursor?.close()

        if (uri != null) {
            resolver.openOutputStream(uri)?.use { outputStream ->
                outputStream.write(image)
            }
        } else {
            throw FileNotFoundException("Failed to create or update MediaStore record.")
        }
    }
}