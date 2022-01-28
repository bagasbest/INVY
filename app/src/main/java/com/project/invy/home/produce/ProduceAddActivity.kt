package com.project.invy.home.produce

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.project.invy.databinding.ActivityProduceAddBinding
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.firebase.firestore.FirebaseFirestore
import com.project.invy.R
import java.text.SimpleDateFormat
import java.util.*


class ProduceAddActivity : AppCompatActivity() {
    private var binding: ActivityProduceAddBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProduceAddBinding.inflate(layoutInflater)
        setContentView(binding?.root)


        binding?.back?.setOnClickListener {
            onBackPressed()
        }

        binding?.button?.setOnClickListener {
            formValidation()
        }

        binding?.date?.setOnClickListener {
            // munculkan kalendar dan user bisa memilih tanggal pada kalendar
            val datePicker = MaterialDatePicker.Builder.datePicker().setCalendarConstraints(CalendarConstraints.Builder().setValidator(DateValidatorPointForward.now()).build()).build()
            datePicker.show(supportFragmentManager, datePicker.toString())
            datePicker.addOnPositiveButtonClickListener { selection ->

                /// setelah tanggal dipilih, tutup kalendar dan ganti teks "Input Release Date" dengan tanggal yang di pilih user
                val sdf = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
                val getDateNow: String = sdf.format(Date(selection.toString().toLong()))
                binding?.date!!.text = getDateNow
            }
        }


    }

    private fun formValidation() {
        val name = binding?.name?.text.toString().trim()
        val code = binding?.code?.text.toString().trim()
        val uraian = binding?.uraian?.text.toString().trim()
        val invCode = binding?.invCode?.text.toString().trim()
        val satuan = binding?.satuan?.text.toString().trim()
        val total = binding?.total?.text.toString().trim()
        val keterangan = binding?.keterangan?.text.toString().trim()

        when {
            name.isEmpty() -> {
                Toast.makeText(this, "Nama Barang tidak boleh kosong", Toast.LENGTH_SHORT).show()
            }
            code.isEmpty() -> {
                Toast.makeText(this, "Kode Barang tidak boleh kosong", Toast.LENGTH_SHORT).show()
            }
            uraian.isEmpty() -> {
                Toast.makeText(this, "Uraian tidak boleh kosong", Toast.LENGTH_SHORT).show()
            }
            invCode.isEmpty() -> {
                Toast.makeText(this, "Invoice Code tidak boleh kosong", Toast.LENGTH_SHORT).show()
            }
            satuan.isEmpty() -> {
                Toast.makeText(this, "Satuan tidak boleh kosong", Toast.LENGTH_SHORT).show()
            }
            total.isEmpty() -> {
                Toast.makeText(this, "Total Barang tidak boleh kosong", Toast.LENGTH_SHORT).show()
            }
            keterangan.isEmpty() -> {
                Toast.makeText(this, "Keterangan tidak boleh kosong", Toast.LENGTH_SHORT).show()
            }
            else -> {

                binding?.progressBar?.visibility = View.VISIBLE
                val productId = System.currentTimeMillis().toString()

                val data = mapOf(
                    "productId" to productId,
                    "name" to name,
                    "code" to code,
                    "uraian" to uraian,
                    "invCode" to invCode,
                    "satuan" to satuan.toLong(),
                    "total" to total.toLong(),
                    "keterangan" to keterangan,
                    
                )

                FirebaseFirestore
                    .getInstance()
                    .collection("product")
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


    /// munculkan dialog ketika sukses menambahkan produk baru
    private fun showSuccessDialog() {
        AlertDialog.Builder(this)
            .setTitle("Sukses Menambahkan Produk Baru")
            .setMessage("Produk akan muncul di halaman produksi")
            .setIcon(R.drawable.ic_baseline_check_circle_outline_24)
            .setPositiveButton("OKE") { dialogInterface, _ ->
                dialogInterface.dismiss()
                onBackPressed()
            }
            .show()
    }

    /// munculkan dialog ketika gagal login
    private fun showFailureDialog() {
        AlertDialog.Builder(this)
            .setTitle("Gagal Menambahkan Produk Baru")
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