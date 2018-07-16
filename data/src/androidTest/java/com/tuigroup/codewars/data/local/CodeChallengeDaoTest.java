package com.tuigroup.codewars.data.local;

import android.support.test.runner.AndroidJUnit4;

import com.tuigroup.codewars.data.local.model.CodeChallengeEntity;
import com.tuigroup.codewars.data.TestUtil;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class CodeChallengeDaoTest extends DatabaseTest {

    @Test
    public void insertAndGetChallenge() {
        CodeChallengeEntity challenge = TestUtil.createCodeChallengeEntity("foo", "text");
        database.codeChallengeDao().insert(challenge);
        database.codeChallengeDao()
                .getChallengeById("foo")
                .test()
                .assertValue(codeChallengeEntities -> {
                    // Our result is the first item
                    CodeChallengeEntity challengeResult = codeChallengeEntities.get(0);
                    return challengeResult.getId().equals(challenge.getId())
                            && challengeResult.getCategory().equals(challenge.getCategory());
                });
    }

    @Test
    public void updateAndGetChallenge() {
        CodeChallengeEntity challenge = TestUtil.createCodeChallengeEntity("foo", "text");
        database.codeChallengeDao().insert(challenge);
        CodeChallengeEntity updatedChallenge = TestUtil.createCodeChallengeEntity("foo", "updated_text");
        database.codeChallengeDao().update(updatedChallenge);
        database.codeChallengeDao().getChallengeById("foo")
                .test()
                .assertValue(codeChallengeEntities -> {
                    CodeChallengeEntity challengeResult = codeChallengeEntities.get(0);
                    return challengeResult.getId().equals(challenge.getId())
                            && challengeResult.getCategory().equals("updated_text");
                });
        // Insert should replace if the model already exist
        CodeChallengeEntity updatedChallenge2 = TestUtil.createCodeChallengeEntity("foo", "updated_text_2");
        database.codeChallengeDao().insert(updatedChallenge2);
        database.codeChallengeDao().getChallengeById("foo")
                .test()
                .assertValue(codeChallengeEntities -> {
                    CodeChallengeEntity challengeResult = codeChallengeEntities.get(0);
                    return challengeResult.getId().equals(challenge.getId())
                            && challengeResult.getCategory().equals("updated_text_2");
                });
    }

    @Test
    public void deleteAndGetChallenge() {
        CodeChallengeEntity challenge = TestUtil.createCodeChallengeEntity("foo", "text");
        database.codeChallengeDao().insert(challenge);
        database.codeChallengeDao().delete(challenge);
        database.codeChallengeDao().getChallengeById("foo")
                .test()
                .assertValue(codeChallengeEntities -> codeChallengeEntities.isEmpty());
    }
}
