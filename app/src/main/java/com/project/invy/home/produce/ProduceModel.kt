package com.project.invy.home.produce

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ProduceModel(
    var productId: String? = null,
    var name: String? = null,
    var code: String? = null,
    var invCode: String? = null,
    var satuan: String? = null,
    var total: Long? = 0L,
    var keterangan: String? = null,
    var date: String? = null,
    var status: String? = null,
) : Parcelable