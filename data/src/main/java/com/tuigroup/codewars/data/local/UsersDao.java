package com.tuigroup.codewars.data.local;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.tuigroup.codewars.data.local.model.UserEntity;

import io.reactivex.Flowable;

@Dao
public interface UsersDao {

    @Query("SELECT * FROM user WHERE username == :username")
    Flowable<UserEntity> getUserById(String username);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(UserEntity... user);

    @Update
    void update(UserEntity... user);

    @Delete
    void delete(UserEntity... user);
}
