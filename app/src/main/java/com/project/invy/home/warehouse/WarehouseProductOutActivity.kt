package com.project.invy.home.warehouse

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.project.invy.databinding.ActivityWarehouseProductOutBinding

class WarehouseProductOutActivity : AppCompatActivity() {

    private var binding: ActivityWarehouseProductOutBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWarehouseProductOutBinding.inflate(layoutInflater)
        setContentView(binding?.root)
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}