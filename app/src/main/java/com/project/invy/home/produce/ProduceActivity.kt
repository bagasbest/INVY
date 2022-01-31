package com.project.invy.home.produce

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.project.invy.databinding.ActivityProduceBinding

class ProduceActivity : AppCompatActivity() {

    private var binding: ActivityProduceBinding? = null
    private var adapter: ProduceAdapter? = null


    override fun onResume() {
        super.onResume()
        initRecyclerView()
        initViewModel()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProduceBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        binding?.back?.setOnClickListener {
            onBackPressed()
        }

        binding?.addProduct?.setOnClickListener {
            startActivity(Intent(this, ProduceAddActivity::class.java))
        }
    }

    private fun initRecyclerView() {
        binding?.rvProduct?.layoutManager = LinearLayoutManager(this)
        adapter = ProduceAdapter("product")
        binding?.rvProduct?.adapter = adapter
    }

    private fun initViewModel() {
        val viewModel = ViewModelProvider(this)[ProduceViewModel::class.java]

        binding?.progressBar?.visibility = View.VISIBLE
        viewModel.setListProduct()
        viewModel.getProduceList().observe(this) { reward ->
            if (reward.size > 0) {
                adapter!!.setData(reward)
                binding?.noData?.visibility = View.GONE
            } else {
                binding?.noData?.visibility = View.VISIBLE
                binding!!.progressBar.visibility = View.GONE
            }
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}