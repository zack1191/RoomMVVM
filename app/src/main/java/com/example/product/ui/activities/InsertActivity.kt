package com.example.product.ui.activities

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.product.R
import com.example.product.data.entities.Product
import com.example.product.ui_seperator.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_insert.*

@AndroidEntryPoint
class InsertActivity : AppCompatActivity()
{
    companion object
    {
        fun newIntent(context : Context) : Intent
        {
            return Intent(context, InsertActivity::class.java)
        }
    }

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
            val product = Product(etProductName.text.toString(), etPrice.text.toString(), etQty.text.toString(), "")
            mViewModel.insertData(product)
            Toast.makeText(applicationContext, "Saved!", Toast.LENGTH_SHORT).show()
        }
        ivCamera.setOnClickListener {
            val intent = CameraXActivity.newIntent(applicationContext)
            startActivity(intent)
        }
    }
}