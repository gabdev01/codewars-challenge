package com.tuigroup.codewars.data.remote;

import com.tuigroup.codewars.data.remote.model.CodeChallenge;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface CodeChallengeRestApi {

    @GET("code-challenges/{codeChallengeId}")
    Single<CodeChallenge> getCodeChallenge(
            @Path("codeChallengeId") String codeChallengeId
    );
}
