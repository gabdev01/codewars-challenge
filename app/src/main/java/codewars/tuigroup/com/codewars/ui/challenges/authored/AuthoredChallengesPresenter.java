package codewars.tuigroup.com.codewars.ui.challenges.authored;

import com.tuigroup.codewars.data.UserRepositoryContract;
import com.tuigroup.codewars.data.local.model.AuthoredChallengeEntity;
import com.tuigroup.codewars.data.remote.exception.NoConnectivityException;
import com.tuigroup.codewars.data.util.Status;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import codewars.tuigroup.com.codewars.ui.base.BasePresenter;
import codewars.tuigroup.com.codewars.ui.util.rx.SchedulerProvider;
import io.reactivex.disposables.CompositeDisposable;

public class AuthoredChallengesPresenter extends BasePresenter<AuthoredChallengesContract.View>
        implements AuthoredChallengesContract.Presenter {

    private UserRepositoryContract userRepository;
    private String userId;
    private List<AuthoredChallengeEntity> authoredChallenges;
    private CompositeDisposable challengesCompositeDisposable;

    @Inject
    public AuthoredChallengesPresenter(UserRepositoryContract userRepository, SchedulerProvider schedulerProvider, String userId) {
        super(schedulerProvider);
        this.userRepository = userRepository;
        this.userId = userId;
        this.authoredChallenges = null;
        this.challengesCompositeDisposable = new CompositeDisposable();
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
        view.showLoadingChallengesIndicator(true);
        challengesCompositeDisposable.clear();
        challengesCompositeDisposable.add(userRepository.getAuthoredChallenges(userId)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui(), true)
                .subscribe(
                        authoredChallenges -> {
                            if (authoredChallenges.status == Status.LOCAL
                                    && authoredChallenges.data.isEmpty()) {
                                // Maybe first load so we let the loading indicator and wait the remote data
                            } else if (authoredChallenges.status == Status.REMOTE
                                    && authoredChallenges.data.isEmpty()) {
                                this.authoredChallenges = new ArrayList<>();
                                view.showLoadingChallengesIndicator(false);
                                view.showNoChallenges();
                            } else {
                                this.authoredChallenges = authoredChallenges.data;
                                view.showLoadingChallengesIndicator(false);
                                view.showChallenges(this.authoredChallenges);
                            }
                        },
                        throwable -> {
                            // If we already have a result no need to show an error
                            if (authoredChallenges == null) {
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
    public void openChallenge(AuthoredChallengeEntity authoredChallenge) {
        view.showChallengeView(authoredChallenge.getId());
    }
}
