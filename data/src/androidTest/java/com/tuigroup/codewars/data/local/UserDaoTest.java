package com.tuigroup.codewars.data.local;

import android.support.test.runner.AndroidJUnit4;

import com.tuigroup.codewars.data.local.model.UserEntity;
import com.tuigroup.codewars.data.TestUtil;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class UserDaoTest extends DatabaseTest {

    @Test
    public void insertAndGetUser() {
        UserEntity user = TestUtil.createUserEntity("foo", "name", 1);
        database.usersDao().insert(user);
        database.usersDao()
                .getUserById("foo")
                .test()
                .assertValue(userResult -> userResult.getUsername().equals(user.getUsername())
                        && userResult.getName().equals(user.getName())
                        && userResult.getLeaderboardPosition() == user.getLeaderboardPosition());
    }

    @Test
    public void updateAndGetUser() {
        UserEntity user = TestUtil.createUserEntity("foo", "name", 0);
        database.usersDao().insert(user);
        UserEntity updatedUser = TestUtil.createUserEntity("foo", "name1", 1);
        database.usersDao().update(updatedUser);
        database.usersDao().getUserById("foo")
                .test()
                .assertValue(userResult -> userResult.getUsername().equals(updatedUser.getUsername())
                        && userResult.getName().equals(updatedUser.getName())
                        && userResult.getLeaderboardPosition() == updatedUser.getLeaderboardPosition());
        // Insert should replace if the model already exist
        UserEntity updatedUser2 = TestUtil.createUserEntity("foo", "name2", 2);
        database.usersDao().insert(updatedUser2);
        database.usersDao().getUserById("foo")
                .test()
                .assertValue(userResult -> userResult.getUsername().equals(updatedUser2.getUsername())
                        && userResult.getName().equals(updatedUser2.getName())
                        && userResult.getLeaderboardPosition() == updatedUser2.getLeaderboardPosition());
    }

    @Test
    public void deleteAndGetUser() {
        UserEntity user = TestUtil.createUserEntity("foo", "name", 0);
        database.usersDao().insert(user);
        database.usersDao().delete(user);
        database.usersDao().getUserById("foo")
                .test()
                .assertNoValues();
    }
}
