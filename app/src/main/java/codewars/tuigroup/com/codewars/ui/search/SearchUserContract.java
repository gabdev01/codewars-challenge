package codewars.tuigroup.com.codewars.ui.search;

import com.tuigroup.codewars.data.UserRepositoryContract;
import com.tuigroup.codewars.data.local.model.UserEntity;
import com.tuigroup.codewars.data.local.model.UserSearchHistory;

import java.util.List;

import codewars.tuigroup.com.codewars.ui.base.BaseView;
import codewars.tuigroup.com.codewars.ui.base.ScopedPresenter;

public class SearchUserContract {

    interface View extends BaseView<SearchUserContract.Presenter> {

        void showSearchUserIndicator(boolean enabled);

        void showSearchUserNotFound();

        void showSearchUserError();

        void showSearchUserNoInternetError();

        void showSearchUserSuccess(UserEntity user);

        void showUsersSearchHistory(List<UserSearchHistory> usersSearchHistory);

        void showNoUsersSearchHistory();

        void showUsersSearchHistoryError();

        void showUserChallengesView(String username);
    }

    interface Presenter extends ScopedPresenter<SearchUserContract.View> {

        void searchUser(String username);

        void loadSearchHistory(UserRepositoryContract.UserOrderBy orderBy);

        void openUserFoundChallenges();

        void openUserChallenges(UserEntity user);
    }
}
