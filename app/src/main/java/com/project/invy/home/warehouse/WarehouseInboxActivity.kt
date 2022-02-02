package com.project.invy.home.warehouse

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.project.invy.databinding.ActivityWarehouseInboxBinding
import com.project.invy.home.produce.ProduceAdapter
import com.project.invy.home.produce.ProduceViewModel

class WarehouseInboxActivity : AppCompatActivity() {

    private var binding: ActivityWarehouseInboxBinding? = null
    private var adapter: ProduceAdapter? = null


    override fun onResume() {
        super.onResume()
        initRecyclerView()
        initViewModel()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWarehouseInboxBinding.inflate(layoutInflater)
        setContentView(binding?.root)


        binding?.back?.setOnClickListener {
            onBackPressed()
        }
    }

    private fun initRecyclerView() {
        binding?.rvProduct?.layoutManager = LinearLayoutManager(this)
        adapter = ProduceAdapter("warehouse")
        binding?.rvProduct?.adapter = adapter
    }

    private fun initViewModel() {
        val viewModel = ViewModelProvider(this)[ProduceViewModel::class.java]

        binding?.progressBar?.visibility = View.VISIBLE
        viewModel.setListProduct()
        viewModel.getProduceList().observe(this) { produce ->
            if (produce.size > 0) {
                adapter!!.setData(produce)
                binding?.noData?.visibility = View.GONE
            } else {
                binding?.noData?.visibility = View.VISIBLE
            }
            binding!!.progressBar.visibility = View.GONE
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}