package com.example.product.data.databases

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.product.data.daos.ProductDao
import com.example.product.data.entities.Product

@Database(entities = [Product::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase()
{
    abstract fun productDao() : ProductDao
}