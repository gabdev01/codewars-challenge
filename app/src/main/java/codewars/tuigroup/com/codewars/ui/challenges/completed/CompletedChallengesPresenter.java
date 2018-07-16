package codewars.tuigroup.com.codewars.ui.challenges.completed;

import com.tuigroup.codewars.data.UserRepositoryContract;
import com.tuigroup.codewars.data.local.model.CompletedChallengeEntity;
import com.tuigroup.codewars.data.paging.ObservableBoundaryCallback;
import com.tuigroup.codewars.data.remote.exception.NoConnectivityException;

import java.util.List;

import javax.inject.Inject;

import codewars.tuigroup.com.codewars.ui.base.BasePresenter;
import codewars.tuigroup.com.codewars.ui.util.rx.SchedulerProvider;
import io.reactivex.disposables.CompositeDisposable;

public class CompletedChallengesPresenter extends BasePresenter<CompletedChallengesContract.View>
        implements CompletedChallengesContract.Presenter, ObservableBoundaryCallback.BoundaryCallbackRequestListener {

    private UserRepositoryContract userRepository;
    private String userId;
    private List<CompletedChallengeEntity> completedChallenges;
    private CompositeDisposable challengesCompositeDisposable;
    private boolean isRequestInRunning;

    @Inject
    public CompletedChallengesPresenter(UserRepositoryContract userRepository,
                                        SchedulerProvider schedulerProvider,
                                        String userId) {
        super(schedulerProvider);
        this.userRepository = userRepository;
        this.userId = userId;
        this.completedChallenges = null;
        this.challengesCompositeDisposable = new CompositeDisposable();
        this.isRequestInRunning = false;
    }

    @Override
    public void subscribe() {
        super.subscribe();
        loadChallenges();
    }

    @Override
    public void detachView() {
        super.detachView();
        challengesCompositeDisposable.clear();
    }

    @Override
    public void loadChallenges() {
        challengesCompositeDisposable.clear();
        challengesCompositeDisposable.add(userRepository.getCompletedChallenges(this, userId)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui(), true)
                .subscribe(
                        challenges -> {
                            completedChallenges = challenges.snapshot();
                            if (challenges.isEmpty()) {
                                if(!isRequestInRunning) {
                                    view.showNoChallenges();
                                }
                            } else {
                                view.showChallenges(challenges);
                            }
                        },
                        throwable -> {
                            processError(throwable);
                        }
                ));
    }

    private void processError(Throwable throwable) {
        if (completedChallenges == null || completedChallenges.isEmpty()) {
            logError(throwable);
            boolean isThrowableHandled = false;
            if (throwable instanceof NoConnectivityException) {
                view.showLoadingChallengesNoInternetError();
                isThrowableHandled = true;
            }
            if (!isThrowableHandled) {
                logError(throwable);
                view.showLoadingChallengesError();
            }
        }
    }

    @Override
    public void onRequestInProgress(boolean inProgress) {
        isRequestInRunning = inProgress;
        view.showLoadingChallengesIndicator(inProgress);
    }

    @Override
    public void onRequestError(Throwable throwable) {
        processError(throwable);
    }

    @Override
    public void onAllDataLoaded() {
        view.showAllDataLoaded();
    }

    @Override
    public void openChallenge(CompletedChallengeEntity challenge) {
        view.showChallengeView(challenge.getId());
    }
}
