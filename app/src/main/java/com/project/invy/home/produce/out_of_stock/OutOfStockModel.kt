package com.project.invy.home.produce.out_of_stock

data class OutOfStockModel(
    var productId: String? = null,
    var name: String? = null,
    var vendor: String? = null,
    var isAvailable: String? = null,
    var date: String? = null,
    var total: Long? = 0L,
)