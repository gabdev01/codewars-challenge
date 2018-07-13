package codewars.tuigroup.com.codewars.ui.search;

import com.tuigroup.codewars.data.local.model.UserSearchHistory;
import com.tuigroup.codewars.data.local.model.UserEntity;

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
    }

    interface Presenter extends ScopedPresenter<SearchUserContract.View> {

        void searchUser(String username);

        void openUserFoundDetails();

        void openUserDetails(UserEntity user);
    }
}
