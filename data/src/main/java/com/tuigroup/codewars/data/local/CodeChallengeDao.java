package com.tuigroup.codewars.data.local;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.tuigroup.codewars.data.local.model.CodeChallengeEntity;

import io.reactivex.Flowable;

@Dao
public interface CodeChallengeDao {

    @Query("SELECT * FROM code_challenge WHERE id == :id")
    Flowable<CodeChallengeEntity> getChallengeById(String id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(CodeChallengeEntity... user);

    @Update
    void update(CodeChallengeEntity... user);

    @Delete
    void delete(CodeChallengeEntity... user);
}
