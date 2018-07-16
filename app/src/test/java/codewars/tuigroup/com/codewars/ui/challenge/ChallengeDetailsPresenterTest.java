package codewars.tuigroup.com.codewars.ui.challenge;

import com.google.common.base.Optional;
import com.tuigroup.codewars.data.CodeChallengeRepositoryContract;
import com.tuigroup.codewars.data.local.model.CodeChallengeEntity;
import com.tuigroup.codewars.data.util.Resource;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import codewars.tuigroup.com.codewars.util.TestSchedulerProvider;
import codewars.tuigroup.com.codewars.util.TestUtil;
import io.reactivex.Flowable;
import io.reactivex.schedulers.TestScheduler;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class ChallengeDetailsPresenterTest {

    private static final String CHALLENGE_ID_TEST = "challenge_id_test";

    @Mock
    private CodeChallengeRepositoryContract codeChallengeRepository;
    @Mock
    private ChallengeDetailsContract.View authoredChallengesView;

    private TestScheduler testScheduler;
    private ChallengeDetailsPresenter challengesDetailsPresenter;

    @BeforeClass
    public static void onlyOnce() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
        testScheduler = new TestScheduler();
        TestSchedulerProvider testSchedulerProvider = new TestSchedulerProvider(testScheduler);
        challengesDetailsPresenter = new ChallengeDetailsPresenter(
                codeChallengeRepository, testSchedulerProvider, CHALLENGE_ID_TEST);
        challengesDetailsPresenter.attachView(authoredChallengesView);
    }

    @Test
    public void loadChallengeDetailsFromRepositoryAndLoadIntoView() {
        Resource<Optional<CodeChallengeEntity>> result =
                Resource.local(Optional.of(TestUtil.createCodeChallengeEntity(CHALLENGE_ID_TEST, "cat")));
        doReturn(Flowable.just(result))
                .when(codeChallengeRepository)
                .getChallenge(CHALLENGE_ID_TEST);

        challengesDetailsPresenter.loadChallenge();

        testScheduler.triggerActions();

        verify(authoredChallengesView).showLoadingChallengeIndicator(true);
        verify(authoredChallengesView).showLoadingChallengeIndicator(false);
        verify(authoredChallengesView).showChallenge(result.data.get());
    }

    @Test
    public void loadChallengeDetailsFromRepositoryWithErrorAndLoadIntoView() {
        doReturn(Flowable.just(new Throwable()))
                .when(codeChallengeRepository)
                .getChallenge(CHALLENGE_ID_TEST);

        challengesDetailsPresenter.loadChallenge();

        testScheduler.triggerActions();

        verify(authoredChallengesView).showLoadingChallengeIndicator(true);
        verify(authoredChallengesView).showLoadingChallengeIndicator(false);
        verify(authoredChallengesView).showLoadingChallengeError();
    }

    @After
    public void tearDown() throws Exception {
        challengesDetailsPresenter.detachView();
    }
}
