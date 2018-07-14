package codewars.tuigroup.com.codewars.ui.challenges;

import com.tuigroup.codewars.data.UserRepository;
import com.tuigroup.codewars.data.remote.exception.NoConnectivityException;
import com.tuigroup.codewars.data.remote.model.CompletedChallenge;

import javax.inject.Inject;

import codewars.tuigroup.com.codewars.ui.base.BasePresenter;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class ChallengesPresenter extends BasePresenter<ChallengesContract.View>
        implements ChallengesContract.Presenter {

    @Inject
    String userId;

    private UserRepository userRepository;

    @Inject
    public ChallengesPresenter(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void subscribe() {
        super.subscribe();
        loadChallenges();
    }

    private void loadChallenges() {
        addDisposable(userRepository.getCompletedChallenges(userId, 0)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        challenges -> {
                            view.showLoadingChallengesIndicator(false);
                            view.showChallenges(challenges);
                        },
                        throwable -> {
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
                ));
    }

    @Override
    public void openChallenge(CompletedChallenge challenge) {
        // TODO open challenge details
    }
}
