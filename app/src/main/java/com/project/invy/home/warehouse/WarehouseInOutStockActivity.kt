package com.project.invy.home.warehouse

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.project.invy.databinding.ActivityWarehouseInOutStockBinding
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.firebase.firestore.FirebaseFirestore
import com.project.invy.R
import java.text.SimpleDateFormat
import java.util.*


class WarehouseInOutStockActivity : AppCompatActivity() {

    private var binding: ActivityWarehouseInOutStockBinding? = null
    private var availableOrNot: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWarehouseInOutStockBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        val adapter = ArrayAdapter.createFromResource(
            this,
            R.array.available, android.R.layout.simple_list_item_1
        )
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        // Apply the adapter to the spinner
        binding?.availableOrNot?.setAdapter(adapter)
        binding?.availableOrNot?.setOnItemClickListener {_, _, _, _ ->
            availableOrNot = binding?.availableOrNot?.text.toString()
        }


        binding?.save?.setOnClickListener {
            formValidation()
        }

        binding?.date?.setOnClickListener {
            // munculkan kalendar dan user bisa memilih tanggal pada kalendar
            val datePicker = MaterialDatePicker.Builder.datePicker().setCalendarConstraints(
                CalendarConstraints.Builder().setValidator(DateValidatorPointForward.now()).build()).build()
            datePicker.show(supportFragmentManager, datePicker.toString())
            datePicker.addOnPositiveButtonClickListener { selection ->

                /// setelah tanggal dipilih, tutup kalendar dan ganti teks "Input Release Date" dengan tanggal yang di pilih user
                val sdf = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
                val getDateNow: String = sdf.format(Date(selection.toString().toLong()))
                binding?.date!!.text = getDateNow
            }
        }

        binding?.back?.setOnClickListener {
            onBackPressed()
        }

    }

    private fun formValidation() {
        val date = binding?.date?.text.toString()
        val name = binding?.name?.text.toString().trim()
        val total = binding?.total?.text.toString().trim()
        val vendor = binding?.productVendor?.text.toString().trim()

        when {
            date == "Tanggal" -> {
                Toast.makeText(this, "Anda harus mengisi tanggal", Toast.LENGTH_SHORT).show()
            }
            name.isEmpty() -> {
                Toast.makeText(this, "Nama Barang tidak boleh kosong", Toast.LENGTH_SHORT).show()
            }
            total.isEmpty() -> {
                Toast.makeText(this, "Jumlah Barang tidak boleh kosong", Toast.LENGTH_SHORT).show()
            }
            vendor.isEmpty() -> {
                Toast.makeText(this, "Nama Vendor tidak boleh kosong", Toast.LENGTH_SHORT).show()
            }
            availableOrNot == null -> {
                Toast.makeText(this, "Barang Tersedia atau Tidak? silahkan isi", Toast.LENGTH_SHORT).show()
            }
            else -> {
                binding?.progressBar?.visibility = View.VISIBLE
                val productId = System.currentTimeMillis().toString()

                val data = mapOf(
                    "productId" to productId,
                    "name" to name,
                    "vendor" to vendor,
                    "isAvailable" to availableOrNot,
                    "total" to total.toLong(),
                    "date" to date,
                )

                FirebaseFirestore
                    .getInstance()
                    .collection("product_available_or_not")
                    .document(productId)
                    .set(data)
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            binding?.progressBar?.visibility = View.GONE
                            showSuccessDialog()
                        } else {
                            binding?.progressBar?.visibility = View.GONE
                            showFailureDialog()
                        }
                    }
            }
        }

    }

    private fun showSuccessDialog() {
        AlertDialog.Builder(this)
            .setTitle("Sukses Menambahkan Daftar Produk")
            .setMessage("Produk sukses ditambahkan")
            .setIcon(R.drawable.ic_baseline_check_circle_outline_24)
            .setPositiveButton("OKE") { dialogInterface, _ ->
                dialogInterface.dismiss()
                onBackPressed()
            }
            .show()
    }

    private fun showFailureDialog() {
        AlertDialog.Builder(this)
            .setTitle("Gagal Menambahkan Daftar Produk")
            .setMessage("Silahkan periksa koneksi internet anda")
            .setIcon(R.drawable.ic_baseline_clear_24)
            .setPositiveButton("OKE") { dialogInterface, _ -> dialogInterface.dismiss() }
            .show()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}