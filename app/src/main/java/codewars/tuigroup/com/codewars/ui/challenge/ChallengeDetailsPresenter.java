package codewars.tuigroup.com.codewars.ui.challenge;

import android.arch.lifecycle.LifecycleObserver;

import com.tuigroup.codewars.data.CodeChallengeRepository;
import com.tuigroup.codewars.data.local.model.CodeChallengeEntity;
import com.tuigroup.codewars.data.remote.exception.NoConnectivityException;

import javax.inject.Inject;

import codewars.tuigroup.com.codewars.di.ActivityScoped;
import codewars.tuigroup.com.codewars.ui.base.BasePresenter;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

@ActivityScoped
public class ChallengeDetailsPresenter extends BasePresenter<ChallengeDetailsContract.View>
        implements ChallengeDetailsContract.Presenter, LifecycleObserver {

    private CodeChallengeRepository codeChallengeRepository;
    private String challengeId;
    private CompositeDisposable challengeCompositeDisposable;
    private CodeChallengeEntity challenge;

    @Inject
    public ChallengeDetailsPresenter(CodeChallengeRepository codeChallengeRepository, String challengeId) {
        this.codeChallengeRepository = codeChallengeRepository;
        this.challengeId = challengeId;
        this.challengeCompositeDisposable = new CompositeDisposable();
        this.challenge = null;
    }

    @Override
    public void subscribe() {
        super.subscribe();

        loadChallenge();
    }

    @Override
    public void detachView() {
        super.detachView();
        challengeCompositeDisposable.clear();
    }

    @Override
    public void loadChallenge() {
        view.showLoadingChallengeIndicator(true);
        challengeCompositeDisposable.clear();
        challengeCompositeDisposable.add(codeChallengeRepository.getChallenge(challengeId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread(), true)
                .subscribe(
                        challengeResponse -> {
                            if (challengeResponse.data.isPresent()) {
                                challenge = challengeResponse.data.get();
                                view.showLoadingChallengeIndicator(false);
                                view.showChallenge(challenge);
                            }
                        },
                        throwable -> {
                            // If we already have a result no need to show an error
                            if (challenge == null) {
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
                        }
                ));
    }
}
