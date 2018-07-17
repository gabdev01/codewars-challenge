package com.tuigroup.codewars.data;

import com.tuigroup.codewars.data.local.AuthoredChallengeDao;
import com.tuigroup.codewars.data.local.CodeChallengeDao;
import com.tuigroup.codewars.data.local.CompletedChallengeDao;
import com.tuigroup.codewars.data.local.UserDao;
import com.tuigroup.codewars.data.local.UserSearchHistoryDao;
import com.tuigroup.codewars.data.local.model.CompletedChallengeEntity;
import com.tuigroup.codewars.data.mapper.AuthoredChallengeMapper;
import com.tuigroup.codewars.data.mapper.CodeChallengeMapper;
import com.tuigroup.codewars.data.mapper.UserMapper;
import com.tuigroup.codewars.data.paging.CompletedChallengesBoundaryCallback;
import com.tuigroup.codewars.data.paging.ObservableBoundaryCallback;
import com.tuigroup.codewars.data.remote.CodeChallengeRestApi;
import com.tuigroup.codewars.data.remote.UserRestApi;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class RepositoriesModule {

    @Singleton
    @Provides
    UserRepositoryContract provideUserRepository(UserRestApi userRestApi,
                                                 UserDao userDao,
                                                 CompletedChallengeDao completedChallengeDao,
                                                 AuthoredChallengeDao authoredChallengeDao,
                                                 UserSearchHistoryDao searchUserHistoryDao,
                                                 UserMapper userMapper,
                                                 AuthoredChallengeMapper authoredChallengeMapper) {
        return new UserRepository(userRestApi,
                userDao,
                completedChallengeDao,
                authoredChallengeDao,
                searchUserHistoryDao,
                userMapper,
                authoredChallengeMapper);
    }

    @Singleton
    @Provides
    CodeChallengeRepositoryContract provideCodeChallengeRepository(CodeChallengeRestApi codeChallengeRestApi,
                                                                   CodeChallengeDao codeChallengeDao,
                                                                   CodeChallengeMapper codeChallengeMapper) {
        return new CodeChallengeRepository(codeChallengeRestApi, codeChallengeDao, codeChallengeMapper);
    }

    @Provides
    ObservableBoundaryCallback<CompletedChallengeEntity, String> provideCompletedChallengesBoundaryCallback(UserRestApi userRestApi,
                                                                                                            CompletedChallengeDao completedChallengeDao) {
        return new CompletedChallengesBoundaryCallback(userRestApi, completedChallengeDao);
    }
}
