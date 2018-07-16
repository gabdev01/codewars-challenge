package com.tuigroup.codewars.data.local;

import android.support.test.runner.AndroidJUnit4;

import com.tuigroup.codewars.data.TestUtil;
import com.tuigroup.codewars.data.local.model.UserEntity;
import com.tuigroup.codewars.data.local.model.UserSearchHistory;
import com.tuigroup.codewars.data.local.model.UserSearchHistoryEntity;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Calendar;
import java.util.Date;

@RunWith(AndroidJUnit4.class)
public class UserSearchHistoryDaoTest extends DatabaseTest {

    @Test
    public void insertAndGetUser() {
        UserSearchHistoryEntity userSearchHistory = TestUtil.createUserSearchHistoryEntity(new Date(), "foo");
        UserEntity user = TestUtil.createUserEntity("foo", "name", 1);
        database.userSearchHistoryDao().insert(userSearchHistory);
        database.usersDao().insert(user);
        database.userSearchHistoryDao()
                .getLastUsersSearched(1)
                .test()
                .assertValue(userSearchHistoryResult -> {
                    UserSearchHistory result = userSearchHistoryResult.get(0);
                    return result.getSearchDate().equals(userSearchHistory.getSearchDate())
                            && result.getUser().getUsername().equals(userSearchHistory.getUserId());
                });
    }

    @Test
    public void insertAndGetUserByDateAdded() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2018, Calendar.JANUARY, 9);
        UserSearchHistoryEntity userSearchHistory = TestUtil.createUserSearchHistoryEntity(calendar.getTime(), "foo");
        UserEntity user = TestUtil.createUserEntity("foo", "name", 0);
        calendar.add(Calendar.MINUTE, 1);
        UserSearchHistoryEntity userSearchHistory1 = TestUtil.createUserSearchHistoryEntity(calendar.getTime(), "foo1");
        UserEntity user1 = TestUtil.createUserEntity("foo1", "name1", 1);
        calendar.add(Calendar.MINUTE, 1);
        UserSearchHistoryEntity userSearchHistory2 = TestUtil.createUserSearchHistoryEntity(calendar.getTime(), "foo2");
        UserEntity user2 = TestUtil.createUserEntity("foo2", "name2", 2);
        database.userSearchHistoryDao().insert(userSearchHistory, userSearchHistory1, userSearchHistory2);
        database.usersDao().insert(user, user1, user2);
        database.userSearchHistoryDao()
                .getLastUsersSearched(5)
                .test()
                .assertValue(userSearchHistoryResult -> {
                    UserSearchHistory lastUserResult = userSearchHistoryResult.get(0);
                    return userSearchHistoryResult.size() == 3
                            && lastUserResult.getUser().getUsername().equals(user2.getUsername());
                });
    }

    @Test
    public void insertAndGetUserByDateRank() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2018, Calendar.JANUARY, 9);
        UserSearchHistoryEntity userSearchHistory = TestUtil.createUserSearchHistoryEntity(calendar.getTime(), "foo");
        UserEntity user = TestUtil.createUserEntity("foo", "name", 0);
        calendar.add(Calendar.MINUTE, 1);
        UserSearchHistoryEntity userSearchHistory1 = TestUtil.createUserSearchHistoryEntity(calendar.getTime(), "foo1");
        UserEntity user1 = TestUtil.createUserEntity("foo1", "name1", 1);
        calendar.add(Calendar.MINUTE, 1);
        UserSearchHistoryEntity userSearchHistory2 = TestUtil.createUserSearchHistoryEntity(calendar.getTime(), "foo2");
        UserEntity user2 = TestUtil.createUserEntity("foo2", "name2", 2);
        calendar.add(Calendar.MINUTE, 1);
        database.userSearchHistoryDao().insert(userSearchHistory, userSearchHistory1, userSearchHistory2);
        database.usersDao().insert(user, user1, user2);
        database.userSearchHistoryDao()
                .getLastUsersSearchedByRank(5)
                .test()
                .assertValue(userSearchHistoryResult -> {
                    UserSearchHistory lastUserResult = userSearchHistoryResult.get(0);
                    return userSearchHistoryResult.size() == 3
                            && lastUserResult.getUser().getUsername().equals(user.getUsername());
                });
    }
}
