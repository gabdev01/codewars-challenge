package com.tuigroup.codewars.data.local.model

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "completed_challenge")
data class CompletedChallengeEntity(
        @PrimaryKey
        @ColumnInfo(name = "id")
        val id: String,
        @ColumnInfo(name = "user_id")
        val userId: String,
        @ColumnInfo(name = "name")
        val name: String?,
        @ColumnInfo(name = "slug")
        val slug: String?,
        @ColumnInfo(name = "completed_at")
        val completedAt: String?,
        @ColumnInfo(name = "completed_languages")
        val completedLanguages: List<String>?,
        @ColumnInfo(name = "page_index_in_response")
        var pageIndexInResponse: Int = -1
) : Parcelable