package com.tuigroup.codewars.data;

import android.arch.paging.PagedList;

import com.tuigroup.codewars.data.local.model.AuthoredChallengeEntity;
import com.tuigroup.codewars.data.local.model.CompletedChallengeEntity;
import com.tuigroup.codewars.data.local.model.UserEntity;
import com.tuigroup.codewars.data.local.model.UserSearchHistory;
import com.tuigroup.codewars.data.paging.ObservableBoundaryCallback;
import com.tuigroup.codewars.data.util.Resource;
import com.tuigroup.codewars.data.util.UserOrderBy;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.Single;

public interface UserRepositoryContract {

    Single<UserEntity> getUser(String username);

    Flowable<List<UserSearchHistory>> getLastUsersSearched(int limit);

    Flowable<List<UserSearchHistory>> getLastUsersSearched(UserOrderBy orderBy, int limit);

    Observable<PagedList<CompletedChallengeEntity>> getCompletedChallenges(
            ObservableBoundaryCallback boundaryCallback,
            String username);

    Flowable<Resource<List<AuthoredChallengeEntity>>> getAuthoredChallenges(String username);
}


