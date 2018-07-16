package codewars.tuigroup.com.codewars.ui.search;

import com.tuigroup.codewars.data.local.model.UserEntity;
import com.tuigroup.codewars.data.local.model.UserSearchHistory;
import com.tuigroup.codewars.data.util.UserOrderBy;

import java.util.List;

import codewars.tuigroup.com.codewars.ui.base.BaseState;
import codewars.tuigroup.com.codewars.ui.base.BaseView;
import codewars.tuigroup.com.codewars.ui.base.ScopedPresenter;
import codewars.tuigroup.com.codewars.ui.util.RequestResultType;

public class SearchUserContract {

    interface View extends BaseView<Presenter> {

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

    interface Presenter extends ScopedPresenter<View, State> {

        void searchUser(String username);

        void loadSearchHistory(UserOrderBy orderBy);

        void openUserFoundChallenges();

        void openUserChallenges(UserEntity user);
    }

    interface State extends BaseState {

        UserOrderBy getUsersSearchedOrderBy();

        UserEntity getUserFound();

        RequestResultType getSearchUserResultType();
    }
}
