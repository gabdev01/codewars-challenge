package codewars.tuigroup.com.codewars.ui.challenges;

import codewars.tuigroup.com.codewars.di.ActivityScoped;
import codewars.tuigroup.com.codewars.di.FragmentScoped;
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
        UserChallengesContract.Presenter userChallengesPresenter(UserChallengesPresenter presenter);

        @ContributesAndroidInjector
        @FragmentScoped
        ChallengesFragment challengesFragment();
    }

    @Provides
    @ActivityScoped
    static String provideUserId(UserChallengesActivity activity) {
        return activity.getIntent().getStringExtra(EXTRA_USER_ID);
    }


    @Provides
    @ActivityScoped
    static ChallengesAdapter provideChallengesAdapter() {
        return new ChallengesAdapter();
    }
}
