package com.example.product.ui_seperator.repositories

import androidx.lifecycle.LiveData
import androidx.paging.LivePagedListBuilder
import com.example.product.data.daos.ProductDao
import com.example.product.data.entities.Product
import javax.inject.Inject

class MainRepository @Inject constructor(private val productDao : ProductDao)
{
    private val getProduct = productDao.getAllData()
    val productList = LivePagedListBuilder(getProduct, 10).build()
    val productListLiveData : LiveData<List<Product>> = productDao.getProductList()
    suspend fun insertData(product : Product)
    {
        productDao.insertData(product)
    }

    suspend fun delete(productName : String)
    {
        productDao.delete(productName)
    }

    suspend fun update(price : String, qty : String, productName : String)
    {
        productDao.update(price, qty, productName)
    }
}