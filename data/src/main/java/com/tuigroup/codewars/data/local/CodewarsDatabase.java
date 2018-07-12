package com.tuigroup.codewars.data.local;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;

import com.tuigroup.codewars.data.local.converter.DateTypeConverter;
import com.tuigroup.codewars.data.local.model.UserSearchHistoryEntity;
import com.tuigroup.codewars.data.local.model.UserEntity;

@Database(entities = {UserEntity.class, UserSearchHistoryEntity.class}, version = 1)
@TypeConverters({DateTypeConverter.class})
public abstract class CodewarsDatabase extends RoomDatabase {

    public abstract UserDao usersDao();

    public abstract UserSearchHistoryDao searchUserHistoryDao();
}