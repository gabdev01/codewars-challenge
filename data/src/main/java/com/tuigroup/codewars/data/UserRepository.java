package com.tuigroup.codewars.data;

import com.tuigroup.codewars.data.local.UserDao;
import com.tuigroup.codewars.data.local.UserSearchHistoryDao;
import com.tuigroup.codewars.data.local.model.UserSearchHistory;
import com.tuigroup.codewars.data.local.model.UserSearchHistoryEntity;
import com.tuigroup.codewars.data.local.model.UserEntity;
import com.tuigroup.codewars.data.mapper.UserMapper;
import com.tuigroup.codewars.data.remote.UserRestApi;
import com.tuigroup.codewars.data.remote.model.AuthoredChallengeResponse;
import com.tuigroup.codewars.data.remote.model.CompletedChallengeResponse;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Flowable;
import io.reactivex.Single;

@Singleton
public class UserRepository {

    private static UserRepository INSTANCE;

    private UserRestApi userRestApi;
    private UserDao userDao;
    private UserSearchHistoryDao searchUserHistoryDao;

    @Inject
    UserRepository(UserRestApi userRestApi,
                   UserDao userDao,
                   UserSearchHistoryDao searchUserHistoryDao) {
        this.userRestApi = userRestApi;
        this.userDao = userDao;
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

    public Single<List<CompletedChallenge>> getCompletedChallenges(String username, int page) {
        return userRestApi.getCompletedChallenges(username, page)
                .map(response -> {
                    return response.getData();
                });
    }

    public Single<AuthoredChallengeResponse> getAuthoredChallenges(String username) {
        return userRestApi.getAuthoredChallenges(username);
        /*return Flowable.create(emitter -> new NetworkBoundSource<UserEntity, User>(emitter) {
            @Override
            public Single<User> getRemote() {
                return userRestApi.getUser(username);
            }

            @Override
            public Flowable<UserEntity> getLocal() {
                return userDao.getUserById(username);
            }

            @Override
            public void saveCallResult(UserEntity data) {
                userDao.insert(data);
            }

            @Override
            public Function<User, UserEntity> mapper() {
                return UserMapper::mapFromApiToEntity;
            }
        }, BackpressureStrategy.BUFFER);*/
    }
}
