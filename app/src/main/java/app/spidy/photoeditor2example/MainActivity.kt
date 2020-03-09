package app.spidy.photoeditor2example

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import app.spidy.photoeditor2.core.*
import java.io.File
import java.io.FileOutputStream

class MainActivity : AppCompatActivity() {
    private lateinit var editor: PhotoEditor
    private lateinit var editorView: PhotoEditorView
    private lateinit var saveBtn: Button
    private lateinit var fileChooser: FileChooser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        PermissionHandler.requestStorage(this, "") {}

        editorView = findViewById(R.id.editorView)
        editor = PhotoEditor.Builder(this, editorView).build()
        saveBtn = findViewById(R.id.saveBtn)
        fileChooser = FileChooser(this)

        editorView.source?.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.got_s))

        val textStyle = TextStyleBuilder()
            .withTextColor(Color.WHITE)
            .withTextOutline(Color.RED, 5)
            .withOpacity(0.5f)
            .withTextSize(50f)

        editor.addText("Hello, world", textStyle)


        saveBtn.setOnClickListener {
            editor.saveAsBitmap(object : OnSaveBitmap {
                override fun onBitmapReady(saveBitmap: Bitmap?) {
                    saveBitmap?.also {
                        val dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)!!
                        val file = File(dir.absolutePath, "test.png")
                        val fileOutputStream = FileOutputStream(file)
                        it.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream)
                        fileOutputStream.flush()
                        fileOutputStream.close()

                        Toast.makeText(this@MainActivity, "Saved!", Toast.LENGTH_SHORT).show()
                    }
                }
                override fun onFailure(e: Exception?) {

                }
            })
        }


        editor.setOnPhotoEditorListener(object : OnPhotoEditorListener {
            override fun onTextLongPress(rootView: View?, text: String?, colorCode: Int) {
                if (rootView != null) {
                    editor.editText(rootView, "Jeeva kumar", textStyle)
                }
                fileChooser.open()
                Toast.makeText(this@MainActivity, "Long press", Toast.LENGTH_SHORT).show()
                editor.isRotationEnabled = true
            }
            override fun onStartViewChangeListener(viewType: ViewType?) {}
            override fun onRemoveViewListener(viewType: ViewType?, numberOfAddedViews: Int) {}
            override fun onAddViewListener(viewType: ViewType?, numberOfAddedViews: Int) {}
            override fun onStopViewChangeListener(viewType: ViewType?) {}
            override fun onImageAddListener(imageView: ImageView) {

            }
            override fun onImageLongPress(imageView: ImageView) {
                imageView.alpha = .5f
            }
        })
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode != FileChooser.REQUEST_CODE || resultCode != RESULT_OK) {
            return super.onActivityResult(requestCode, resultCode, data)
        }

        data?.data?.also {
            val bytes = fileChooser.read(it)
            val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
            editor.addImage(bitmap)
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == PermissionHandler.STORAGE_PERMISSION_CODE ||
            requestCode == PermissionHandler.LOCATION_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                PermissionHandler.execute()
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}