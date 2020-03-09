package app.spidy.photoeditor2example

import android.content.Intent
import android.net.Uri
import java.io.IOException

class FileChooser(private val activity: MainActivity) {
    companion object {
        const val REQUEST_CODE = 51
    }

    fun open() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true)
        activity.startActivityForResult(intent, REQUEST_CODE)
    }

    fun read(uri: Uri): ByteArray {
        val inputStream = activity.contentResolver.openInputStream(uri)
            ?: throw IOException("Unable to obtain input stream from URI")
        return inputStream.readBytes()
    }
}