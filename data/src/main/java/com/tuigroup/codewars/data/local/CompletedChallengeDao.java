package com.tuigroup.codewars.data.local;

import android.arch.paging.DataSource;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.tuigroup.codewars.data.local.model.CompletedChallengeEntity;

import java.util.List;

import io.reactivex.Flowable;

@Dao
public interface CompletedChallengeDao {

    @Query("SELECT * FROM completed_challenge WHERE user_id == :username")
    Flowable<List<CompletedChallengeEntity>> getCompletedChallengesByUser(String username);

    @Query("SELECT * FROM completed_challenge WHERE user_id == :username")
    DataSource.Factory<Integer, CompletedChallengeEntity> getCompletedChallengesDataSourceByUser(String username);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(CompletedChallengeEntity... challenge);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<CompletedChallengeEntity> challenges);

    @Update
    void update(CompletedChallengeEntity... challenge);

    @Delete
    void delete(CompletedChallengeEntity... challenge);
}
