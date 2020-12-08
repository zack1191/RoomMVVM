package com.example.product.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Product", primaryKeys = ["productName"])
data class Product(
        var productName:String,
        var price:String?,
        var qty:String?,
        @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
        var productImg:ByteArray?
                  )