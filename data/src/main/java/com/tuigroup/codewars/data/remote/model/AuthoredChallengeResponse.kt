package com.tuigroup.codewars.data.remote.model

import com.google.gson.annotations.SerializedName

data class AuthoredChallengeResponse(
    @SerializedName("data") val data: AuthoredChallenge
)