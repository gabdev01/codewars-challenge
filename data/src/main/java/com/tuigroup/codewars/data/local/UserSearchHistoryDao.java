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

    @Query("SELECT user_result.*, user_result.search_date FROM "
            + "(SELECT user.*, user_search_history.search_date  FROM "
            + "user INNER JOIN user_search_history ON user.username = user_search_history.user_id "
            + "ORDER BY user_search_history.search_date DESC LIMIT :limit) "
            + "user_result ORDER BY user_result.leaderboard_position ASC")
    Flowable<List<UserSearchHistory>> getLastUsersSearchedByRank(int limit);

    /*
    // Unfortunately, raw query doesn't allow to modify ORDER BY and the order ASC and DESC.
    @RawQuery(observedEntities = UserSearchHistoryEntity.class)
    Flowable<List<UserSearchHistory>> getLastUsersSearchedViaQuery(SupportSQLiteQuery query);
    */

    /*
    It's possible to use this format to modify the ORDER BY with a parameter but no solution
    found to modify dynamically ASC and DESC
    CASE :order
    WHEN 'sort_by_date' THEN user_search_history.search_date
    WHEN 'sort_by_rank' THEN user.leaderboard_position
    END
     */

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(UserSearchHistoryEntity... user);

    @Update
    void update(UserSearchHistoryEntity... user);

    @Delete
    void delete(UserSearchHistoryEntity... user);
}
