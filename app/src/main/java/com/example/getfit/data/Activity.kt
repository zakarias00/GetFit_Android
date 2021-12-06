package com.example.getfit.data

import com.google.gson.annotations.SerializedName
import java.util.*

data class Activity(
    @SerializedName("_id") val id: Int? = null,
    @SerializedName("date") val date: Long? = null, //sql vagy java????
    @SerializedName("distance") val distance: Double? = null,
    @SerializedName("kcal") val kcal: Double? = null,
    @SerializedName("sportId") val sportId: Int? = null,
    @SerializedName("time") val time: Double? = null,
    @SerializedName("userId") val userId: Int? = null,
){
    constructor( date: Long?, distance: Double?, sportId: Int?, time: Double?, userId: Int?) :
            this(null, date, distance, null, sportId, time, userId)

}

