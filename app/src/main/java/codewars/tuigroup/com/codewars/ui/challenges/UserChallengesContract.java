package codewars.tuigroup.com.codewars.ui.challenges;

import codewars.tuigroup.com.codewars.ui.base.BaseView;
import codewars.tuigroup.com.codewars.ui.base.ScopedPresenter;

public class UserChallengesContract {

    interface View extends BaseView<UserChallengesContract.Presenter> {

        void showAuthoredChallengesView();

        void showCompletedChallengesView();
    }

    interface Presenter extends ScopedPresenter<UserChallengesContract.View> {

        void openAuthoredChallengesView();

        void openCompletedChallengesView();
    }
}
