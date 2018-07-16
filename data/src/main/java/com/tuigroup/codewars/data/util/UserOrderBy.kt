package com.tuigroup.codewars.data.util

enum class UserOrderBy(val value: Int) {
    DATE_ADDED(0),
    HIGHEST_RANK(1);

    companion object {
        private val map = UserOrderBy.values().associateBy(UserOrderBy::value)
        @JvmStatic
        fun from(type: Int) = map[type]
    }
}
