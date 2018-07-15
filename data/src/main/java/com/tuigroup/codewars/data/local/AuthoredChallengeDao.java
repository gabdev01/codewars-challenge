package com.tuigroup.codewars.data.local;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.tuigroup.codewars.data.local.model.AuthoredChallengeEntity;

import java.util.List;

import io.reactivex.Flowable;

@Dao
public interface AuthoredChallengeDao {

    @Query("SELECT * FROM authored_challenge WHERE user_id == :username")
    Flowable<List<AuthoredChallengeEntity>> getAuthoredChallenge(String username);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(AuthoredChallengeEntity... challenge);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<AuthoredChallengeEntity> challenges);

    @Update
    void update(AuthoredChallengeEntity... challenge);

    @Delete
    void delete(AuthoredChallengeEntity... challenge);
}
