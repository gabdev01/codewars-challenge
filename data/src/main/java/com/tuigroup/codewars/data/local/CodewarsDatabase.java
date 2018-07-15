package com.tuigroup.codewars.data.local;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;

import com.tuigroup.codewars.data.local.converter.DateTypeConverter;
import com.tuigroup.codewars.data.local.converter.ListStringTypeConverter;
import com.tuigroup.codewars.data.local.model.AuthoredChallengeEntity;
import com.tuigroup.codewars.data.local.model.CodeChallengeEntity;
import com.tuigroup.codewars.data.local.model.CompletedChallengeEntity;
import com.tuigroup.codewars.data.local.model.UserEntity;
import com.tuigroup.codewars.data.local.model.UserSearchHistoryEntity;

@Database(entities = {UserEntity.class,
        UserSearchHistoryEntity.class,
        CompletedChallengeEntity.class,
        AuthoredChallengeEntity.class,
        CodeChallengeEntity.class}, version = 1)
@TypeConverters({DateTypeConverter.class, ListStringTypeConverter.class})
public abstract class CodewarsDatabase extends RoomDatabase {

    public abstract UserDao usersDao();

    public abstract UserSearchHistoryDao userSearchHistoryDao();

    public abstract CompletedChallengeDao completedChallengeDao();

    public abstract AuthoredChallengeDao authoredChallengeDao();

    public abstract CodeChallengeDao codeChallengeDao();
}