package com.tuigroup.codewars.data.remote.model

import com.google.gson.annotations.SerializedName

data class Rank(
        @SerializedName("rank") val rank: Int,
        @SerializedName("name") val name: String,
        @SerializedName("color") val color: String,
        @SerializedName("score") val score: Int
)