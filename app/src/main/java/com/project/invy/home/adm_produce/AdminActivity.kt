package com.project.invy.home.adm_produce

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.project.invy.databinding.ActivityAdminBinding

class AdminActivity : AppCompatActivity() {

    private var binding: ActivityAdminBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminBinding.inflate(layoutInflater)
        setContentView(binding?.root)
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}