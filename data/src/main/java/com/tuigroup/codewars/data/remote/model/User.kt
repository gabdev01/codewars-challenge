package com.tuigroup.codewars.data.remote.model

import com.google.gson.annotations.SerializedName

data class User(
        @SerializedName("username") val username: String,
        @SerializedName("name") val name: String?,
        @SerializedName("honor") val honor: Int,
        @SerializedName("clan") val clan: String?,
        @SerializedName("leaderboardPosition") val leaderboardPosition: Int,
        @SerializedName("skills") val skills: List<String>?,
        @SerializedName("ranks") val ranks: Ranks?
)