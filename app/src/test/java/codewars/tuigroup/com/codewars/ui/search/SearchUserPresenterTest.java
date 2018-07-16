package codewars.tuigroup.com.codewars.ui.search;

import com.tuigroup.codewars.data.UserRepositoryContract;
import com.tuigroup.codewars.data.local.model.UserEntity;
import com.tuigroup.codewars.data.local.model.UserSearchHistory;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import codewars.tuigroup.com.codewars.util.TestSchedulerProvider;
import codewars.tuigroup.com.codewars.util.TestUtil;
import io.reactivex.Flowable;
import io.reactivex.Single;
import io.reactivex.schedulers.TestScheduler;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class SearchUserPresenterTest {

    private final static int SEARCH_USER_HISTORY_LIMIT = 5;

    @Mock
    private UserRepositoryContract userRepository;
    @Mock
    private SearchUserContract.View searchUserView;

    private TestScheduler testScheduler;
    private SearchUserPresenter searchUserPresenter;

    @BeforeClass
    public static void onlyOnce() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
        testScheduler = new TestScheduler();
        TestSchedulerProvider testSchedulerProvider = new TestSchedulerProvider(testScheduler);
        searchUserPresenter = new SearchUserPresenter(userRepository, testSchedulerProvider);
        searchUserPresenter.attachView(searchUserView);
    }

    @Test
    public void searchUserFromRepositoryAndLoadIntoView() {
        UserEntity userResponse = TestUtil.createUserEntity("foo", "name", 1);
        doReturn(Single.just(userResponse))
                .when(userRepository)
                .getUser("foo");

        searchUserPresenter.searchUser("foo");

        testScheduler.triggerActions();

        verify(searchUserView).showSearchUserIndicator(true);
        verify(searchUserView).showSearchUserIndicator(false);
        verify(searchUserView).showSearchUserSuccess(userResponse);
    }

    @Test
    public void getNoSearchHistoryFromRepositoryAndLoadIntoView() {
        doReturn(Flowable.just(new ArrayList<>()))
                .when(userRepository)
                .getLastUsersSearched(UserRepositoryContract.UserOrderBy.DATE_ADDED, SEARCH_USER_HISTORY_LIMIT);

        searchUserPresenter.loadSearchHistory(UserRepositoryContract.UserOrderBy.DATE_ADDED);

        testScheduler.triggerActions();

        verify(searchUserView).showNoUsersSearchHistory();
    }

    @Test
    public void getSearchHistoryFromRepositoryAndLoadIntoView() {
        List<UserSearchHistory> result = TestUtil.createUserSearchHistoryList();
        doReturn(Flowable.just(result))
                .when(userRepository)
                .getLastUsersSearched(UserRepositoryContract.UserOrderBy.DATE_ADDED, SEARCH_USER_HISTORY_LIMIT);

        searchUserPresenter.loadSearchHistory(UserRepositoryContract.UserOrderBy.DATE_ADDED);

        testScheduler.triggerActions();

        verify(searchUserView).showUsersSearchHistory(result);
    }

    @Test
    public void getSearchHistoryWithErrorFromRepositoryAndLoadIntoView() {
        doReturn(Flowable.just(new Throwable()))
                .when(userRepository)
                .getLastUsersSearched(UserRepositoryContract.UserOrderBy.DATE_ADDED, SEARCH_USER_HISTORY_LIMIT);

        searchUserPresenter.loadSearchHistory(UserRepositoryContract.UserOrderBy.DATE_ADDED);

        testScheduler.triggerActions();

        verify(searchUserView).showUsersSearchHistoryError();
    }

    @After
    public void tearDown() throws Exception {
        searchUserPresenter.detachView();
    }
}
