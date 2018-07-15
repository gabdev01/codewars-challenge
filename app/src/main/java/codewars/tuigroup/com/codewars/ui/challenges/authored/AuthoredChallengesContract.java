package codewars.tuigroup.com.codewars.ui.challenges.authored;

import com.tuigroup.codewars.data.local.model.AuthoredChallengeEntity;

import java.util.List;

import codewars.tuigroup.com.codewars.ui.base.BaseView;
import codewars.tuigroup.com.codewars.ui.base.ScopedPresenter;

public class AuthoredChallengesContract {

    public interface View extends BaseView<AuthoredChallengesContract.Presenter> {

        void showLoadingChallengesIndicator(boolean enabled);

        void showLoadingChallengesError();

        void showLoadingChallengesNoInternetError();

        void showChallenges(List<AuthoredChallengeEntity> challenges);

        void showNoChallenges();

        void showChallengeView(String challengeId);
    }

    public interface Presenter extends ScopedPresenter<AuthoredChallengesContract.View> {

        void loadChallenges();

        void openChallenge(AuthoredChallengeEntity challenge);
    }
}
