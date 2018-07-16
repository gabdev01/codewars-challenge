package codewars.tuigroup.com.codewars.ui.challenges.authored;

import com.tuigroup.codewars.data.UserRepositoryContract;
import com.tuigroup.codewars.data.local.model.AuthoredChallengeEntity;
import com.tuigroup.codewars.data.util.Resource;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;

import codewars.tuigroup.com.codewars.util.TestSchedulerProvider;
import codewars.tuigroup.com.codewars.util.TestUtil;
import io.reactivex.Flowable;
import io.reactivex.schedulers.TestScheduler;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class AuthoredChallengesPresenterTest {

    private static final String USERNAME_TEST = "username_test";

    @Mock
    private UserRepositoryContract userRepository;
    @Mock
    private AuthoredChallengesContract.View authoredChallengesView;

    private TestScheduler testScheduler;
    private AuthoredChallengesPresenter authoredChallengesPresenter;

    @BeforeClass
    public static void onlyOnce() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
        testScheduler = new TestScheduler();
        TestSchedulerProvider testSchedulerProvider = new TestSchedulerProvider(testScheduler);
        authoredChallengesPresenter = new AuthoredChallengesPresenter(
                userRepository, testSchedulerProvider, USERNAME_TEST);
        authoredChallengesPresenter.attachView(authoredChallengesView);
    }

    @Test
    public void loadChallengesFromRepositoryAndLoadIntoView() {
        Resource<List<AuthoredChallengeEntity>> result = Resource.local(TestUtil.createAuthoredChallengeEntityList());
        doReturn(Flowable.just(result))
                .when(userRepository)
                .getAuthoredChallenges(USERNAME_TEST);

        authoredChallengesPresenter.loadChallenges();

        testScheduler.triggerActions();

        verify(authoredChallengesView).showLoadingChallengesIndicator(true);
        verify(authoredChallengesView).showLoadingChallengesIndicator(false);
        verify(authoredChallengesView).showChallenges(result.data);
    }

    @Test
    public void loadChallengesWithErrorFromRepositoryAndLoadIntoView() {
        doReturn(Flowable.just(new Throwable()))
                .when(userRepository)
                .getAuthoredChallenges(USERNAME_TEST);

        authoredChallengesPresenter.loadChallenges();

        testScheduler.triggerActions();

        verify(authoredChallengesView).showLoadingChallengesIndicator(true);
        verify(authoredChallengesView).showLoadingChallengesIndicator(false);
        verify(authoredChallengesView).showLoadingChallengesError();
    }

    @Test
    public void openChallengeFromRepositoryAndLoadIntoView() {
        AuthoredChallengeEntity result = TestUtil.createAuthoredChallengeEntity(
                "id1", "username1", "cat1");

        authoredChallengesPresenter.openChallenge(result);

        verify(authoredChallengesView).showChallengeView(result.getId());
    }

    @After
    public void tearDown() throws Exception {
        authoredChallengesPresenter.detachView();
    }
}
