package codewars.tuigroup.com.codewars.ui.challenges;

import codewars.tuigroup.com.codewars.ui.base.BaseView;
import codewars.tuigroup.com.codewars.ui.base.ScopedPresenter;

public class UserChallengesContract {

    interface View extends BaseView<UserChallengesContract.Presenter> {

        void showCompletedChallengesView();

        void showAuthoredChallengesView();
    }

    interface Presenter extends ScopedPresenter<UserChallengesContract.View> {

        void openCompletedChallengesView();

        void openAuthoredChallengesView();
    }
}
