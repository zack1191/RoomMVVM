package com.example.product.data.entities

import android.util.Base64
import androidx.room.ColumnInfo
import java.util.*

data class ProductVO(

    var productName:String,
    var price:String?,
    var qty:String?,
    var productImg:String?
    )
