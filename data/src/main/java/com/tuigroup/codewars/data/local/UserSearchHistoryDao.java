package com.tuigroup.codewars.data.local;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.tuigroup.codewars.data.local.model.UserSearchHistory;
import com.tuigroup.codewars.data.local.model.UserSearchHistoryEntity;

import java.util.List;

import io.reactivex.Flowable;

@Dao
public interface UserSearchHistoryDao {

    @Query("SELECT user.*, user_search_history.search_date FROM user "
            + "INNER JOIN user_search_history ON user.username = user_search_history.user_id "
            + "ORDER BY user_search_history.search_date DESC LIMIT :limit")
    Flowable<List<UserSearchHistory>> getLastUsersSearched(int limit);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(UserSearchHistoryEntity... user);

    @Update
    void update(UserSearchHistoryEntity... user);

    @Delete
    void delete(UserSearchHistoryEntity... user);
}
