package codewars.tuigroup.com.codewars.di;

import codewars.tuigroup.com.codewars.ui.challenge.ChallengeDetailsActivity;
import codewars.tuigroup.com.codewars.ui.challenge.ChallengeDetailsModule;
import codewars.tuigroup.com.codewars.ui.challenges.UserChallengesActivity;
import codewars.tuigroup.com.codewars.ui.challenges.UserChallengesModule;
import codewars.tuigroup.com.codewars.ui.search.SearchUserActivity;
import codewars.tuigroup.com.codewars.ui.search.SearchUserModule;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ActivityBindingModule {
    @ActivityScoped
    @ContributesAndroidInjector(modules = SearchUserModule.class)
    abstract SearchUserActivity searchUserActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = UserChallengesModule.class)
    abstract UserChallengesActivity userChallengesActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = ChallengeDetailsModule.class)
    abstract ChallengeDetailsActivity challengeDetailsActivity();
}