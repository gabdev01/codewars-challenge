package com.tuigroup.codewars.data.local.model

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "code_challenge")
data class CodeChallengeEntity(
        @PrimaryKey
        @ColumnInfo(name = "id")
        val id: String,
        @ColumnInfo(name = "name")
        val name: String?,
        @ColumnInfo(name = "slug")
        val slug: String?,
        @ColumnInfo(name = "category")
        val category: String?,
        @ColumnInfo(name = "languages")
        val languages: List<String>?,
        @ColumnInfo(name = "url")
        val url: String?,
        @ColumnInfo(name = "description")
        val description: String?,
        @ColumnInfo(name = "total_attempts")
        val totalAttempts: Int,
        @ColumnInfo(name = "total_completed")
        val totalCompleted: Int,
        @ColumnInfo(name = "total_stars")
        val totalStars: Int,
        @ColumnInfo(name = "tags")
        val tags: List<String>?
)