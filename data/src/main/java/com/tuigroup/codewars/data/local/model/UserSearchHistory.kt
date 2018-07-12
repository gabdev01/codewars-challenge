package com.tuigroup.codewars.data.local.model

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Embedded
import java.util.*

data class UserSearchHistory(
        @ColumnInfo(name = "search_date")
        val searchDate: Date,
        @Embedded
        val user: UserEntity
)

