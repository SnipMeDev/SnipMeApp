package dev.snipme.snipmeapp.domain.share

import android.content.ClipData
import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import androidx.core.content.ContextCompat.startActivity
import androidx.core.content.FileProvider
import dev.snipme.snipmeapp.domain.snippets.Snippet
import java.io.File

class ShareSnippetUseCase(
    private val context: Context
) {

    operator fun invoke(snippet: Snippet, image: ByteArray) {
        val imageFile = File(context.cacheDir, "code.png")
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

        println("Image: ${image.size}")
        println("ImageFile: $imageFile")

        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TITLE, snippet.title)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            putExtra(Intent.EXTRA_TEXT, snippet.code.raw)
            putExtra(Intent.EXTRA_SUBJECT, snippet.language.raw)
            putExtra(Intent.EXTRA_STREAM, imageUri)
            type = "image/*"
            setDataAndType(imageUri, context.getContentResolver().getType(imageUri));
            clipData = ClipData.newRawUri(snippet.title, imageUri)
        }

        val shareIntent = Intent.createChooser(sendIntent, snippet.title)
        shareIntent.addFlags(FLAG_ACTIVITY_NEW_TASK)
        startActivity(context, shareIntent, null)
    }
}