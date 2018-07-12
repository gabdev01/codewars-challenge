package com.tuigroup.codewars.data.local.model

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import java.util.*

@Entity(tableName = "user_search_history")
data class UserSearchHistoryEntity(
        @PrimaryKey
        @ColumnInfo(name = "search_date")
        val searchDate: Date,
        @ColumnInfo(name = "user_id")
        val userId: String
)

