package com.example.product.ui.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.product.R
import com.example.product.data.entities.Product

class ProductAdapter : PagedListAdapter<Product, ProductAdapter.ProductViewHolder>(DIFF_CALLBACK)
{
    companion object
    {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Product>()
        {
            // Concert details may have changed if reloaded from the data,
            // but ID is fixed.
            override fun areItemsTheSame(oldItem : Product, newItem : Product) =
                    oldItem.productName == newItem.productName

            override fun areContentsTheSame(oldItem : Product, newItem : Product) =
                    oldItem == newItem
        }
    }

    private var itemClickListener : CustomOnItemClickListener? = null
    private var updateItemClickListener : UpdateOnItemClickListener? = null

    inner class ProductViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView)
    {
        @SuppressLint("CutPasteId")
        fun bindData(product : Product?)
        {
            val productName = itemView.findViewById<TextView>(R.id.tvProductName)
            val price = itemView.findViewById<TextView>(R.id.tvPrice)
            val qty = itemView.findViewById<TextView>(R.id.tvQty)
            val image = itemView.findViewById<ImageView>(R.id.imageView)
            val delete = itemView.findViewById<ImageView>(R.id.ivDelete)
            val update = itemView.findViewById<ImageView>(R.id.ivUpdate)
            product?.let {
                productName.text = product.productName
                price.text = product.price
                qty.text = product.qty
            }
            delete.setOnClickListener {
                product?.let {
                    itemClickListener !!.customOnItemClick(product, adapterPosition)
                }
            }
            update.setOnClickListener {
                product?.let {
                    updateItemClickListener !!.updateOnItemClick(product, adapterPosition)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent : ViewGroup, viewType : Int) : ProductViewHolder
    {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_product, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder : ProductViewHolder, position : Int)
    {
        val product = getItem(position)
        product?.let { product ->
            holder.bindData(product)
        }
    }

    interface CustomOnItemClickListener
    {
        fun customOnItemClick(product : Product, position : Int)
    }

    interface UpdateOnItemClickListener
    {
        fun updateOnItemClick(product : Product, position : Int)
    }

    fun customSetOnItemClickListener(listener : CustomOnItemClickListener)
    {
        this.itemClickListener = listener
    }

    fun updateOnItemClickListener(listener : UpdateOnItemClickListener)
    {
        this.updateItemClickListener = listener
    }
}
