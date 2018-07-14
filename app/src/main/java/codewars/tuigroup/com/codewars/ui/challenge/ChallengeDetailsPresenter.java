package codewars.tuigroup.com.codewars.ui.challenge;

import com.tuigroup.codewars.data.CodeChallengeRepository;
import com.tuigroup.codewars.data.remote.exception.NoConnectivityException;

import javax.inject.Inject;

import codewars.tuigroup.com.codewars.di.ActivityScoped;
import codewars.tuigroup.com.codewars.ui.base.BasePresenter;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

@ActivityScoped
public class ChallengeDetailsPresenter extends BasePresenter<ChallengeDetailsContract.View>
        implements ChallengeDetailsContract.Presenter {

    private CodeChallengeRepository codeChallengeRepository;
    private String challengeId;

    @Inject
    public ChallengeDetailsPresenter(CodeChallengeRepository codeChallengeRepository, String challengeId) {
        this.codeChallengeRepository = codeChallengeRepository;
        this.challengeId = challengeId;
    }

    @Override
    public void subscribe() {
        super.subscribe();

        loadChallenge();
    }

    @Override
    public void loadChallenge() {
        view.showLoadingChallengeIndicator(true);
        addLifecycleDisposable(codeChallengeRepository.getChallenge(challengeId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        challenge -> {
                            view.showLoadingChallengeIndicator(false);
                            view.showChallenge(challenge);
                        },
                        throwable -> {
                            logError(throwable);
                            boolean isThrowableHandled = false;
                            if (throwable instanceof NoConnectivityException) {
                                view.showLoadingChallengeIndicator(false);
                                view.showLoadingChallengeNoInternetError();
                                isThrowableHandled = true;
                            }
                            if (!isThrowableHandled) {
                                logError(throwable);
                                view.showLoadingChallengeIndicator(false);
                                view.showLoadingChallengeError();
                            }
                        }
                ));
    }
}
