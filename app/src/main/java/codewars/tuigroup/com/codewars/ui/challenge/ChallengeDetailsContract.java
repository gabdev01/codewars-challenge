package codewars.tuigroup.com.codewars.ui.challenge;

import com.tuigroup.codewars.data.local.model.CodeChallengeEntity;

import codewars.tuigroup.com.codewars.ui.base.BaseState;
import codewars.tuigroup.com.codewars.ui.base.BaseView;
import codewars.tuigroup.com.codewars.ui.base.ScopedPresenter;

public class ChallengeDetailsContract {

    public interface View extends BaseView<Presenter> {

        void showLoadingChallengeIndicator(boolean enabled);

        void showChallenge(CodeChallengeEntity challenge);

        void showLoadingChallengeError();

        void showLoadingChallengeNoInternetError();

        void showChallengeUrl(String url);
    }

    public interface Presenter extends ScopedPresenter<View, State> {

        void loadChallenge();

        void openChallengeUrl();
    }

    interface State extends BaseState {

    }
}
