package com.project.invy.home

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.project.invy.MainActivity
import com.project.invy.R
import com.project.invy.databinding.ActivityHomeBinding
import com.project.invy.home.adm_produce.AdminActivity
import com.project.invy.home.produce.ProduceActivity
import com.project.invy.home.warehouse.WarehouseInboxActivity
import com.project.invy.home.warehouse.WarehouseProductInActivity
import com.project.invy.home.warehouse.WarehouseProductOutActivity

class HomeActivity : AppCompatActivity() {

    private var binding: ActivityHomeBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        checkRole()


        binding?.produce?.setOnClickListener {
            startActivity(Intent(this, ProduceActivity::class.java))
        }

        binding?.admin?.setOnClickListener {
            startActivity(Intent(this, AdminActivity::class.java))
        }

        binding?.productInWarehouse?.setOnClickListener {
            startActivity(Intent(this, WarehouseProductInActivity::class.java))
        }

        binding?.productOutWarehouse?.setOnClickListener {
            startActivity(Intent(this, WarehouseProductOutActivity::class.java))
        }

        binding?.inboxBarang?.setOnClickListener {
            startActivity(Intent(this, WarehouseInboxActivity::class.java))
        }


        binding?.productIn?.setOnClickListener {
            val intent = Intent(this, AdminActivity::class.java)
            intent.putExtra(AdminActivity.EXTRA_OPTION, "product_in")
            startActivity(intent)
        }

        binding?.productOut?.setOnClickListener {
            val intent = Intent(this, AdminActivity::class.java)
            intent.putExtra(AdminActivity.EXTRA_OPTION, "product_out")
            startActivity(intent)
        }

        binding?.permintaanBarang?.setOnClickListener {
            val intent = Intent(this, AdminActivity::class.java)
            intent.putExtra(AdminActivity.EXTRA_OPTION, "permintaan_barang")
            startActivity(intent)
        }

        binding?.logout?.setOnClickListener {
           showConfirmLogout()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun checkRole() {
        val uid = FirebaseAuth.getInstance().currentUser!!.uid
        FirebaseFirestore.getInstance()
            .collection("users")
            .document(uid)
            .get()
            .addOnSuccessListener {

                when ("" + it.data?.get("role")) {
                    "produce" -> {
                        binding?.produce?.visibility = View.VISIBLE
                        binding?.title?.text = "Produksi"
                    }
                    "admin" -> {
                        binding?.admin?.visibility = View.VISIBLE
                        binding?.title?.text = "Adm Produksi"

                        Glide.with(this)
                            .load(R.drawable.product_in)
                            .into(binding!!.productInImage)

                        Glide.with(this)
                            .load(R.drawable.product_out)
                            .into(binding!!.productOutImage)

                    }
                    else -> {
                        binding?.warehouse?.visibility = View.VISIBLE
                        binding?.title?.text = "gudang"


                        Glide.with(this)
                            .load(R.drawable.product_in)
                            .into(binding!!.productInImageWarehouse)

                        Glide.with(this)
                            .load(R.drawable.product_out)
                            .into(binding!!.productOutImageWarehouse)
                    }
                }
            }

    }

    /// konfirmasi logout
    private fun showConfirmLogout() {
        AlertDialog.Builder(this)
            .setTitle("Konfirmasi Logout")
            .setMessage("Apakah anda yakin ingin keluar ?")
            .setIcon(R.drawable.ic_baseline_warning_24)
            .setPositiveButton("YA") { dialogInterface, _ ->
                dialogInterface.dismiss()
                FirebaseAuth.getInstance().signOut()

                val intent = Intent(this, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                finish()
            }
            .setNegativeButton("TIDAK", null)
            .show()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}