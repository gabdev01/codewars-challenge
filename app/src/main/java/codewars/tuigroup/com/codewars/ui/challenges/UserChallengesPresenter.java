package codewars.tuigroup.com.codewars.ui.challenges;

import javax.inject.Inject;

import codewars.tuigroup.com.codewars.ui.base.BasePresenter;
import codewars.tuigroup.com.codewars.ui.util.rx.SchedulerProvider;

public class UserChallengesPresenter extends BasePresenter<UserChallengesContract.View>
        implements UserChallengesContract.Presenter {

    @Inject
    public UserChallengesPresenter(SchedulerProvider schedulerProvider) {
        super(schedulerProvider);
    }

    @Override
    public void openAuthoredChallengesView() {
        view.showAuthoredChallengesView();
    }

    @Override
    public void openCompletedChallengesView() {
        view.showCompletedChallengesView();
    }
}
