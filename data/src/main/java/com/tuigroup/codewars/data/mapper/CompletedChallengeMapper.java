package com.tuigroup.codewars.data.mapper;

import com.tuigroup.codewars.data.local.model.CompletedChallengeEntity;
import com.tuigroup.codewars.data.remote.model.CompletedChallenge;

import java.util.ArrayList;
import java.util.List;

public class CompletedChallengeMapper {

    public static List<CompletedChallengeEntity> mapFromApiToEntity(
            List<CompletedChallenge> completedChallenges, String username, int pageIndex) {

        List<CompletedChallengeEntity> completedChallengeEntities = new ArrayList<>();
        for (CompletedChallenge completedChallenge : completedChallenges) {
            completedChallengeEntities.add(mapFromApiToEntity(completedChallenge, username, pageIndex));
        }
        return completedChallengeEntities;
    }

    public static CompletedChallengeEntity mapFromApiToEntity(
            CompletedChallenge completedChallenge, String username, int pageIndex) {

        CompletedChallengeEntity completedChallengeEntity = new CompletedChallengeEntity(
                completedChallenge.getId(),
                username,
                completedChallenge.getName(),
                completedChallenge.getSlug(),
                completedChallenge.getCompletedAt(),
                completedChallenge.getCompletedLanguages(),
                pageIndex
        );
        return completedChallengeEntity;
    }
}
