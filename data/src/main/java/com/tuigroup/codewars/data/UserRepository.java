package com.tuigroup.codewars.data;

import android.arch.paging.DataSource;
import android.arch.paging.PagedList;
import android.arch.paging.RxPagedListBuilder;

import com.tuigroup.codewars.data.local.AuthoredChallengeDao;
import com.tuigroup.codewars.data.local.CompletedChallengeDao;
import com.tuigroup.codewars.data.local.UserDao;
import com.tuigroup.codewars.data.local.UserSearchHistoryDao;
import com.tuigroup.codewars.data.local.model.AuthoredChallengeEntity;
import com.tuigroup.codewars.data.local.model.CompletedChallengeEntity;
import com.tuigroup.codewars.data.local.model.UserEntity;
import com.tuigroup.codewars.data.local.model.UserSearchHistory;
import com.tuigroup.codewars.data.local.model.UserSearchHistoryEntity;
import com.tuigroup.codewars.data.mapper.AuthoredChallengeMapper;
import com.tuigroup.codewars.data.mapper.UserMapper;
import com.tuigroup.codewars.data.remote.UserRestApi;
import com.tuigroup.codewars.data.remote.model.AuthoredChallenge;
import com.tuigroup.codewars.data.util.NetworkBoundSource;
import com.tuigroup.codewars.data.util.Resource;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.functions.Function;

@Singleton
public class UserRepository {

    private static final int COMPLETED_CHALLENGES_PAGE_SIZE = 50;

    private UserRestApi userRestApi;
    private UserDao userDao;
    private CompletedChallengeDao completedChallengeDao;
    private AuthoredChallengeDao authoredChallengeDao;
    private UserSearchHistoryDao searchUserHistoryDao;

    @Inject
    UserRepository(UserRestApi userRestApi,
                   UserDao userDao,
                   CompletedChallengeDao completedChallengeDao,
                   AuthoredChallengeDao authoredChallengeDao,
                   UserSearchHistoryDao searchUserHistoryDao) {
        this.userRestApi = userRestApi;
        this.userDao = userDao;
        this.completedChallengeDao = completedChallengeDao;
        this.authoredChallengeDao = authoredChallengeDao;
        this.searchUserHistoryDao = searchUserHistoryDao;
    }

    public Single<UserEntity> getUser(String username) {
        return userRestApi.getUser(username)
                .map(UserMapper::mapFromApiToEntity)
                .doOnSuccess(user -> {
                    userDao.insert(user);
                    UserSearchHistoryEntity history = new UserSearchHistoryEntity(
                            new Date(),
                            user.getUsername()
                    );
                    searchUserHistoryDao.insert(history);
                });
    }

    public Flowable<List<UserSearchHistory>> getLastUsersSearched(int limit) {
        return searchUserHistoryDao.getLastUsersSearched(limit);
    }

    public Flowable<List<UserSearchHistory>> getLastUsersSearched(UserOrderBy orderBy, int limit) {
        if (orderBy == UserOrderBy.HIGHEST_RANK) {
            return searchUserHistoryDao.getLastUsersSearchedByRank(limit);
        } else {
            return searchUserHistoryDao.getLastUsersSearched(limit);
        }
    }

    public Observable<PagedList<CompletedChallengeEntity>> getCompletedChallenges(CompletedChallengePageBoundaryCallback callback, String username) {
        callback.setUsername(username);
        DataSource.Factory localData = completedChallengeDao.getCompletedChallengesByUser2(callback.getUsername());
        PagedList.Config pagedList = new PagedList.Config.Builder()
                .setPageSize(COMPLETED_CHALLENGES_PAGE_SIZE)
                .setEnablePlaceholders(false)
                .setInitialLoadSizeHint(COMPLETED_CHALLENGES_PAGE_SIZE * 3)
                .setPrefetchDistance(COMPLETED_CHALLENGES_PAGE_SIZE)
                .build();
        Observable<PagedList<CompletedChallengeEntity>> result = new RxPagedListBuilder<>(localData, pagedList)
                .setBoundaryCallback(callback)
                .buildObservable();
        return result;
    }

    public Flowable<Resource<List<AuthoredChallengeEntity>>> getAuthoredChallenges(String username) {
        return Flowable.create(emitter -> new NetworkBoundSource<List<AuthoredChallengeEntity>, List<AuthoredChallenge>>(emitter) {
            @Override
            public Single<List<AuthoredChallenge>> getRemote() {
                return userRestApi.getAuthoredChallenges(username)
                        .map(response -> response.getData());
            }

            @Override
            public Flowable<List<AuthoredChallengeEntity>> getLocal() {
                return authoredChallengeDao.getAuthoredChallenge(username);
            }

            @Override
            public void saveCallResult(List<AuthoredChallengeEntity> data) {
                authoredChallengeDao.insertAll(data);
            }

            @Override
            public Function<List<AuthoredChallenge>, List<AuthoredChallengeEntity>> mapper() {
                return challenge -> AuthoredChallengeMapper.mapFromApiToEntity(challenge, username);
            }
        }, BackpressureStrategy.LATEST);
    }

    public enum UserOrderBy {
        DATE_ADDED,
        HIGHEST_RANK
    }
}
