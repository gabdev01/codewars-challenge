package com.tuigroup.codewars.data.local;

import android.arch.persistence.room.Room;
import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class CodewarsDatabaseModule {

    @Singleton
    @Provides
    CodewarsDatabase provideCodewarsDatabase(Context context) {
        return Room.databaseBuilder(context, CodewarsDatabase.class, "database-codewars")
                .build();
    }

    @Singleton
    @Provides
    UserDao provideUserDao(CodewarsDatabase codewarsDatabase) {
        return codewarsDatabase.usersDao();
    }

    @Singleton
    @Provides
    UserSearchHistoryDao provideUserSearchHistoryDao(CodewarsDatabase codewarsDatabase) {
        return codewarsDatabase.searchUserHistoryDao();
    }
}