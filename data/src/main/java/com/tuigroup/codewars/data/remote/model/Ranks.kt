package com.tuigroup.codewars.data.remote.model

import com.google.gson.annotations.SerializedName

data class Ranks(
        @SerializedName("overall") val overall: Rank,
        @SerializedName("languages") val languages: Map<String, Rank>
)