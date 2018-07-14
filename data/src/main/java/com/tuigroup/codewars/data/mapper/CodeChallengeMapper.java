package com.tuigroup.codewars.data.mapper;

import com.tuigroup.codewars.data.local.model.CodeChallengeEntity;
import com.tuigroup.codewars.data.remote.model.CodeChallenge;

public class CodeChallengeMapper {

    public static CodeChallengeEntity mapFromApiToEntity(CodeChallenge codeChallenge) {
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
