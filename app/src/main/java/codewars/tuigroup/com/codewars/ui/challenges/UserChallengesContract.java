package codewars.tuigroup.com.codewars.ui.challenges;

import codewars.tuigroup.com.codewars.ui.base.BaseState;
import codewars.tuigroup.com.codewars.ui.base.BaseView;
import codewars.tuigroup.com.codewars.ui.base.ScopedPresenter;

public class UserChallengesContract {

    interface View extends BaseView<Presenter> {

        void showCompletedChallengesView();

        void showAuthoredChallengesView();
    }

    interface Presenter extends ScopedPresenter<View, State> {

        void openCompletedChallengesView();

        void openAuthoredChallengesView();
    }

    interface State extends BaseState {
        ChallengesViewType getChallengesViewType();
    }
}