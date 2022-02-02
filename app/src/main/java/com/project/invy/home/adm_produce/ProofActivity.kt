package com.project.invy.home.adm_produce

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.project.invy.databinding.ActivityProofBinding

class ProofActivity : AppCompatActivity() {

    private var binding: ActivityProofBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProofBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        val image = intent.getStringExtra(EXTRA_IMAGE)
        Glide.with(this)
            .load(image)
            .into(binding!!.image)

        binding?.back?.setOnClickListener {
            onBackPressed()
        }


    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    companion object {
        const val EXTRA_IMAGE = "image"
    }
}