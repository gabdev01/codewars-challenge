package com.tuigroup.codewars.data.local.model

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "user")
data class UserEntity(
        @PrimaryKey
        @ColumnInfo(name = "username")
        val username: String,
        @ColumnInfo(name = "name")
        val name: String? = null,
        @ColumnInfo(name = "honor")
        val honor: Int,
        @ColumnInfo(name = "clan")
        val clan: String? = null,
        @ColumnInfo(name = "leaderboard_position")
        val leaderboardPosition: Int
)
