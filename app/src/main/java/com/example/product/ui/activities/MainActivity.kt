package com.example.product.ui.activities

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.paging.PagedList
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.product.R
import com.example.product.data.entities.Product
import com.example.product.ui.adapters.ProductAdapter
import com.example.product.ui_seperator.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*

@AndroidEntryPoint
class MainActivity : AppCompatActivity()
{
    private val mViewModel : MainViewModel by viewModels()
    private lateinit var mProductAdapter : ProductAdapter
    override fun onCreate(savedInstanceState : Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mViewModel.getAllData.observe(this, Observer {
            it?.let {
                setupRecyclerView(it)
                Toast.makeText(applicationContext, it.size.toString(), Toast.LENGTH_SHORT).show()
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
        mProductAdapter.updateOnItemClickListener(object : ProductAdapter.UpdateOnItemClickListener
        {
            override fun updateOnItemClick(product : Product, position : Int)
            {
                Toast.makeText(applicationContext, position.toString(), Toast.LENGTH_SHORT).show()
            }
        })
    }
}