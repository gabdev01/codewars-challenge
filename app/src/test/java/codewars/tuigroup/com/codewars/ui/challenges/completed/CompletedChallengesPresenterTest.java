package codewars.tuigroup.com.codewars.ui.challenges.completed;

import android.arch.paging.DataSource;

import com.tuigroup.codewars.data.UserRepositoryContract;
import com.tuigroup.codewars.data.local.model.CompletedChallengeEntity;

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
    private DataSource<CompletedChallengeEntity, Integer> dataSource;

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
                userRepository, testSchedulerProvider, USERNAME_TEST);
        completedChallengesPresenter.attachView(completedChallengesView);
    }

    @Test
    public void loadChallengesWithErrorFromRepositoryAndLoadIntoView() {
        doReturn(Observable.just(new Throwable()))
                .when(userRepository)
                .getCompletedChallenges(completedChallengesPresenter, USERNAME_TEST);

        completedChallengesPresenter.loadChallenges();

        testScheduler.triggerActions();

        verify(completedChallengesView).showLoadingChallengesError();
    }

    @Test
    public void openChallengeFromRepositoryAndLoadIntoView() {
        CompletedChallengeEntity result = TestUtil.createCompletedChallengeEntity(
                "id1", "username1", "cat1");

        completedChallengesPresenter.openChallenge(result);

        verify(completedChallengesView).showChallengeView(result.getId());
    }

    @After
    public void tearDown() throws Exception {
        completedChallengesPresenter.detachView();
    }
}
