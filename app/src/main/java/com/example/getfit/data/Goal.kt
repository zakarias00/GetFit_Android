package com.example.getfit.data

import com.google.gson.annotations.SerializedName
import java.sql.Date

data class Goal(
    @SerializedName("_id") val id: Int? = null,
    @SerializedName("amount") var amount: Int? = null,
    @SerializedName("currentAmount") val currentAmount: Int? = null,
    @SerializedName("dateStart") val dateStart: Long? = null,
    @SerializedName("status") val status: Int? = null,
    @SerializedName("userId") val userId: Int? = null
) {
    constructor( amount: Int?, dateStart: Long?, userId: Int?) :
            this(null, amount, 0, dateStart, 0, userId)

}
