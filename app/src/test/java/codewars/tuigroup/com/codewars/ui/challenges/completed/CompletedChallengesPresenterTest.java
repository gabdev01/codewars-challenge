package codewars.tuigroup.com.codewars.ui.challenges.completed;

import com.tuigroup.codewars.data.UserRepositoryContract;
import com.tuigroup.codewars.data.local.model.CompletedChallengeEntity;
import com.tuigroup.codewars.data.paging.ObservableBoundaryCallback;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import codewars.tuigroup.com.codewars.util.TestSchedulerProvider;
import codewars.tuigroup.com.codewars.util.TestUtil;
import io.reactivex.Observable;
import io.reactivex.schedulers.TestScheduler;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class CompletedChallengesPresenterTest {

    private static final String USERNAME_TEST = "username_test";

    @Mock
    private UserRepositoryContract userRepository;
    @Mock
    private CompletedChallengesContract.View completedChallengesView;
    @Mock
    private ObservableBoundaryCallback<CompletedChallengeEntity, String> observableBoundaryCallback;

    private TestScheduler testScheduler;
    private CompletedChallengesPresenter completedChallengesPresenter;

    @BeforeClass
    public static void onlyOnce() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
        testScheduler = new TestScheduler();
        TestSchedulerProvider testSchedulerProvider = new TestSchedulerProvider(testScheduler);
        completedChallengesPresenter = new CompletedChallengesPresenter(
                userRepository, testSchedulerProvider, observableBoundaryCallback, USERNAME_TEST);
    }

    @Test
    public void loadChallengesWithErrorFromRepositoryAndLoadIntoView() {
        doReturn(Observable.just(new Throwable()))
                .when(userRepository)
                .getCompletedChallenges(observableBoundaryCallback, USERNAME_TEST);

        // attachView should load the challenges
        completedChallengesPresenter.attachView(completedChallengesView);

        testScheduler.triggerActions();

        verify(completedChallengesView).showLoadingChallengesError();
    }

    @Test
    public void openChallengeFromRepositoryAndLoadIntoView() {
        doReturn(Observable.just(new Throwable()))
                .when(userRepository)
                .getCompletedChallenges(observableBoundaryCallback, USERNAME_TEST);
        CompletedChallengeEntity result = TestUtil.createCompletedChallengeEntity(
                "id1", "username1", "cat1");

        completedChallengesPresenter.attachView(completedChallengesView);
        completedChallengesPresenter.openChallenge(result);

        verify(completedChallengesView).showChallengeView(result.getId());
    }

    @After
    public void tearDown() throws Exception {
        completedChallengesPresenter.detachView();
    }
}
