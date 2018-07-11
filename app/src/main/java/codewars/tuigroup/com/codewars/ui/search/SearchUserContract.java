package codewars.tuigroup.com.codewars.ui.search;

import com.tuigroup.codewars.data.local.model.UserEntity;

import codewars.tuigroup.com.codewars.ui.base.BaseView;
import codewars.tuigroup.com.codewars.ui.base.ScopedPresenter;

public class SearchUserContract {

    interface View extends BaseView<SearchUserContract.Presenter> {

        void showSearchUserIndicator(boolean enabled);

        void showSearchUserError();

        void showSearchUserNoInternetError();

        void showSearchUserSuccess(UserEntity user);

    }

    interface Presenter extends ScopedPresenter<SearchUserContract.View> {

        void searchUser(String username);
    }
}
