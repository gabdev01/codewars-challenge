package com.tuigroup.codewars.data;

import com.tuigroup.codewars.data.local.UsersDao;
import com.tuigroup.codewars.data.local.model.UserEntity;
import com.tuigroup.codewars.data.mapper.UserMapper;
import com.tuigroup.codewars.data.remote.UsersRestApi;
import com.tuigroup.codewars.data.remote.model.AuthoredChallengeResponse;
import com.tuigroup.codewars.data.remote.model.CompletedChallengeResponse;
import com.tuigroup.codewars.data.remote.model.User;
import com.tuigroup.codewars.data.util.NetworkBoundSource;
import com.tuigroup.codewars.data.util.Resource;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.Single;
import io.reactivex.functions.Function;

public class UsersRepository {

    private static UsersRepository INSTANCE;

    private UsersRestApi usersRestApi;
    private UsersDao usersDao;

    private UsersRepository(UsersRestApi usersRestApi, UsersDao usersDao) {
        this.usersRestApi = usersRestApi;
        this.usersDao = usersDao;
    }

    public static UsersRepository getInstance(UsersRestApi usersRestApi, UsersDao usersDao) {
        if (INSTANCE == null) {
            INSTANCE = new UsersRepository(usersRestApi, usersDao);
        }
        return INSTANCE;
    }

    public Flowable<Resource<UserEntity>> getUser(String username) {
        return Flowable.create(emitter -> new NetworkBoundSource<UserEntity, User>(emitter) {
            @Override
            public Single<User> getRemote() {
                return usersRestApi.getUser(username);
            }

            @Override
            public Flowable<UserEntity> getLocal() {
                return usersDao.getUserById(username);
            }

            @Override
            public void saveCallResult(UserEntity data) {
                usersDao.insert(data);
            }

            @Override
            public Function<User, UserEntity> mapper() {
                return UserMapper::mapFromApiToEntity;
            }
        }, BackpressureStrategy.BUFFER);
    }

    public Single<CompletedChallengeResponse> getCompletedChallenges(String username, int page) {
        return usersRestApi.getCompletedChallenges(username, page);
    }

    public Single<AuthoredChallengeResponse> getAuthoredChallenges(String username) {
        return usersRestApi.getAuthoredChallenges(username);
    }
}
