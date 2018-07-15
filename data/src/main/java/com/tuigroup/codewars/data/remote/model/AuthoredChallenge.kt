package com.tuigroup.codewars.data.remote.model

import com.google.gson.annotations.SerializedName

data class AuthoredChallenge(
        @SerializedName("id") val id: String,
        @SerializedName("category") val category: String?,
        @SerializedName("description") val description: String?,
        @SerializedName("name") val name: String?,
        @SerializedName("output") val output: String?,
        @SerializedName("published_status") val publishedStatus: String?,
        @SerializedName("rank") val rank: Int,
        @SerializedName("state") val state: String?,
        @SerializedName("status") val status: String?,
        @SerializedName("total_attempts") val totalAttempts: Int,
        @SerializedName("total_completed") val totalCompleted: Int,
        @SerializedName("total_up_votes") val totalUpVotes: Int,
        @SerializedName("total_vote_score") val totalVoteScore: Int
)