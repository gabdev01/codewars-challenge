package com.tuigroup.codewars.data.mapper;

import com.tuigroup.codewars.data.local.model.CodeChallengeEntity;
import com.tuigroup.codewars.data.remote.model.CodeChallenge;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class CodeChallengeMapper {

    @Inject
    public CodeChallengeMapper() {

    }

    public CodeChallengeEntity mapFromApiToEntity(CodeChallenge codeChallenge) {
        CodeChallengeEntity codeChallengeEntity = new CodeChallengeEntity(
                codeChallenge.getId(),
                codeChallenge.getName(),
                codeChallenge.getSlug(),
                codeChallenge.getCategory(),
                codeChallenge.getLanguages(),
                codeChallenge.getUrl(),
                codeChallenge.getDescription(),
                codeChallenge.getTotalAttempts(),
                codeChallenge.getTotalCompleted(),
                codeChallenge.getTotalStars(),
                codeChallenge.getTags()
        );
        return codeChallengeEntity;
    }
}
