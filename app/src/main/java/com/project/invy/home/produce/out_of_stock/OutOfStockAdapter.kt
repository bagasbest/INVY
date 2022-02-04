package com.project.invy.home.produce.out_of_stock

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.project.invy.databinding.ItemInOutStockBinding

class OutOfStockAdapter : RecyclerView.Adapter<OutOfStockAdapter.ViewHolder>() {

    private val productList = ArrayList<OutOfStockModel>()

    @SuppressLint("NotifyDataSetChanged")
    fun setData(items: ArrayList<OutOfStockModel>) {
        productList.clear()
        productList.addAll(items)
        notifyDataSetChanged()
    }


    inner class ViewHolder(private val binding: ItemInOutStockBinding) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(model: OutOfStockModel) {
            with(binding) {
                date.text = model.date
                productName.text = model.name
                total.text = "Total: ${model.total.toString()}"

                if(model.isAvailable == "Tersedia") {
                    status.text = "IN"
                } else {
                    status.text = "OUT"
                }
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemInOutStockBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(productList[position])
    }

    override fun getItemCount(): Int = productList.size
}