package codewars.tuigroup.com.codewars.util;

import com.tuigroup.codewars.data.local.model.AuthoredChallengeEntity;
import com.tuigroup.codewars.data.local.model.CodeChallengeEntity;
import com.tuigroup.codewars.data.local.model.CompletedChallengeEntity;
import com.tuigroup.codewars.data.local.model.UserEntity;
import com.tuigroup.codewars.data.local.model.UserSearchHistory;
import com.tuigroup.codewars.data.local.model.UserSearchHistoryEntity;

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

    public static UserSearchHistoryEntity createUserSearchHistoryEntity(Date date, String username) {
        UserSearchHistoryEntity userSearchHistoryEntity = new UserSearchHistoryEntity(
                date,
                username
        );
        return userSearchHistoryEntity;
    }

    public static UserSearchHistory createUserSearchHistoryEntity(Date date, UserEntity user) {
        UserSearchHistory userSearchHistoryEntity = new UserSearchHistory(
                date,
                user
        );
        return userSearchHistoryEntity;
    }

    public static List<UserSearchHistory> createUserSearchHistoryList() {
        List<UserSearchHistory> result = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        calendar.set(2018, Calendar.JANUARY, 9);
        UserEntity user = TestUtil.createUserEntity("foo", "name", 0);
        UserSearchHistory userSearchHistory = TestUtil.createUserSearchHistoryEntity(calendar.getTime(), user);
        calendar.add(Calendar.MINUTE, 1);
        UserEntity user1 = TestUtil.createUserEntity("foo1", "name1", 1);
        UserSearchHistory userSearchHistory1 = TestUtil.createUserSearchHistoryEntity(calendar.getTime(), user1);
        calendar.add(Calendar.MINUTE, 1);
        UserEntity user2 = TestUtil.createUserEntity("foo2", "name2", 2);
        UserSearchHistory userSearchHistory2 = TestUtil.createUserSearchHistoryEntity(calendar.getTime(), user2);
        result.add(userSearchHistory);
        result.add(userSearchHistory1);
        result.add(userSearchHistory2);
        return result;
    }

    public static List<AuthoredChallengeEntity> createAuthoredChallengeEntityList() {
        List<AuthoredChallengeEntity> result = new ArrayList<>();
        result.add(createAuthoredChallengeEntity("id1", "username1", "cat1"));
        result.add(createAuthoredChallengeEntity("id2", "username2", "cat2"));
        result.add(createAuthoredChallengeEntity("id3", "username3", "cat3"));
        return result;
    }

    public static List<CompletedChallengeEntity> createCompletedChallengeEntityList() {
        List<CompletedChallengeEntity> result = new ArrayList<CompletedChallengeEntity>();
        result.add(createCompletedChallengeEntity("id1", "username1", "name1"));
        result.add(createCompletedChallengeEntity("id2", "username2", "name2"));
        result.add(createCompletedChallengeEntity("id3", "username3", "name3"));
        return result;
    }
}
