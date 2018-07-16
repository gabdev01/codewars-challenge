package com.tuigroup.codewars.data.local.model

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Embedded
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class UserSearchHistory(
        @ColumnInfo(name = "search_date")
        val searchDate: Date,
        @Embedded
        val user: UserEntity
) : Parcelable
