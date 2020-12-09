package com.example.product.ui.activities

import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.paging.PagedList
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.product.R
import com.example.product.commons.Utils
import com.example.product.data.entities.Product
import com.example.product.data.entities.ProductVO
import com.example.product.ui.adapters.ProductAdapter
import com.example.product.ui_seperator.viewmodels.MainViewModel
import com.google.firebase.database.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

@AndroidEntryPoint
class MainActivity : AppCompatActivity()
{
    private val mViewModel : MainViewModel by viewModels()
    private lateinit var mProductAdapter : ProductAdapter
    private lateinit var databaseReference : DatabaseReference
    override fun onCreate(savedInstanceState : Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (Build.VERSION.SDK_INT >= 23)
        {
            Utils.requestAppPermission(this, Utils.myPermissions)
        }
        mViewModel.getAllData.observe(this, Observer {
            it?.let {
                setupRecyclerView(it)
            }
        })

        floatingActionButton.setOnClickListener {
            val intent = InsertActivity.newIntent(applicationContext)
            startActivity(intent)
        }
    }

    private fun setupRecyclerView(productList : PagedList<Product>)
    {
        mProductAdapter = ProductAdapter()
        rvProduct.layoutManager = LinearLayoutManager(this)
        rvProduct.adapter = mProductAdapter
        mProductAdapter.submitList(productList)
        mProductAdapter.customSetOnItemClickListener(object : ProductAdapter.CustomOnItemClickListener
        {
            override fun customOnItemClick(product : Product, position : Int)
            {
                mViewModel.delete(product.productName)
            }
        })
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Product")
        mProductAdapter.updateOnItemClickListener(object : ProductAdapter.UpdateOnItemClickListener
        {
            override fun updateOnItemClick(product : Product, position : Int)
            {
                val dialog : AlertDialog
                val builder = AlertDialog.Builder(this@MainActivity)
                val dialogView = layoutInflater.inflate(R.layout.update_item, null)
                val etUpdatePrice = dialogView.findViewById<EditText>(R.id.etUpdatePrice)
                val etUpdateQty = dialogView.findViewById<EditText>(R.id.etUpdateQty)
                val updateButton = dialogView.findViewById<Button>(R.id.btnUpdate)
                builder.setView(dialogView)
                builder.setCancelable(true)
                dialog = builder.create()
                dialog.show()
                updateButton.setOnClickListener {
                    mViewModel.update(etUpdatePrice.text.toString(), etUpdateQty.text.toString(), product.productName)
                    dialog.dismiss()
                }

            }
        })
    }

    override fun onCreateOptionsMenu(menu : Menu?) : Boolean
    {
        menuInflater.inflate(R.menu.product_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onOptionsItemSelected(item : MenuItem) : Boolean
    {

        return when (item.itemId)
        {
            R.id.syncToFirebase ->
            {

                mViewModel.productListLiveData.observe(this, Observer {
                    for (i in 0 until it.size)
                    {
                        val base64Encoded = Base64.getEncoder().encodeToString(it[i].productImg)
                        val product = ProductVO(it[i].productName, it[i].price, it[i].qty, base64Encoded)
                        databaseReference.push().setValue(product) //cant save byte array object to firebase
                    }
                })
                Toast.makeText(applicationContext, "Data Synced!", Toast.LENGTH_SHORT).show()
                true
            }

            else -> false
        }
    }
}