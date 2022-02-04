package com.project.invy.home.produce

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.project.invy.databinding.ActivityProduceInOutStockBinding
import com.project.invy.home.produce.out_of_stock.OutOfStockAdapter
import com.project.invy.home.produce.out_of_stock.OutOfStockViewModel

class ProduceInOutStockActivity : AppCompatActivity() {

    private var binding: ActivityProduceInOutStockBinding? = null
    private var adapter: OutOfStockAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProduceInOutStockBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        initRecyclerView()
        initViewModel()

        binding?.back?.setOnClickListener {
            onBackPressed()
        }

    }

    private fun initRecyclerView() {
        binding?.rvStock?.layoutManager = LinearLayoutManager(this)
        adapter = OutOfStockAdapter()
        binding?.rvStock?.adapter = adapter
    }

    private fun initViewModel() {
        val viewModel = ViewModelProvider(this)[OutOfStockViewModel::class.java]

        binding?.progressBar?.visibility = View.VISIBLE
        viewModel.setListProduct()
        viewModel.getProduceList().observe(this) { product ->
            if (product.size > 0) {
                adapter!!.setData(product)
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