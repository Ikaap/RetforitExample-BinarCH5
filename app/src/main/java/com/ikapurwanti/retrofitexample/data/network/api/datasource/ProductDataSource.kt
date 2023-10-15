package com.ikapurwanti.retrofitexample.data.network.api.datasource

import com.ikapurwanti.retrofitexample.data.model.ProductsResponse
import com.ikapurwanti.retrofitexample.data.network.api.service.ProductService

interface ProductDataSource {
    suspend fun getProducts(): ProductsResponse
}

class ProductDataSourceImpl(private val service: ProductService) : ProductDataSource {
    override suspend fun getProducts(): ProductsResponse {
        return service.getProducts()
    }
}