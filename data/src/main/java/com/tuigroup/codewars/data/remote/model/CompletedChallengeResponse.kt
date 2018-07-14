package com.tuigroup.codewars.data.remote.model

import com.google.gson.annotations.SerializedName

data class CompletedChallengeResponse(
        @SerializedName("totalPages") val totalPages: Int,
        @SerializedName("totalItems") val totalItems: Int,
        @SerializedName("data") val data: List<CompletedChallenge>
)