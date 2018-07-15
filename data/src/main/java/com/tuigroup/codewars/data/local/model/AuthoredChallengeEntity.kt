package com.tuigroup.codewars.data.local.model

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "authored_challenge")
data class AuthoredChallengeEntity(
        @PrimaryKey
        @ColumnInfo(name = "id")
        val id: String,
        @ColumnInfo(name = "user_id")
        val userId: String,
        @ColumnInfo(name = "category")
        val category: String?,
        @ColumnInfo(name = "description")
        val description: String?,
        @ColumnInfo(name = "name")
        val name: String?,
        @ColumnInfo(name = "output")
        val output: String?,
        @ColumnInfo(name = "publishedStatus")
        val publishedStatus: String?,
        @ColumnInfo(name = "rank")
        val rank: Int,
        @ColumnInfo(name = "state")
        val state: String?,
        @ColumnInfo(name = "status")
        val status: String?,
        @ColumnInfo(name = "total_attempts")
        val totalAttempts: Int,
        @ColumnInfo(name = "total_completed")
        val totalCompleted: Int,
        @ColumnInfo(name = "total_up_votes")
        val totalUpVotes: Int,
        @ColumnInfo(name = "total_vote_score")
        val totalVoteScore: Int
)