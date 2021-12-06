package com.example.getfit.data

import com.google.gson.annotations.SerializedName
import java.sql.Date

data class Sport(
    @SerializedName("_id") val id: Int? = null,
    @SerializedName("kcal") val kcal: Double? = null,
    @SerializedName("type") val type: String? = null,
) {

    constructor( kcal: Double?, type:String?) :
            this(null, kcal, type)

}
