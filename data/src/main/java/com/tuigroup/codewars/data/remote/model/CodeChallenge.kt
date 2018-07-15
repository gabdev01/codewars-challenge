package com.tuigroup.codewars.data.remote.model

import com.google.gson.annotations.SerializedName

data class CodeChallenge(
        @SerializedName("id") val id: String,
        @SerializedName("name") val name: String?,
        @SerializedName("slug") val slug: String?,
        @SerializedName("category") val category: String?,
        @SerializedName("languages") val languages: List<String>?,
        @SerializedName("url") val url: String?,
        @SerializedName("description") val description: String?,
        @SerializedName("totalAttempts") val totalAttempts: Int,
        @SerializedName("totalCompleted") val totalCompleted: Int,
        @SerializedName("totalStars") val totalStars: Int,
        @SerializedName("tags") val tags: List<String>?
)