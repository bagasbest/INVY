package com.project.invy.home.adm_produce

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore

class AdminViewModel : ViewModel() {



    private val productList = MutableLiveData<ArrayList<AdminModel>>()
    private val listItems = ArrayList<AdminModel>()
    private val TAG = AdminViewModel::class.java.simpleName

    fun setListProductIn() {
        listItems.clear()

        try {
            FirebaseFirestore.getInstance().collection("product_in")
                .get()
                .addOnSuccessListener { documents ->
                    for (document in documents) {
                        val model = AdminModel()
                        model.name = document.data["name"].toString()
                        model.date = document.data["date"].toString()
                        model.image = document.data["image"].toString()
                        model.productVendor = document.data["productVendor"].toString()
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

    fun setListProductOut() {
        listItems.clear()

        try {
            FirebaseFirestore.getInstance().collection("product_out")
                .get()
                .addOnSuccessListener { documents ->
                    for (document in documents) {
                        val model = AdminModel()
                        model.name = document.data["name"].toString()
                        model.date = document.data["date"].toString()
                        model.image = document.data["image"].toString()
                        model.productVendor = document.data["productVendor"].toString()
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

    fun getProduceList() : LiveData<ArrayList<AdminModel>> {
        return productList
    }

}