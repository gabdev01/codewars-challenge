package codewars.tuigroup.com.codewars.ui.challenges;

import com.tuigroup.codewars.data.remote.model.CompletedChallenge;

import java.util.List;

import codewars.tuigroup.com.codewars.ui.base.BaseView;
import codewars.tuigroup.com.codewars.ui.base.ScopedPresenter;

public class ChallengesContract {

    interface View extends BaseView<ChallengesContract.Presenter> {

        void showLoadingChallengesIndicator(boolean enabled);

        void showLoadingChallengesError();

        void showLoadingChallengesNoInternetError();

        void showChallenges(List<CompletedChallenge> challenges);

        void showChallengeView(String challengeId);
    }

    interface Presenter extends ScopedPresenter<ChallengesContract.View> {

        void openChallenge(CompletedChallenge challenge);
    }
}
