package com.tuigroup.codewars.data.local;

import android.support.test.runner.AndroidJUnit4;

import com.tuigroup.codewars.data.local.model.CompletedChallengeEntity;
import com.tuigroup.codewars.data.TestUtil;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

@RunWith(AndroidJUnit4.class)
public class CompletedChallengeDaoTest extends DatabaseTest {

    @Test
    public void insertAndGetChallenge() {
        CompletedChallengeEntity challenge = TestUtil.createCompletedChallengeEntity("foo", "username", "name");
        database.completedChallengeDao().insert(challenge);
        database.completedChallengeDao()
                .getCompletedChallengesByUser("username")
                .test()
                .assertValue(completedChallengeEntities -> {
                    // Our result is the first item
                    CompletedChallengeEntity challengeResult = completedChallengeEntities.get(0);
                    return challengeResult.getId().equals(challenge.getId())
                            && challengeResult.getName().equals(challenge.getName());
                });
    }

    @Test
    public void insertAllAndGetAllChallenges() {
        CompletedChallengeEntity challenge1 = TestUtil.createCompletedChallengeEntity("foo1", "username1", "name1");
        CompletedChallengeEntity challenge2 = TestUtil.createCompletedChallengeEntity("foo2", "username2", "name2");
        List<CompletedChallengeEntity> challenges = new ArrayList<>();
        challenges.add(challenge1);
        challenges.add(challenge2);
        database.completedChallengeDao().insertAll(challenges);
        database.completedChallengeDao()
                .getCompletedChallengesByUser("username2")
                .test()
                .assertValue(completedChallengeEntities -> {
                    // Our result is the first item
                    CompletedChallengeEntity challengeResult = completedChallengeEntities.get(0);
                    return challengeResult.getId().equals(challenge2.getId())
                            && challengeResult.getName().equals(challenge2.getName())
                            && challengeResult.getUserId().equals(challenge2.getUserId());
                });
    }

    @Test
    public void updateAndGetChallenge() {
        CompletedChallengeEntity challenge = TestUtil.createCompletedChallengeEntity("foo", "username", "name");
        database.completedChallengeDao().insert(challenge);
        CompletedChallengeEntity updatedChallenge = TestUtil.createCompletedChallengeEntity("foo", "username", "updated_name");
        database.completedChallengeDao().update(updatedChallenge);
        database.completedChallengeDao().getCompletedChallengesByUser("username")
                .test()
                .assertValue(completedChallengeEntities -> {
                    CompletedChallengeEntity challengeResult = completedChallengeEntities.get(0);
                    return challengeResult.getId().equals(challenge.getId())
                            && challengeResult.getName().equals("updated_name");
                });
        // Insert should replace if the model already exist
        CompletedChallengeEntity updatedChallenge2 = TestUtil.createCompletedChallengeEntity("foo", "username", "updated_name_2");
        database.completedChallengeDao().insert(updatedChallenge2);
        database.completedChallengeDao().getCompletedChallengesByUser("username")
                .test()
                .assertValue(completedChallengeEntities -> {
                    CompletedChallengeEntity challengeResult = completedChallengeEntities.get(0);
                    return challengeResult.getId().equals(challenge.getId())
                            && challengeResult.getName().equals("updated_name_2");
                });
    }

    @Test
    public void deleteAndGetChallenge() {
        CompletedChallengeEntity challenge = TestUtil.createCompletedChallengeEntity("foo", "username", "name");
        database.completedChallengeDao().insert(challenge);
        database.completedChallengeDao().delete(challenge);
        database.completedChallengeDao().getCompletedChallengesByUser("username")
                .test()
                .assertValue(completedChallengeEntities -> completedChallengeEntities.isEmpty());
    }
}
