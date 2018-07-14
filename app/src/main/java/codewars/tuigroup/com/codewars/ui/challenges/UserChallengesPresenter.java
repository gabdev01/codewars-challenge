package codewars.tuigroup.com.codewars.ui.challenges;

import javax.inject.Inject;

import codewars.tuigroup.com.codewars.ui.base.BasePresenter;

public class UserChallengesPresenter extends BasePresenter<UserChallengesContract.View>
        implements UserChallengesContract.Presenter {

    @Inject
    public UserChallengesPresenter() {
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
