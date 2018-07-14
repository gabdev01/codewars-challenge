package com.tuigroup.codewars.data;

import com.tuigroup.codewars.data.local.CodeChallengeDao;
import com.tuigroup.codewars.data.local.model.CodeChallengeEntity;
import com.tuigroup.codewars.data.mapper.CodeChallengeMapper;
import com.tuigroup.codewars.data.remote.CodeChallengeRestApi;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Single;

@Singleton
public class CodeChallengeRepository {

    private CodeChallengeRestApi codeChallengeRestApi;
    private CodeChallengeDao codeChallengeDao;

    @Inject
    CodeChallengeRepository(CodeChallengeRestApi codeChallengeRestApi,
                            CodeChallengeDao codeChallengeDao) {
        this.codeChallengeRestApi = codeChallengeRestApi;
        this.codeChallengeDao = codeChallengeDao;
    }

    public Single<CodeChallengeEntity> getChallenge(String challengeId) {
        return codeChallengeRestApi.getCodeChallenge(challengeId)
                .map(CodeChallengeMapper::mapFromApiToEntity)
                .doOnSuccess(challenge -> {
                    codeChallengeDao.insert(challenge);
                });
    }
}
