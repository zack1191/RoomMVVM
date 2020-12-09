package com.example.product.data.daos

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.product.data.entities.Product

@Dao
interface ProductDao
{
    @Query("SELECT * FROM Product")
    fun getAllData() : androidx.paging.DataSource.Factory<Int, Product>

    @Query("SELECT * FROM Product")
    fun getProductList() : LiveData<List<Product>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertData(product : Product)

    @Query("Delete from Product where productName=:productName")
    suspend fun delete(productName : String)

    @Query("Update Product SET price=:price , qty=:qty WHERE productName=:productName")
    suspend fun update(price : String, qty : String, productName : String)
}