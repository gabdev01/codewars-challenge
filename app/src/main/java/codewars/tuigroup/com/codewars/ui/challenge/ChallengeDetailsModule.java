package codewars.tuigroup.com.codewars.ui.challenge;

import codewars.tuigroup.com.codewars.di.ActivityScoped;
import dagger.Binds;
import dagger.Module;
import dagger.Provides;

import static codewars.tuigroup.com.codewars.ui.challenge.ChallengeDetailsActivity.EXTRA_CHALLENGE_ID;

@Module
public abstract class ChallengeDetailsModule {
    @Binds
    @ActivityScoped
    abstract ChallengeDetailsContract.Presenter challengeDetailsPresenter(ChallengeDetailsPresenter presenter);

    @Provides
    @ActivityScoped
    static String provideChallengeId(ChallengeDetailsActivity activity) {
        return activity.getIntent().getStringExtra(EXTRA_CHALLENGE_ID);
    }
}
