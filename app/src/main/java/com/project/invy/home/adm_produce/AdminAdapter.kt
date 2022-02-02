package com.project.invy.home.adm_produce

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.project.invy.databinding.ItemInOutBinding

class AdminAdapter : RecyclerView.Adapter<AdminAdapter.ViewHolder>() {

    private val productList = ArrayList<AdminModel>()

    @SuppressLint("NotifyDataSetChanged")
    fun setData(items: ArrayList<AdminModel>) {
        productList.clear()
        productList.addAll(items)
        notifyDataSetChanged()
    }


    inner class ViewHolder(private val binding: ItemInOutBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(model: AdminModel) {
            with(binding) {
                date.text = model.date
                productName.text = model.name
                productVendor.text = model.productVendor

                see.setOnClickListener {
                    val intent = Intent(itemView.context, ProofActivity::class.java)
                    intent.putExtra(ProofActivity.EXTRA_IMAGE, model.image)
                    itemView.context.startActivity(intent)
                }
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemInOutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(productList[position])
    }

    override fun getItemCount(): Int = productList.size
}