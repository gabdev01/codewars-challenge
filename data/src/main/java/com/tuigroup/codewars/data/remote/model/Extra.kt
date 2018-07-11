package com.tuigroup.codewars.data.remote.model

import com.google.gson.annotations.SerializedName

data class Extra(
    @SerializedName("status") val status: String,
    @SerializedName("beta") val beta: Boolean,
    @SerializedName("published") val published: Boolean
)