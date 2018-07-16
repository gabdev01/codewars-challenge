package codewars.tuigroup.com.codewars.ui.challenge;

import com.tuigroup.codewars.data.local.model.CodeChallengeEntity;

import codewars.tuigroup.com.codewars.ui.base.BaseView;
import codewars.tuigroup.com.codewars.ui.base.ScopedPresenter;

public class ChallengeDetailsContract {

    public interface View extends BaseView<ChallengeDetailsContract.Presenter> {

        void showLoadingChallengeIndicator(boolean enabled);

        void showChallenge(CodeChallengeEntity challenge);

        void showLoadingChallengeError();

        void showLoadingChallengeNoInternetError();
    }

    public interface Presenter extends ScopedPresenter<ChallengeDetailsContract.View> {

        void loadChallenge();
    }
}
