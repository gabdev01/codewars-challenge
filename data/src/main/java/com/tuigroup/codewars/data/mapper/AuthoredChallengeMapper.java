package com.tuigroup.codewars.data.mapper;

import com.tuigroup.codewars.data.local.model.AuthoredChallengeEntity;
import com.tuigroup.codewars.data.remote.model.AuthoredChallenge;

import java.util.ArrayList;
import java.util.List;

public class AuthoredChallengeMapper {

    public static List<AuthoredChallengeEntity> mapFromApiToEntity(List<AuthoredChallenge> authoredChallenges, String username) {
        List<AuthoredChallengeEntity> authoredChallengeEntities = new ArrayList<>();
        for (AuthoredChallenge authoredChallenge : authoredChallenges) {
            authoredChallengeEntities.add(mapFromApiToEntity(authoredChallenge, username));
        }
        return authoredChallengeEntities;
    }

    public static AuthoredChallengeEntity mapFromApiToEntity(AuthoredChallenge authoredChallenge, String username) {
        AuthoredChallengeEntity authoredChallengeEntity = new AuthoredChallengeEntity(
                authoredChallenge.getId(),
                username,
                authoredChallenge.getCategory(),
                authoredChallenge.getDescription(),
                authoredChallenge.getName(),
                authoredChallenge.getOutput(),
                authoredChallenge.getPublishedStatus(),
                authoredChallenge.getRank(),
                authoredChallenge.getState(),
                authoredChallenge.getStatus(),
                authoredChallenge.getTotalAttempts(),
                authoredChallenge.getTotalCompleted(),
                authoredChallenge.getTotalUpVotes(),
                authoredChallenge.getTotalVoteScore()
        );
        return authoredChallengeEntity;
    }
}
