package com.tuigroup.codewars.data.remote.model

import com.google.gson.annotations.SerializedName

data class CodeChallenges(
        @SerializedName("totalAuthored") val totalAuthored: Int,
        @SerializedName("totalCompleted") val totalCompleted: Int
)