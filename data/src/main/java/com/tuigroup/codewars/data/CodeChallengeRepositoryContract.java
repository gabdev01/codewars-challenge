package com.tuigroup.codewars.data;

import com.google.common.base.Optional;
import com.tuigroup.codewars.data.local.model.CodeChallengeEntity;
import com.tuigroup.codewars.data.util.Resource;

import io.reactivex.Flowable;

public interface CodeChallengeRepositoryContract {

    Flowable<Resource<Optional<CodeChallengeEntity>>> getChallenge(String challengeId);
}


