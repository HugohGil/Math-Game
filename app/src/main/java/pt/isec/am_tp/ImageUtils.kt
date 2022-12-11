package pt.isec.am_tp

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.view.View
import android.widget.ImageView
import java.io.File
import java.io.FileOutputStream
import kotlin.math.max
import kotlin.math.min

fun setImage(view: View, path: String) {
    getImage(view,path)?.also {
        when {
            view is ImageView -> (view as ImageView).setImageBitmap(it)
            else -> view.background = BitmapDrawable(view.resources, it)
        }
    }
}
fun getImage(view: View, path: String) : Bitmap? {
    val targetWidth = view.width
    val targetHeight = view.height
    if (targetHeight < 1 || targetWidth < 1)
        return null
    val bmpOptions = BitmapFactory.Options()
    bmpOptions.inJustDecodeBounds = true
    BitmapFactory.decodeFile(path, bmpOptions)
    val photoWidth = bmpOptions.outWidth
    val photoHeight = bmpOptions.outHeight
    val scale = max(1, min(photoWidth / targetWidth, photoHeight / targetHeight))
    bmpOptions.inSampleSize = scale
    bmpOptions.inJustDecodeBounds = false
    return BitmapFactory.decodeFile(path, bmpOptions)
}

fun getTempFilename(context: Context,
                    prefix : String = "image",
                    extension : String = ".img"
) : String =
    File.createTempFile(
        prefix, extension,
        context.externalCacheDir //context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
    ).absolutePath

fun createFileFromUri(
    context: Context,
    uri : Uri,
    filename : String = getTempFilename(context)
) : String {
    FileOutputStream(filename).use { outputStream ->
        context.contentResolver.openInputStream(uri)?.use { inputStream ->
            inputStream.copyTo(outputStream)
        }
    }
    return filename
}