package com.ikapurwanti.retrofitexample.presentation.product

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.ikapurwanti.retrofitexample.R
import com.ikapurwanti.retrofitexample.data.network.api.datasource.ProductDataSourceImpl
import com.ikapurwanti.retrofitexample.data.network.api.service.ProductService
import com.ikapurwanti.retrofitexample.data.repository.ProductRepository
import com.ikapurwanti.retrofitexample.data.repository.ProductRepositoryImpl
import com.ikapurwanti.retrofitexample.databinding.ActivityProductBinding
import com.ikapurwanti.retrofitexample.presentation.product.adapter.ProductListAdapter
import com.ikapurwanti.retrofitexample.utils.GenericViewModelFactory
import com.ikapurwanti.retrofitexample.utils.proceedWhen

class ProductActivity : AppCompatActivity() {

    private val binding : ActivityProductBinding by lazy {
        ActivityProductBinding.inflate(layoutInflater)
    }

    private val adapterProduct = ProductListAdapter()

    private val viewModel : ProductViewModel by viewModels {
        val service = ProductService.invoke()
        val dataSource = ProductDataSourceImpl(service)
        val repo : ProductRepository = ProductRepositoryImpl(dataSource)
        GenericViewModelFactory.create(ProductViewModel(repo))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setupRvListProduct()
        observeData()
        getData()
    }

    private fun setupRvListProduct() {
        binding.rvProduct.apply {
            layoutManager = LinearLayoutManager(this@ProductActivity)
            adapter = adapterProduct
            adapterProduct.refreshList()
        }
    }

    private fun observeData() {
        viewModel.productList.observe(this){
            it.proceedWhen(
                doOnSuccess = {
                    binding.rvProduct.isVisible = true
                    binding.layoutState.root.isVisible = false
                    binding.layoutState.pbLoading.isVisible = false
                    binding.layoutState.tvError.isVisible = false
                    binding.rvProduct.apply {
                        layoutManager = LinearLayoutManager(this@ProductActivity)
                        adapter = adapterProduct
                    }
                    it.payload?.let {
                        adapterProduct.setData(it.products)
                    }
                },
                doOnLoading = {
                    binding.layoutState.root.isVisible = true
                    binding.layoutState.pbLoading.isVisible = true
                    binding.layoutState.tvError.isVisible = false
                    binding.rvProduct.isVisible = false
                },
                doOnError = { err ->
                    binding.layoutState.root.isVisible = true
                    binding.layoutState.pbLoading.isVisible = false
                    binding.layoutState.tvError.isVisible = true
                    binding.layoutState.tvError.text = err.exception?.message.orEmpty()
                    binding.rvProduct.isVisible = false
                },
                doOnEmpty = {
                    binding.layoutState.root.isVisible = true
                    binding.layoutState.pbLoading.isVisible = false
                    binding.layoutState.tvError.isVisible = true
                    binding.layoutState.tvError.text = getString(R.string.text_product_list_data_empty)
                    binding.rvProduct.isVisible = false
                }
            )
        }
    }

    private fun getData() {
        viewModel.getProductList()
    }

}