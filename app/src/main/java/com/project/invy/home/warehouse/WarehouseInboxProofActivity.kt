package com.project.invy.home.warehouse

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.zxing.BarcodeFormat
import com.google.zxing.WriterException
import com.google.zxing.qrcode.QRCodeWriter
import com.project.invy.databinding.ActivityWarehouseInboxProofBinding
import com.project.invy.home.produce.ProduceModel

class WarehouseInboxProofActivity : AppCompatActivity() {

    private var binding: ActivityWarehouseInboxProofBinding? = null
    private var model : ProduceModel? = null

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWarehouseInboxProofBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        model = intent.getParcelableExtra(EXTRA_MODEL)

        binding?.date?.text = "Tanggal : ${model?.date}"
        binding?.code?.text = "No : ${model?.code}"

        binding?.name?.text = model?.name
        binding?.invCode?.text = model?.invCode
        binding?.total?.text = model?.total.toString()
        binding?.satuan?.text = model?.satuan.toString()
        binding?.keterangan?.text = model?.keterangan

        val writer = QRCodeWriter()
        try {
            val qrLeft = model?.productId + "stoker"
            val qrRight = model?.productId + "Kabag prod BSC"
            val bitMatrixQrLeft = writer.encode(qrLeft, BarcodeFormat.QR_CODE, 60, 60)
            val bitMatrixQrRight = writer.encode(qrRight, BarcodeFormat.QR_CODE, 60, 60)

            val widthLeft = bitMatrixQrLeft.width
            val heightLeft = bitMatrixQrLeft.height
            val bmpLeft = Bitmap.createBitmap(widthLeft, heightLeft, Bitmap.Config.RGB_565)
            for(x in 0 until widthLeft) {
                for (y in 0 until heightLeft) {
                    bmpLeft.setPixel(x, y, if (bitMatrixQrLeft[x,y]) Color.BLACK else Color.WHITE)
                }
            }

            val widthRight = bitMatrixQrRight.width
            val heightRight = bitMatrixQrRight.height
            val bmpRight = Bitmap.createBitmap(widthRight, heightRight, Bitmap.Config.RGB_565)
            for(x in 0 until widthRight) {
                for (y in 0 until heightRight) {
                    bmpRight.setPixel(x, y, if (bitMatrixQrRight[x,y]) Color.BLACK else Color.WHITE)
                }
            }
            binding?.qrLeft?.setImageBitmap(bmpLeft)
            binding?.qrRight?.setImageBitmap(bmpRight)


        } catch (e: WriterException) {
            e.printStackTrace()
        }

        binding?.back?.setOnClickListener {
            onBackPressed()
        }


    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    companion object {
        const val EXTRA_MODEL = "model"
    }
}