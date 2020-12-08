package com.example.product.di

import android.content.Context
import androidx.room.Room
import com.example.product.data.databases.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule
{
    @Singleton
    @Provides
    fun appDatabase(@ApplicationContext context : Context) : AppDatabase
    {
        return Room.databaseBuilder(context.applicationContext, AppDatabase::class.java, "Product.db").build()
    }

    @Singleton
    @Provides
    fun productDao(database : AppDatabase) =
            database.productDao()
}