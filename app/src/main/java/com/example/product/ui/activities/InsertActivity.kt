package com.example.product.ui.activities

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.example.product.BuildConfig
import com.example.product.R
import com.example.product.commons.Utils
import com.example.product.data.entities.Product
import com.example.product.ui_seperator.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_insert.*
import java.io.File
import java.lang.Exception

@AndroidEntryPoint
class InsertActivity : AppCompatActivity()
{
    companion object
    {
        fun newIntent(context : Context) : Intent
        {
            return Intent(context, InsertActivity::class.java)
        }

        private const val REQUEST_IMAGE_CAPTURE = 1
    }

    private var photo : ByteArray? = null
    private lateinit var file : File
    private lateinit var dir : File
    private val mViewModel : MainViewModel by viewModels()

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState : Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_insert)
        onClick()
    }

    private fun onClick()
    {
        btnInsert.setOnClickListener {

            ivProduct.drawable?.let {
                photo = Utils.convertImageToByteArray(ivProduct) //photo
            }
            val product = Product(etProductName.text.toString(), etPrice.text.toString(), etQty.text.toString(), photo)
            mViewModel.insertData(product)
            Toast.makeText(applicationContext, "Saved!", Toast.LENGTH_SHORT).show()
        }
        ivCamera.setOnClickListener {
            Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
                takePictureIntent.resolveActivity(packageManager)?.also {
                    try
                    {
                        dir = File("/sdcard/Product/")

                        val storedPath = "myPhoto.png" //to edit
                        file = File(dir, storedPath)
                        if (! dir.exists())
                        {
                            dir.mkdir()
                        }

                        file.createNewFile()
                        val uri = FileProvider.getUriForFile(applicationContext, BuildConfig.APPLICATION_ID, file)
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri)

                        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
                    }
                    catch (ex : Exception)
                    {
                        Toast.makeText(applicationContext, ex.toString(), Toast.LENGTH_SHORT).show()
                    }

                }
            }
        }
    }

    override fun onActivityResult(requestCode : Int, resultCode : Int, data : Intent?)
    {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK)
        {
            val scanIntent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
            scanIntent.data = Uri.fromFile(file)
            sendBroadcast(scanIntent)
            val width = resources.displayMetrics.widthPixels
            val height = resources.displayMetrics.heightPixels
            val bitmap = Utils.getResizedBitmap(file.path, width, height)
            ivProduct.setImageBitmap(bitmap)
        }

    }
}