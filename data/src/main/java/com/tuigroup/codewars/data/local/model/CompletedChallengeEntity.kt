package com.tuigroup.codewars.data.local.model

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "completed_challenge")
data class CompletedChallengeEntity(
        @PrimaryKey
        @ColumnInfo(name = "id")
        val id: String,
        @ColumnInfo(name = "user_id")
        val userId: String,
        @ColumnInfo(name = "name")
        val name: String,
        @ColumnInfo(name = "slug")
        val slug: String,
        @ColumnInfo(name = "completed_at")
        val completedAt: String,
        @ColumnInfo(name = "completed_languages")
        val completedLanguages: List<String>,
        @ColumnInfo(name = "index_in_response")
        var indexInResponse: Int = -1
)