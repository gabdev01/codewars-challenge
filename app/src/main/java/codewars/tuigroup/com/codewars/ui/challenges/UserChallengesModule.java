package codewars.tuigroup.com.codewars.ui.challenges;

import codewars.tuigroup.com.codewars.di.ActivityScoped;
import codewars.tuigroup.com.codewars.di.FragmentScoped;
import codewars.tuigroup.com.codewars.ui.challenges.authored.AuthoredChallengesAdapter;
import codewars.tuigroup.com.codewars.ui.challenges.authored.AuthoredChallengesContract;
import codewars.tuigroup.com.codewars.ui.challenges.authored.AuthoredChallengesFragment;
import codewars.tuigroup.com.codewars.ui.challenges.authored.AuthoredChallengesPresenter;
import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import dagger.android.ContributesAndroidInjector;

import static codewars.tuigroup.com.codewars.ui.challenges.UserChallengesActivity.EXTRA_USER_ID;

@Module(includes = UserChallengesModule.Declarations.class)
public class UserChallengesModule {

    @Module
    public interface Declarations {

        @Binds
        @ActivityScoped
        ChallengesContract.Presenter challengesPresenter(ChallengesPresenter presenter);

        @Binds
        @ActivityScoped
        AuthoredChallengesContract.Presenter authoredChallengesPresenter(AuthoredChallengesPresenter presenter);

        @Binds
        @ActivityScoped
        UserChallengesContract.Presenter userChallengesPresenter(UserChallengesPresenter presenter);

        @ContributesAndroidInjector
        @FragmentScoped
        ChallengesFragment challengesFragment();

        @ContributesAndroidInjector
        @FragmentScoped
        AuthoredChallengesFragment authoredChallengesFragment();
    }

    @Provides
    @ActivityScoped
    static String provideUserId(UserChallengesActivity activity) {
        return activity.getIntent().getStringExtra(EXTRA_USER_ID);
    }

    @Provides
    @ActivityScoped
    static ChallengesAdapter provideChallengesTempAdapter() {
        return new ChallengesAdapter();
    }

    @ActivityScoped
    static AuthoredChallengesAdapter provideAuthoredChallengesAdapter() {
        return new AuthoredChallengesAdapter();
    }
}
