package com.tuigroup.codewars.data;

import com.tuigroup.codewars.data.local.model.AuthoredChallengeEntity;
import com.tuigroup.codewars.data.local.model.CodeChallengeEntity;
import com.tuigroup.codewars.data.local.model.CompletedChallengeEntity;
import com.tuigroup.codewars.data.local.model.UserEntity;
import com.tuigroup.codewars.data.local.model.UserSearchHistory;
import com.tuigroup.codewars.data.local.model.UserSearchHistoryEntity;
import com.tuigroup.codewars.data.remote.model.AuthoredChallenge;
import com.tuigroup.codewars.data.remote.model.CodeChallenge;
import com.tuigroup.codewars.data.remote.model.CompletedChallenge;
import com.tuigroup.codewars.data.remote.model.User;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class TestUtil {

    public static CodeChallengeEntity createCodeChallengeEntity(String challengeId, String category) {
        CodeChallengeEntity challenge = new CodeChallengeEntity(
                challengeId,
                null,
                null,
                category,
                null,
                null,
                null,
                0,
                0,
                0,
                null
        );
        return challenge;
    }

    public static CodeChallenge createCodeChallenge(String challengeId, String category) {
        CodeChallenge challenge = new CodeChallenge(
                challengeId,
                null,
                null,
                category,
                null,
                null,
                null,
                0,
                0,
                0,
                null
        );
        return challenge;
    }


    public static AuthoredChallenge createAuthoredChallenge(String challengeId, String category) {
        AuthoredChallenge challenge = new AuthoredChallenge(
                challengeId,
                category,
                null,
                null,
                null,
                null,
                0,
                null,
                null,
                0,
                0,
                0,
                0
        );
        return challenge;
    }

    public static AuthoredChallengeEntity createAuthoredChallengeEntity(String challengeId, String username, String category) {
        AuthoredChallengeEntity challenge = new AuthoredChallengeEntity(
                challengeId,
                username,
                category,
                null,
                null,
                null,
                null,
                0,
                null,
                null,
                0,
                0,
                0,
                0
        );
        return challenge;
    }

    public static CompletedChallenge createCompletedChallenge(String challengeId, String username, String name) {
        CompletedChallenge challenge = new CompletedChallenge(
                challengeId,
                name,
                null,
                null,
                null
        );
        return challenge;
    }

    public static CompletedChallengeEntity createCompletedChallengeEntity(String challengeId, String username, String name) {
        CompletedChallengeEntity challenge = new CompletedChallengeEntity(
                challengeId,
                username,
                name,
                null,
                null,
                null,
                0
        );
        return challenge;
    }

    public static UserEntity createUserEntity(String username, String name, int rank) {
        UserEntity userEntity = new UserEntity(
                username,
                name,
                0,
                null,
                rank
        );
        return userEntity;
    }

    public static User createUser(String username, String name, int rank) {
        User userEntity = new User(
                username,
                name,
                0,
                "",
                rank,
                new ArrayList<>(),
                null
        );
        return userEntity;
    }

    public static UserSearchHistoryEntity createUserSearchHistoryEntity(Date date, String username) {
        UserSearchHistoryEntity userSearchHistoryEntity = new UserSearchHistoryEntity(
                date,
                username
        );
        return userSearchHistoryEntity;
    }

    public static UserSearchHistory createUserSearchHistory(Date date, UserEntity userEntity) {
        UserSearchHistory userSearchHistory = new UserSearchHistory(
                date,
                userEntity
        );
        return userSearchHistory;
    }

    public static List<UserSearchHistory> createUserSearchHistoryList() {
        List<UserSearchHistory> result = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        calendar.set(2018, Calendar.JANUARY, 9);
        UserEntity user = TestUtil.createUserEntity("foo", "name", 0);
        UserSearchHistory userSearchHistory = TestUtil.createUserSearchHistory(calendar.getTime(), user);
        calendar.add(Calendar.MINUTE, 1);
        UserEntity user1 = TestUtil.createUserEntity("foo1", "name1", 1);
        UserSearchHistory userSearchHistory1 = TestUtil.createUserSearchHistory(calendar.getTime(), user1);
        calendar.add(Calendar.MINUTE, 1);
        UserEntity user2 = TestUtil.createUserEntity("foo2", "name2", 2);
        UserSearchHistory userSearchHistory2 = TestUtil.createUserSearchHistory(calendar.getTime(), user2);
        result.add(userSearchHistory);
        result.add(userSearchHistory1);
        result.add(userSearchHistory2);
        return result;
    }
}
