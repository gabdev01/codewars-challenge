package codewars.tuigroup.com.codewars.ui.challenges.completed;

import android.util.Log;

import com.tuigroup.codewars.data.CompletedChallengePageBoundaryCallback;
import com.tuigroup.codewars.data.UserRepository;
import com.tuigroup.codewars.data.local.model.CompletedChallengeEntity;
import com.tuigroup.codewars.data.remote.exception.NoConnectivityException;

import java.util.List;

import javax.inject.Inject;

import codewars.tuigroup.com.codewars.ui.base.BasePresenter;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class CompletedChallengesPresenter extends BasePresenter<CompletedChallengesContract.View>
        implements CompletedChallengesContract.Presenter, CompletedChallengePageBoundaryCallback.PageBoundaryCallback {

    private UserRepository userRepository;
    private String userId;
    private List<CompletedChallengeEntity> completedChallenges;
    private CompletedChallengePageBoundaryCallback callback;

    @Inject
    public CompletedChallengesPresenter(UserRepository userRepository,
                                        CompletedChallengePageBoundaryCallback callback,
                                        String userId) {
        this.userRepository = userRepository;
        this.callback = callback;
        this.userId = userId;
        this.completedChallenges = null;
        this.callback.setPageBoundaryListener(this);
    }

    @Override
    public void subscribe() {
        super.subscribe();
        loadChallenges();
    }

    @Override
    public void loadChallenges() {
        view.showLoadingChallengesIndicator(true);
        addLifecycleDisposable(userRepository.getCompletedChallenges(callback, userId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
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
                            if (completedChallenges.isEmpty()) {
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
