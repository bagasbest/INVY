package com.project.invy.home.produce

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.project.invy.R
import com.project.invy.databinding.ItemProductBinding
import com.project.invy.home.warehouse.WarehouseInboxProofActivity

class ProduceAdapter(private val role: String) : RecyclerView.Adapter<ProduceAdapter.ViewHolder>() {

    private val productList = ArrayList<ProduceModel>()

    @SuppressLint("NotifyDataSetChanged")
    fun setData(items: ArrayList<ProduceModel>) {
        productList.clear()
        productList.addAll(items)
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val binding: ItemProductBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n", "NotifyDataSetChanged")
        fun bind(model: ProduceModel) {
            with(binding) {
                date.text = model.date
                name.text = model.name
                code.text = model.code

                when (role) {
                    "product" -> {
                        when (model.status) {
                            "Sedang di proses" -> {
                                option.visibility = View.VISIBLE
                                option.text = "Hapus"
                                reqItem.text = model.status
                            }
                            "Barang diterima" -> {
                                reqItem.text = "Sedang diproses"
                                finish.visibility = View.VISIBLE
                                finish.text = "Selesai"
                            }
                            "Barang sudah sampai" -> {
                                reqItem.text = "Barang sudah sampai"
                            }
                        }

                        finish.setOnClickListener {
                            model.productId?.let { it1 ->
                                FirebaseFirestore.getInstance()
                                    .collection("product")
                                    .document(it1)
                                    .update("status", "Barang sudah sampai")
                                    .addOnCompleteListener {
                                        reqItem.text = "Barang sudah sampai"
                                        binding.finish.visibility = View.GONE
                                        Toast.makeText(itemView.context, "Selesai", Toast.LENGTH_SHORT)
                                            .show()
                                    }
                            }
                        }

                        option.setOnClickListener {

                            /// munculkan konfirmasi sebelum mengapus produk ini
                            AlertDialog.Builder(itemView.context)
                                .setTitle("Konfirmasi Menghapus Pengajuan Produk")
                                .setMessage("Apakah anda yakin ingin menghapus pengajuan ini ?")
                                .setIcon(R.drawable.ic_baseline_warning_24)
                                .setPositiveButton("YA") { dialogInterface, _ ->

                                    dialogInterface.dismiss()
                                    /// proses hapus produk
                                    model.productId?.let { it1 ->
                                        FirebaseFirestore
                                            .getInstance()
                                            .collection("product")
                                            .document(it1)
                                            .delete()
                                            .addOnCompleteListener { task ->
                                                if (task.isSuccessful) {

                                                    productList.removeAt(adapterPosition)
                                                    notifyDataSetChanged()

                                                    Toast.makeText(
                                                        itemView.context,
                                                        "Berhasil menghapus pengajuan produk ini",
                                                        Toast.LENGTH_SHORT
                                                    )
                                                        .show()
                                                } else {
                                                    Toast.makeText(
                                                        itemView.context,
                                                        "Gagal menghapus pengajuan produk ini",
                                                        Toast.LENGTH_SHORT
                                                    )
                                                        .show()
                                                }
                                            }
                                    }

                                }
                                .setNegativeButton("TIDAK", null)
                                .show()
                        }
                    }
                    "warehouse" -> {
                        option.visibility = View.VISIBLE
                        option.text = "Lihat"
                        option.setOnClickListener {
                            val intent = Intent(itemView.context, WarehouseInboxProofActivity::class.java)
                            intent.putExtra(WarehouseInboxProofActivity.EXTRA_MODEL, model)
                            itemView.context.startActivity(intent)
                        }

                        if (model.status == "Sedang di proses") {
                            finish.visibility = View.VISIBLE
                            finish.text = "Terima"
                            finish.setOnClickListener {
                                model.productId?.let { it1 ->
                                    FirebaseFirestore
                                        .getInstance()
                                        .collection("product")
                                        .document(it1)
                                        .update("status", "Barang diterima")
                                        .addOnCompleteListener {
                                            finish.visibility = View.GONE
                                            Toast.makeText(itemView.context, "Berhasil menerima barang!", Toast.LENGTH_SHORT)
                                                .show()
                                        }
                                }
                            }
                        }
                    }
                    "produce" -> {
                        option.text = "LIHAT"
                        option.setOnClickListener {
                            val intent = Intent(itemView.context, WarehouseInboxProofActivity::class.java)
                            intent.putExtra(WarehouseInboxProofActivity.EXTRA_MODEL, model)
                            itemView.context.startActivity(intent)
                        }
                    }
                }
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(productList[position])
    }

    override fun getItemCount(): Int = productList.size
}