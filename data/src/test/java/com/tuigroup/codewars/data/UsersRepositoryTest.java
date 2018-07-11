package com.tuigroup.codewars.data;

import com.tuigroup.codewars.data.remote.UsersRestApi;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class UsersRepositoryTest {

    private static final String FAKE_USERNAME = "David";

    private UsersRepository usersRepository;

    @Mock
    private UsersRestApi mockUsersRestApi;

    @Before
    public void setUp() {
        usersRepository = UsersRepository.getInstance(mockUsersRestApi);
        ;
    }

    @Test
    public void testGetUserEntityDetailsFromApi() {
       /* User fakeUser = new User(
                "",
                "",
                10,
                "Clan",
                11,
                new ArrayList<>(),
                new Rank(),
                null
        );

        Single<User> fakeObservable = Single.just(fakeUser);
        given(mockUsersRestApi.getUser(FAKE_USERNAME)).willReturn(fakeObservable);

        usersRepository.getUser(FAKE_USERNAME);

        verify(mockUsersRestApi).getUser(FAKE_USERNAME);*/
    }
}
