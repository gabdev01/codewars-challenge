package com.tuigroup.codewars.data.local;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.tuigroup.codewars.data.local.model.CodeChallengeEntity;

import java.util.List;

import io.reactivex.Flowable;

@Dao
public interface CodeChallengeDao {

    // Workaround: if there are no rows in the table to be returned by the query,
    // then the Flowable will emit an empty list. Otherwise the Flowable don't emit anything.
    @Query("SELECT * FROM code_challenge WHERE id == :id")
    Flowable<List<CodeChallengeEntity>> getChallengeById(String id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(CodeChallengeEntity... challenge);

    @Update
    void update(CodeChallengeEntity... challenge);

    @Delete
    void delete(CodeChallengeEntity... challenge);
}
