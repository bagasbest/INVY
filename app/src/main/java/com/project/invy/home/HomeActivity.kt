package com.project.invy.home

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.project.invy.MainActivity
import com.project.invy.R
import com.project.invy.databinding.ActivityHomeBinding
import com.project.invy.home.adm_produce.AdminActivity
import com.project.invy.home.produce.ProduceActivity

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

        binding?.logout?.setOnClickListener {
            FirebaseAuth.getInstance().signOut()

            val intent = Intent(this, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish()
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
                val role = "" + it.data?.get("role")

                when (role) {
                    "produce" -> {
                        binding?.produce?.visibility = View.VISIBLE
                        binding?.title?.text = "Produksi"
                    }
                    "adm produce" -> {
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
                        binding?.title?.text = "Gudang"


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

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}