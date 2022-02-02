package com.project.invy.home.warehouse

import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.bumptech.glide.Glide
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.project.invy.R
import com.project.invy.databinding.ActivityWarehouseProductOutBinding
import java.text.SimpleDateFormat
import java.util.*

class WarehouseProductOutActivity : AppCompatActivity() {

    private var binding: ActivityWarehouseProductOutBinding? = null
    private var dp: String? = null
    private val REQUEST_FROM_GALLERY = 1001

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWarehouseProductOutBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        binding?.back?.setOnClickListener {
            onBackPressed()
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

        binding?.save?.setOnClickListener {
            formValidation()
        }

        // KLIK TAMBAH GAMBAR
        binding?.imageHint?.setOnClickListener {
            ImagePicker.with(this)
                .galleryOnly()
                .compress(1024)
                .start(REQUEST_FROM_GALLERY);
        }
    }

    private fun formValidation() {
        val date = binding?.date?.text.toString()
        val name = binding?.name?.text.toString().trim()
        val total = binding?.total?.text.toString().trim()
        val productVendor = binding?.productVendor?.text.toString().trim()
        val keterangan = binding?.keterangan?.text.toString().trim()

        if(date == "Date") {
            Toast.makeText(this, "Tanggal Barang Keluar tidak boleh kosong", Toast.LENGTH_SHORT).show()
        } else if (name.isEmpty()) {
            Toast.makeText(this, "Nama Barang Keluar tidak boleh kosong", Toast.LENGTH_SHORT).show()
        } else if (total.isEmpty()) {
            Toast.makeText(this, "Total Barang Keluar tidak boleh kosong", Toast.LENGTH_SHORT).show()
        } else if (productVendor.isEmpty()) {
            Toast.makeText(this, "Nama Vendor Keluar Masuk tidak boleh kosong", Toast.LENGTH_SHORT).show()
        } else if (keterangan.isEmpty()) {
            Toast.makeText(this, "Keterangan Barang Keluar tidak boleh kosong", Toast.LENGTH_SHORT).show()
        } else if (dp == null) {
            Toast.makeText(this, "Bukti Penerimaan Barang Keluar tidak boleh kosong", Toast.LENGTH_SHORT).show()
        } else {
            binding?.progressBar?.visibility = View.VISIBLE
            val productId = System.currentTimeMillis().toString()

            val data = mapOf(
                "productId" to productId,
                "name" to name,
                "date" to date,
                "total" to total.toLong(),
                "productVendor" to productVendor,
                "keterangan" to keterangan,
                "image" to dp,
            )

            FirebaseFirestore
                .getInstance()
                .collection("product_out")
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

    /// tampilkan dialog box ketika gagal menambahkan barang Keluar
    private fun showFailureDialog() {
        AlertDialog.Builder(this)
            .setTitle("Gagal")
            .setMessage("Gagal menambahkan penerimaan barang Keluar, silahkan periksa koneksi internet anda, dan coba lagi nanti")
            .setIcon(R.drawable.ic_baseline_clear_24)
            .setPositiveButton("OKE") { dialogInterface, _ ->
                dialogInterface.dismiss()
            }
            .show()
    }

    /// tampilkan dialog box ketika sukses menambahkan barang Keluar
    private fun showSuccessDialog() {
        AlertDialog.Builder(this)
            .setTitle("Berhasil")
            .setMessage("Sukses menambahkan penerimaan barang Keluar, admin akan mengeceknya.")
            .setIcon(R.drawable.ic_baseline_check_circle_outline_24)
            .setPositiveButton("OKE") { dialogInterface, _ ->
                dialogInterface.dismiss()
                onBackPressed()
            }
            .show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_FROM_GALLERY) {
                uploadArticleDp(data?.data)
            }
        }
    }


    /// fungsi untuk mengupload foto kedalam cloud storage
    private fun uploadArticleDp(data: Uri?) {
        val mStorageRef = FirebaseStorage.getInstance().reference
        val mProgressDialog = ProgressDialog(this)
        mProgressDialog.setMessage("Mohon tunggu hingga proses selesai...")
        mProgressDialog.setCanceledOnTouchOutside(false)
        mProgressDialog.show()
        val imageFileName = "product_out/image_" + System.currentTimeMillis() + ".png"
        mStorageRef.child(imageFileName).putFile(data!!)
            .addOnSuccessListener {
                mStorageRef.child(imageFileName).downloadUrl
                    .addOnSuccessListener { uri: Uri ->
                        mProgressDialog.dismiss()
                        dp = uri.toString()
                        Glide
                            .with(this)
                            .load(dp)
                            .into(binding!!.image)
                    }
                    .addOnFailureListener { e: Exception ->
                        mProgressDialog.dismiss()
                        Toast.makeText(
                            this,
                            "Gagal mengunggah gambar",
                            Toast.LENGTH_SHORT
                        ).show()
                        Log.d("imageDp: ", e.toString())
                    }
            }
            .addOnFailureListener { e: Exception ->
                mProgressDialog.dismiss()
                Toast.makeText(
                    this,
                    "Gagal mengunggah gambar",
                    Toast.LENGTH_SHORT
                )
                    .show()
                Log.d("imageDp: ", e.toString())
            }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}