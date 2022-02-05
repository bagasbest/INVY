package com.project.invy.home.adm_produce

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.project.invy.databinding.ActivityAdminBinding
import com.project.invy.home.produce.ProduceAdapter
import com.project.invy.home.produce.ProduceViewModel

class AdminActivity : AppCompatActivity() {

    private var binding: ActivityAdminBinding? = null
    private var adminAdapter : AdminAdapter? = null
    private var produceAdapter: ProduceAdapter? = null

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        when (intent.getStringExtra(EXTRA_OPTION)) {
            "product_in" -> {
                binding?.rvIn?.visibility = View.VISIBLE
                initRecyclerViewIn()
                initViewModelIn()
                binding?.title?.text = "Bukti penerimaan barang masuk"
            }
            "product_out" -> {
                binding?.rvOut?.visibility = View.VISIBLE
                initRecyclerViewOut()
                initViewModelOut()
                binding?.title?.text = "Bukti penerimaan barang keluar"
            }
            else -> {
                binding?.rvProduct?.visibility = View.VISIBLE
                initRecyclerViewProduce()
                initViewModelProduce()
                binding?.title?.text = "Bukti BPM (Bon Penerimaan Material)"
            }
        }

        binding?.back?.setOnClickListener {
            onBackPressed()
        }
    }

    private fun initRecyclerViewProduce() {
        binding?.rvProduct?.layoutManager = LinearLayoutManager(this)
        produceAdapter = ProduceAdapter("produce")
        binding?.rvProduct?.adapter = produceAdapter
    }

    private fun initViewModelProduce() {
        val viewModel = ViewModelProvider(this)[ProduceViewModel::class.java]

        binding?.progressBar?.visibility = View.VISIBLE
        viewModel.setListProduct()
        viewModel.getProduceList().observe(this) { produce ->
            if (produce.size > 0) {
                produceAdapter!!.setData(produce)
                binding?.noData?.visibility = View.GONE
            } else {
                binding?.noData?.visibility = View.VISIBLE
            }
            binding!!.progressBar.visibility = View.GONE
        }
    }

    private fun initRecyclerViewOut() {
        binding?.rvOut?.layoutManager = LinearLayoutManager(this)
        adminAdapter = AdminAdapter()
        binding?.rvOut?.adapter = adminAdapter
    }

    private fun initViewModelOut() {
        val viewModel = ViewModelProvider(this)[AdminViewModel::class.java]

        binding?.progressBar?.visibility = View.VISIBLE
        viewModel.setListProductOut()
        viewModel.getProduceList().observe(this) { productOut ->
            if (productOut.size > 0) {
                adminAdapter!!.setData(productOut)
                binding?.noData?.visibility = View.GONE
            } else {
                binding?.noData?.visibility = View.VISIBLE
            }
            binding!!.progressBar.visibility = View.GONE
        }
    }

    private fun initRecyclerViewIn() {
        binding?.rvIn?.layoutManager = LinearLayoutManager(this)
        adminAdapter = AdminAdapter()
        binding?.rvIn?.adapter = adminAdapter
    }

    private fun initViewModelIn() {
        val viewModel = ViewModelProvider(this)[AdminViewModel::class.java]

        binding?.progressBar?.visibility = View.VISIBLE
        viewModel.setListProductIn()
        viewModel.getProduceList().observe(this) { productIn ->
            if (productIn.size > 0) {
                adminAdapter!!.setData(productIn)
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

    companion object {
        const val EXTRA_OPTION = "option"
    }
}