package com.project.invy.home.warehouse

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.project.invy.databinding.ActivityWarehouseInboxBinding

class WarehouseInboxActivity : AppCompatActivity() {

    private var binding: ActivityWarehouseInboxBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWarehouseInboxBinding.inflate(layoutInflater)
        setContentView(binding?.root)
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}