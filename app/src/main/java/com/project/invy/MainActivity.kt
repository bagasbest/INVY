package com.project.invy

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.project.invy.databinding.ActivityMainBinding
import com.project.invy.home.HomeActivity
import com.project.invy.register.RegisterActivity

class MainActivity : AppCompatActivity() {

    private var binding: ActivityMainBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)


        autoLogin()

        binding?.register?.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        binding?.login?.setOnClickListener {
            formValidation()
        }
    }

    private fun autoLogin() {
        if(FirebaseAuth.getInstance().currentUser != null) {
            startActivity(Intent(this, HomeActivity::class.java))
            finish()
        }
    }

    private fun formValidation() {
        val username = binding?.username?.text.toString().trim()
        val password = binding?.password?.text.toString().trim()

        if(username.isEmpty()) {
            Toast.makeText(this, "Username tidak boleh kosong", Toast.LENGTH_SHORT).show()
            return
        } else if (password.isEmpty()) {
            Toast.makeText(this, "Password tidak boleh kosong", Toast.LENGTH_SHORT).show()
            return
        }

        binding?.progressCircular?.visibility = View.VISIBLE

        FirebaseFirestore
            .getInstance()
            .collection("users")
            .whereEqualTo("username", username)
            .limit(1)
            .get()
            .addOnCompleteListener{
                if (it.result.size() == 0) {
                    /// jika tidak terdapat di database dan email serta password, maka tidak bisa login
                    binding?.progressCircular?.visibility = View.GONE
                    showFailureDialog()
                    return@addOnCompleteListener
                }

                /// jika terdaftar maka ambil email di database, kemudian lakukan autentikasi menggunakan email & password dari user
                for (snapshot in it.result) {
                    val email = "" + snapshot["email"]

                    /// fungsi untuk mengecek, apakah email yang di inputkan ketika login sudah terdaftar di database atau belum
                    FirebaseAuth
                        .getInstance()
                        .signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                /// jika terdapat di database dan email serta password sama, maka masuk ke homepage
                                binding?.progressCircular?.visibility = View.GONE
                                startActivity(Intent(this, HomeActivity::class.java))
                            } else {
                                /// jika tidak terdapat di database dan email serta password, maka tidak bisa login
                                binding?.progressCircular?.visibility = View.GONE
                                showFailureDialog()
                            }
                        }
                }
            }
    }


    /// munculkan dialog ketika gagal login
    private fun showFailureDialog() {
        AlertDialog.Builder(this)
            .setTitle("Gagal melakukan login")
            .setMessage("Silahkan login kembali dengan informasi yang benar")
            .setIcon(R.drawable.ic_baseline_clear_24)
            .setPositiveButton("OKE") { dialogInterface, _ -> dialogInterface.dismiss() }
            .show()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}