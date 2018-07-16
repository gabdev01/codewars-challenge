package codewars.tuigroup.com.codewars.ui.challenges.completed;

import android.arch.paging.PagedList;

import com.tuigroup.codewars.data.local.model.CompletedChallengeEntity;

import codewars.tuigroup.com.codewars.ui.base.BaseState;
import codewars.tuigroup.com.codewars.ui.base.BaseView;
import codewars.tuigroup.com.codewars.ui.base.ScopedPresenter;

public class CompletedChallengesContract {

    public interface View extends BaseView<Presenter> {

        void showLoadingChallengesIndicator(boolean enabled);

        void showLoadingChallengesError();

        void showLoadingChallengesNoInternetError();

        void showChallenges(PagedList<CompletedChallengeEntity> challenges);

        void showNoChallenges();

        void showChallengeView(String challengeId);

        void showAllDataLoaded();
    }

    public interface Presenter extends ScopedPresenter<View, State> {

        void loadChallenges();

        void openChallenge(CompletedChallengeEntity challenge);
    }

    interface State extends BaseState {

    }
}
