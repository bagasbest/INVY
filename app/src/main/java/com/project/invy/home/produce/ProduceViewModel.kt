package com.project.invy.home.produce

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore

class ProduceViewModel : ViewModel() {


    private val productList = MutableLiveData<ArrayList<ProduceModel>>()
    private val listItems = ArrayList<ProduceModel>()
    private val TAG = ProduceViewModel::class.java.simpleName

    fun setListProduct() {
        listItems.clear()


        try {
            FirebaseFirestore.getInstance().collection("product")
                .get()
                .addOnSuccessListener { documents ->
                    for (document in documents) {
                        val model = ProduceModel()
                        model.code = document.data["code"].toString()
                        model.name = document.data["name"].toString()
                        model.date = document.data["date"].toString()
                        model.invCode = document.data["invCode"].toString()
                        model.keterangan = document.data["keterangan"].toString()
                        model.productId = document.data["productId"].toString()
                        model.satuan = document.data["satuan"].toString()
                        model.status = document.data["status"].toString()
                        model.total = document.data["total"] as Long

                        listItems.add(model)
                    }
                    productList.postValue(listItems)
                }
                .addOnFailureListener { exception ->
                    Log.w(TAG, "Error getting documents: ", exception)
                }
        } catch (error: Exception) {
            error.printStackTrace()
        }
    }

    fun getProduceList() : LiveData<ArrayList<ProduceModel>> {
        return productList
    }


}