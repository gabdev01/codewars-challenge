package com.tuigroup.codewars.data;

import com.tuigroup.codewars.data.local.AuthoredChallengeDao;
import com.tuigroup.codewars.data.local.CompletedChallengeDao;
import com.tuigroup.codewars.data.local.UserDao;
import com.tuigroup.codewars.data.local.UserSearchHistoryDao;
import com.tuigroup.codewars.data.local.model.AuthoredChallengeEntity;
import com.tuigroup.codewars.data.local.model.UserEntity;
import com.tuigroup.codewars.data.local.model.UserSearchHistory;
import com.tuigroup.codewars.data.mapper.AuthoredChallengeMapper;
import com.tuigroup.codewars.data.mapper.UserMapper;
import com.tuigroup.codewars.data.remote.UserRestApi;
import com.tuigroup.codewars.data.remote.model.AuthoredChallenge;
import com.tuigroup.codewars.data.remote.model.AuthoredChallengeResponse;
import com.tuigroup.codewars.data.remote.model.User;
import com.tuigroup.codewars.data.util.Resource;
import com.tuigroup.codewars.data.util.Status;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Single;
import io.reactivex.observers.TestObserver;
import io.reactivex.schedulers.TestScheduler;
import io.reactivex.subscribers.TestSubscriber;

import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class UsersRepositoryTest {

    private static final String USERNAME_TEST = "username_test";
    private static final String CHALLENGE_ID_TEST = "challenge_id_test";

    @Mock
    private UserRestApi userRestApi;
    @Mock
    private UserDao userDao;
    @Mock
    private CompletedChallengeDao completedChallengeDao;
    @Mock
    private AuthoredChallengeDao authoredChallengeDao;
    @Mock
    private UserSearchHistoryDao searchUserHistoryDao;
    @Mock
    private UserMapper userMapper;
    @Mock
    private AuthoredChallengeMapper authoredChallengeMapper;

    private UserRepository userRepository;


    @Before
    public void setUp() {
        userRepository = new UserRepository(userRestApi, userDao, completedChallengeDao,
                authoredChallengeDao, searchUserHistoryDao, userMapper, authoredChallengeMapper);

        TestScheduler TestScheduler = new TestScheduler();
    }

    @Test
    public void getUserByIdFromApi() {
        User user = TestUtil.createUser(USERNAME_TEST, "name", 1);
        UserEntity userEntity = TestUtil.createUserEntity(USERNAME_TEST, "name", 1);
        Mockito.when(userRestApi.getUser(USERNAME_TEST)).thenReturn(Single.just(user));
        Mockito.when(userMapper.mapFromApiToEntity(user)).thenReturn(userEntity);
        Mockito.when(userDao.getUserById(USERNAME_TEST)).thenReturn(Flowable.just(userEntity));

        TestObserver<UserEntity> testObserver = userRepository.getUser(USERNAME_TEST).test();

        testObserver.assertValue(userEntity);
    }

    @Test
    public void getLastUserSearchOrderByDateFromLocal() {
        List<UserSearchHistory> usersSearchHistory = TestUtil.createUserSearchHistoryList();
        Mockito.when(searchUserHistoryDao.getLastUsersSearched(5)).thenReturn(Flowable.just(usersSearchHistory));

        TestSubscriber<List<UserSearchHistory>> testObserver = userRepository.getLastUsersSearched(
                UserRepositoryContract.UserOrderBy.DATE_ADDED, 5).test();

        testObserver.assertValue(usersSearchHistory);
    }

    @Test
    public void getAuthoredChallengeFromApi() {
        AuthoredChallenge authoredChallenge =
                TestUtil.createAuthoredChallenge(CHALLENGE_ID_TEST, "category");
        AuthoredChallengeEntity authoredChallengeEntity =
                TestUtil.createAuthoredChallengeEntity(CHALLENGE_ID_TEST, "username", "category");
        List<AuthoredChallenge> authoredChallenges = Arrays.asList(authoredChallenge);
        List<AuthoredChallengeEntity> authoredChallengeEntities = Arrays.asList(authoredChallengeEntity);

        Mockito.when(userRestApi.getAuthoredChallenges(CHALLENGE_ID_TEST))
                .thenReturn(Single.just(new AuthoredChallengeResponse(authoredChallenges)));
        Mockito.when(authoredChallengeMapper.mapFromApiToEntity(authoredChallenges, USERNAME_TEST))
                .thenReturn(authoredChallengeEntities);
        Mockito.when(authoredChallengeDao.getAuthoredChallenges(USERNAME_TEST))
                .thenReturn(Flowable.just(authoredChallengeEntities));

        TestSubscriber<Resource<List<AuthoredChallengeEntity>>> testSubscriber = userRepository.getAuthoredChallenges(USERNAME_TEST).test();

        assertTrue(testSubscriber.values().get(0).status == Status.LOCAL);
        assertTrue(testSubscriber.values().get(0).data.equals(authoredChallengeEntities));
    }
}
