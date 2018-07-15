package codewars.tuigroup.com.codewars.ui.challenges.authored;

import com.tuigroup.codewars.data.UserRepository;
import com.tuigroup.codewars.data.local.model.AuthoredChallengeEntity;
import com.tuigroup.codewars.data.remote.exception.NoConnectivityException;
import com.tuigroup.codewars.data.util.Status;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import codewars.tuigroup.com.codewars.ui.base.BasePresenter;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class AuthoredChallengesPresenter extends BasePresenter<AuthoredChallengesContract.View>
        implements AuthoredChallengesContract.Presenter {

    private UserRepository userRepository;
    private String userId;
    private List<AuthoredChallengeEntity> authoredChallenge;

    @Inject
    public AuthoredChallengesPresenter(UserRepository userRepository, String userId) {
        this.userRepository = userRepository;
        this.userId = userId;
        this.authoredChallenge = null;
    }

    @Override
    public void subscribe() {
        super.subscribe();
        loadChallenges();
    }

    @Override
    public void loadChallenges() {
        view.showLoadingChallengesIndicator(true);
        addLifecycleDisposable(userRepository.getAuthoredChallenges(userId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread(), true)
                .subscribe(
                        authoredChallenges -> {
                            if (authoredChallenges.status == Status.LOCAL
                                    && authoredChallenges.data.isEmpty()) {
                                // Maybe first load so we let the loading indicator and wait the remote data
                            } else if (authoredChallenges.status == Status.REMOTE
                                    && authoredChallenges.data.isEmpty()) {
                                authoredChallenge = new ArrayList<>();
                                view.showLoadingChallengesIndicator(false);
                                view.showNoChallenges();
                            } else {
                                authoredChallenge = authoredChallenges.data;
                                view.showLoadingChallengesIndicator(false);
                                view.showChallenges(authoredChallenge);
                            }
                        },
                        throwable -> {
                            // If we already have a result no need to show an error
                            if (authoredChallenge == null) {
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
