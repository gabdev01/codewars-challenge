package com.tuigroup.codewars.data;

import com.google.common.base.Optional;
import com.tuigroup.codewars.data.local.CodeChallengeDao;
import com.tuigroup.codewars.data.local.model.CodeChallengeEntity;
import com.tuigroup.codewars.data.mapper.CodeChallengeMapper;
import com.tuigroup.codewars.data.remote.CodeChallengeRestApi;
import com.tuigroup.codewars.data.remote.model.CodeChallenge;
import com.tuigroup.codewars.data.util.NetworkBoundSource;
import com.tuigroup.codewars.data.util.Resource;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.Single;
import io.reactivex.functions.Function;

@Singleton
public class CodeChallengeRepository implements CodeChallengeRepositoryContract {

    private CodeChallengeRestApi codeChallengeRestApi;
    private CodeChallengeDao codeChallengeDao;

    private CodeChallengeMapper codeChallengeMapper;

    @Inject
    CodeChallengeRepository(CodeChallengeRestApi codeChallengeRestApi,
                            CodeChallengeDao codeChallengeDao,
                            CodeChallengeMapper codeChallengeMapper) {
        this.codeChallengeRestApi = codeChallengeRestApi;
        this.codeChallengeDao = codeChallengeDao;
        this.codeChallengeMapper = codeChallengeMapper;
    }

    public Flowable<Resource<Optional<CodeChallengeEntity>>> getChallenge(String challengeId) {
        return Flowable.create(emitter -> new NetworkBoundSource<Optional<CodeChallengeEntity>, CodeChallenge>(emitter) {
            @Override
            public Single<CodeChallenge> getRemote() {
                return codeChallengeRestApi.getCodeChallenge(challengeId);
            }

            @Override
            public Flowable<Optional<CodeChallengeEntity>> getLocal() {
                return codeChallengeDao.getChallengeById(challengeId)
                        .flatMap(challenge -> !challenge.isEmpty()
                                ? Flowable.just(Optional.of(challenge.get(0)))
                                : Flowable.just(Optional.absent()));
            }

            @Override
            public void saveCallResult(Optional<CodeChallengeEntity> data) {
                if (data.isPresent()) {
                    codeChallengeDao.insert(data.get());
                }
            }

            @Override
            public Function<CodeChallenge, Optional<CodeChallengeEntity>> mapper() {
                return challenge -> Optional.of(codeChallengeMapper.mapFromApiToEntity(challenge));
            }
        }, BackpressureStrategy.LATEST);
    }
}
