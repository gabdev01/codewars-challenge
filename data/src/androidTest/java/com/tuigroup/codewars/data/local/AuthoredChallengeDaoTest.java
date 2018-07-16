package com.tuigroup.codewars.data.local;

import android.support.test.runner.AndroidJUnit4;

import com.tuigroup.codewars.data.local.model.AuthoredChallengeEntity;
import com.tuigroup.codewars.data.TestUtil;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

@RunWith(AndroidJUnit4.class)
public class AuthoredChallengeDaoTest extends DatabaseTest {

    @Test
    public void insertAndGetChallenge() {
        AuthoredChallengeEntity challenge = TestUtil.createAuthoredChallengeEntity("foo", "username", "category");
        database.authoredChallengeDao().insert(challenge);
        database.authoredChallengeDao()
                .getAuthoredChallenges("username")
                .test()
                .assertValue(authoredChallengeEntities -> {
                    // Our result is the first item
                    AuthoredChallengeEntity challengeResult = authoredChallengeEntities.get(0);
                    return challengeResult.getId().equals(challenge.getId())
                            && challengeResult.getCategory().equals(challenge.getCategory())
                            && challengeResult.getUserId().equals(challenge.getUserId());
                });
    }

    @Test
    public void insertAllAndGetAllChallenges() {
        AuthoredChallengeEntity challenge1 = TestUtil.createAuthoredChallengeEntity("foo1", "username1", "category1");
        AuthoredChallengeEntity challenge2 = TestUtil.createAuthoredChallengeEntity("foo2", "username2", "category2");
        List<AuthoredChallengeEntity> challenges = new ArrayList<>();
        challenges.add(challenge1);
        challenges.add(challenge2);
        database.authoredChallengeDao().insertAll(challenges);
        database.authoredChallengeDao()
                .getAuthoredChallenges("username2")
                .test()
                .assertValue(authoredChallengeEntities -> {
                    // Our result is the first item
                    AuthoredChallengeEntity challengeResult = authoredChallengeEntities.get(0);
                    return challengeResult.getId().equals(challenge2.getId())
                            && challengeResult.getCategory().equals(challenge2.getCategory())
                            && challengeResult.getUserId().equals(challenge2.getUserId());
                });
    }

    @Test
    public void updateAndGetChallenge() {
        AuthoredChallengeEntity challenge = TestUtil.createAuthoredChallengeEntity("foo", "username", "category");
        database.authoredChallengeDao().insert(challenge);
        AuthoredChallengeEntity updatedChallenge = TestUtil.createAuthoredChallengeEntity("foo", "username", "update_category");
        database.authoredChallengeDao().update(updatedChallenge);
        database.authoredChallengeDao().getAuthoredChallenges("username")
                .test()
                .assertValue(authoredChallengeEntities -> {
                    AuthoredChallengeEntity challengeResult = authoredChallengeEntities.get(0);
                    return challengeResult.getId().equals(challenge.getId())
                            && challengeResult.getUserId().equals(challenge.getUserId())
                            && challengeResult.getCategory().equals("update_category");
                });
        // Insert should replace if the model already exist
        AuthoredChallengeEntity updated2Challenge = TestUtil.createAuthoredChallengeEntity("foo", "username", "update_category_2");
        database.authoredChallengeDao().insert(updated2Challenge);
        database.authoredChallengeDao().getAuthoredChallenges("username")
                .test()
                .assertValue(authoredChallengeEntities -> {
                    AuthoredChallengeEntity challengeResult = authoredChallengeEntities.get(0);
                    return challengeResult.getId().equals(challenge.getId())
                            && challengeResult.getUserId().equals(challenge.getUserId())
                            && challengeResult.getCategory().equals("update_category_2");
                });
    }

    @Test
    public void deleteAndGetChallenge() {
        AuthoredChallengeEntity challenge = TestUtil.createAuthoredChallengeEntity("foo", "username", "category");
        database.authoredChallengeDao().insert(challenge);
        database.authoredChallengeDao().delete(challenge);
        database.authoredChallengeDao().getAuthoredChallenges("username")
                .test()
                .assertValue(authoredChallengeEntities -> authoredChallengeEntities.isEmpty());
    }
}
