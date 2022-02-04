package com.project.invy.home.produce.out_of_stock

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore

class OutOfStockViewModel : ViewModel() {

    private val productList = MutableLiveData<ArrayList<OutOfStockModel>>()
    private val listItems = ArrayList<OutOfStockModel>()
    private val TAG = OutOfStockViewModel::class.java.simpleName

    fun setListProduct() {
        listItems.clear()

        try {
            FirebaseFirestore.getInstance().collection("product_available_or_not")
                .get()
                .addOnSuccessListener { documents ->
                    for (document in documents) {
                        val model = OutOfStockModel()
                        model.name = document.data["name"].toString()
                        model.date = document.data["date"].toString()
                        model.vendor = document.data["vendor"].toString()
                        model.isAvailable = document.data["isAvailable"].toString()
                        model.total = document.data["total"] as Long
                        model.productId = document.data["productId"].toString()

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

    fun getProduceList() : LiveData<ArrayList<OutOfStockModel>> {
        return productList
    }

}