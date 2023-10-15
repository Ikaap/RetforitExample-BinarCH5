package com.ikapurwanti.retrofitexample.presentation.product

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ikapurwanti.retrofitexample.data.model.ProductsResponse
import com.ikapurwanti.retrofitexample.data.network.api.service.ProductService
import com.ikapurwanti.retrofitexample.data.repository.ProductRepository
import com.ikapurwanti.retrofitexample.utils.ResultWrapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProductViewModel(
    private val productRepo: ProductRepository
) : ViewModel(){

     val productList = MutableLiveData<ResultWrapper<ProductsResponse>>()

    fun getProductList(){
        viewModelScope.launch(Dispatchers.IO) {
            productRepo.getProducts().collect{
                productList.postValue(it)
            }
        }
    }

}