package com.tuigroup.codewars.data.remote;

import com.tuigroup.codewars.data.remote.model.AuthoredChallengeResponse;
import com.tuigroup.codewars.data.remote.model.CompletedChallengeResponse;
import com.tuigroup.codewars.data.remote.model.User;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface UsersRestApi {

    @GET("users/{username}")
    Single<User> getUser(
            @Path("username") String username
    );

    @GET("users/{username}/code-challenges/completed")
    Single<CompletedChallengeResponse> getCompletedChallenges(
            @Path("username") String username,
            @Query("page") int page
    );

    @GET("users/{username}/code-challenges/authored")
    Single<AuthoredChallengeResponse> getAuthoredChallenges(
            @Path("username") String username
    );
}

