package com.project.invy.home.warehouse

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.project.invy.databinding.ActivityWarehouseProductInBinding

class WarehouseProductInActivity : AppCompatActivity() {

    private var binding: ActivityWarehouseProductInBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWarehouseProductInBinding.inflate(layoutInflater)
        setContentView(binding?.root)
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}