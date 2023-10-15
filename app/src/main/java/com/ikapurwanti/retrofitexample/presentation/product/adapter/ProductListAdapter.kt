package com.ikapurwanti.retrofitexample.presentation.product.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import coil.load
import com.ikapurwanti.retrofitexample.core.ViewHolderBinder
import com.ikapurwanti.retrofitexample.data.model.Product
import com.ikapurwanti.retrofitexample.databinding.ItemProductBinding

class ProductListAdapter: RecyclerView.Adapter<ProductItemListViewHolder>() {

    private val dataDiffer =
        AsyncListDiffer(this, object : DiffUtil.ItemCallback<Product>() {
            override fun areItemsTheSame(
                oldItem: Product,
                newItem: Product
            ): Boolean {
                return oldItem.id == newItem.id

            }

            override fun areContentsTheSame(
                oldItem: Product,
                newItem: Product
            ): Boolean {
                return oldItem.hashCode() == newItem.hashCode()
            }

        })


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductItemListViewHolder {
        return ProductItemListViewHolder(
            binding = ItemProductBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = dataDiffer.currentList.size

    override fun onBindViewHolder(holder: ProductItemListViewHolder, position: Int) {
        holder.bind(dataDiffer.currentList[position])
    }

    fun setData(data: List<Product>) {
        dataDiffer.submitList(data)
    }

    fun refreshList() {
        notifyItemRangeChanged(0, dataDiffer.currentList.size)
    }
}


class ProductItemListViewHolder(
    private val binding: ItemProductBinding
) : ViewHolder(binding.root), ViewHolderBinder<Product> {
    override fun bind(product: Product) {

        with(binding) {
            ivProductImage.load(product.images[0])
            tvProductTitle.text = product.title
            tvProductDesc.text = product.desc
            tvProductPrice.text = "IDR ${product.price}"

        }
    }
}