package codewars.tuigroup.com.codewars.ui.challenges.completed;

import android.util.Log;

import com.tuigroup.codewars.data.UserRepositoryContract;
import com.tuigroup.codewars.data.local.model.CompletedChallengeEntity;
import com.tuigroup.codewars.data.paging.ObservableBoundaryCallback;
import com.tuigroup.codewars.data.remote.exception.NoConnectivityException;

import java.util.List;

import javax.inject.Inject;

import codewars.tuigroup.com.codewars.ui.base.BasePresenter;
import codewars.tuigroup.com.codewars.ui.util.rx.SchedulerProvider;

public class CompletedChallengesPresenter extends BasePresenter<CompletedChallengesContract.View>
        implements CompletedChallengesContract.Presenter, ObservableBoundaryCallback.BoundaryCallbackRequestListener {

    private UserRepositoryContract userRepository;
    private String userId;
    private List<CompletedChallengeEntity> completedChallenges;

    @Inject
    public CompletedChallengesPresenter(UserRepositoryContract userRepository,
                                        SchedulerProvider schedulerProvider,
                                        String userId) {
        super(schedulerProvider);
        this.userRepository = userRepository;
        this.userId = userId;
        this.completedChallenges = null;
    }

    @Override
    public void subscribe() {
        super.subscribe();
        loadChallenges();
    }

    @Override
    public void loadChallenges() {
        view.showLoadingChallengesIndicator(true);
        addLifecycleDisposable(userRepository.getCompletedChallenges(this, userId)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe(
                        challenges -> {
                            completedChallenges = challenges.snapshot();
                            view.showLoadingChallengesIndicator(false);
                            if (challenges.isEmpty()) {
                                view.showNoChallenges();
                            } else {
                                view.showChallenges(challenges);
                            }
                        },
                        throwable -> {
                            if (completedChallenges == null || completedChallenges.isEmpty()) {
                                logError(throwable);
                                boolean isThrowableHandled = false;
                                if (throwable instanceof NoConnectivityException) {
                                    view.showLoadingChallengesIndicator(false);
                                    view.showLoadingChallengesNoInternetError();
                                    isThrowableHandled = true;
                                }
                                if (!isThrowableHandled) {
                                    logError(throwable);
                                    view.showLoadingChallengesIndicator(false);
                                    view.showLoadingChallengesError();
                                }
                            }
                        }
                ));
    }

    @Override
    public void onRequestInProgress(boolean inProgress) {
        Log.e("", "onRequestInProgress");
    }

    @Override
    public void onAllDataLoaded() {
        Log.e("", "onAllDataLoaded");
    }

    @Override
    public void openChallenge(CompletedChallengeEntity challenge) {
        view.showChallengeView(challenge.getId());
    }
}
