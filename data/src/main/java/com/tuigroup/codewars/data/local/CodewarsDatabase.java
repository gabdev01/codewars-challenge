package com.tuigroup.codewars.data.local;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.tuigroup.codewars.data.local.model.UserEntity;

@Database(entities = {UserEntity.class}, version = 1)
public abstract class CodewarsDatabase extends RoomDatabase {

    public abstract UsersDao usersDao();
}