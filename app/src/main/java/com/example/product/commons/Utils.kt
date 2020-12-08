package com.example.product.commons

import android.Manifest
import android.annotation.TargetApi
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.graphics.drawable.BitmapDrawable
import android.media.ExifInterface
import android.os.Build
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import java.io.ByteArrayOutputStream

class Utils : AppCompatActivity()
{
    companion object
    {
        private const val requestCodes = 1

        fun getResizedBitmap(fileName : String, width : Int, height : Int) : Bitmap
        {
            val options = BitmapFactory.Options()
            options.inJustDecodeBounds = true
            val outHeight = options.outHeight
            val outWidth = options.outWidth
            var inSampleSize = 1

            if (outHeight > height || outWidth > width)
            {
                inSampleSize = if (outWidth > outHeight) outHeight / height else outWidth / width
            }

            options.inSampleSize = inSampleSize
            options.inJustDecodeBounds = false
            val bitmap = BitmapFactory.decodeFile(fileName, options)
            val newImgWidth = 800
            val newImgHeight = 800
            var rotateBitmap = Bitmap.createScaledBitmap(bitmap, newImgWidth, newImgHeight, true)
            val mtx : Matrix? = Matrix()
            val ei = ExifInterface(fileName)

            when (ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED))
            {
                ExifInterface.ORIENTATION_ROTATE_90 //portrait
                ->
                {
                    mtx !!.setRotate(90.toFloat())
                    rotateBitmap = Bitmap.createBitmap(rotateBitmap, 0, 0, rotateBitmap.width, rotateBitmap.height, mtx, true)
                    mtx.reset()
                }

                ExifInterface.ORIENTATION_ROTATE_180 // might need to flip horizontally too...
                ->
                {
                    mtx !!.setRotate(180.toFloat())
                    rotateBitmap = Bitmap.createBitmap(rotateBitmap, 0, 0, rotateBitmap.width, rotateBitmap.height, mtx, true)
                    mtx.reset()
                }

                else ->
                {
                    mtx !!.reset()
                }
            }
            return rotateBitmap
        }

        var myPermissions = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA)

        //Permission Requester
        @TargetApi(Build.VERSION_CODES.M)
        fun requestAppPermission(context : Context, selectPermission : Array<String>)
        {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            {
                if (! hasPermission(context, *myPermissions))
                {
                    ActivityCompat.requestPermissions(context as Activity, selectPermission, requestCodes)
                }
            }
        }

        //Permission Checker
        fun hasPermission(context : Context, vararg permissions : String) : Boolean =
                permissions.all() {
                    ActivityCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
                }

        fun convertImageToByteArray(imageView : ImageView) : ByteArray
        {
            val bitmap = (imageView.drawable as BitmapDrawable).bitmap
            val byteArrayOutputStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream)
            val byte = byteArrayOutputStream.toByteArray()
            return byte

        }

    }
}