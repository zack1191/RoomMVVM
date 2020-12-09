package com.example.product.ui_seperator.viewmodels

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagedList
import com.example.product.data.entities.Product
import com.example.product.ui_seperator.repositories.MainRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel @ViewModelInject constructor(private val mainRepository : MainRepository, @Assisted val savedStateHandle : SavedStateHandle) : ViewModel()
{
    val getAllData : LiveData<PagedList<Product>> = mainRepository.productList
    val productListLiveData : LiveData<List<Product>> = mainRepository.productListLiveData
    fun insertData(product : Product) =
            viewModelScope.launch(Dispatchers.IO) {
                mainRepository.insertData(product)
            }

    fun delete(productName : String) =
            viewModelScope.launch(Dispatchers.IO) {
                mainRepository.delete(productName)
            }

    fun update(price : String, qty : String, productName : String) =
            viewModelScope.launch(Dispatchers.IO) {
                mainRepository.update(price, qty, productName)
            }
}