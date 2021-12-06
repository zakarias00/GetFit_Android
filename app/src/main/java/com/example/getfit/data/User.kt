package com.example.getfit.data

import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.sql.Date

data class User(
    @SerializedName("_id") val id: Int? = null,
    @SerializedName("name") val name: String? = null,
    @SerializedName("username") val username: String? = null,
    @SerializedName("email") val email: String? = null,
    @SerializedName("password") val password: String? = null,
    @SerializedName("birthdate") val birthdate: Long? = null,
    @SerializedName("height") var height: Int? = null,
    @SerializedName("weight") var weight: Int? = null,
    @SerializedName("points") val points: Int? = null,
    @SerializedName("level") val level: Int? = null,
    @SerializedName("gender") val gender: Boolean? = null
) : Serializable
{
    constructor(name:String?, username:String?, email:String?, password:String?, birthdate: Long?, height: Int?, weight: Int?, gender: Boolean?) :
            this(null, name, username, email, password, birthdate, height, weight, null, null, gender)

    constructor(name:String?, username:String?, email:String?, password:String?, birthdate: Long?, height: Int?, weight: Int?, points: Int?, level: Int?, gender: Boolean?) :
        this(null, name, username, email, password, birthdate, height, weight, points, level, gender)

}


