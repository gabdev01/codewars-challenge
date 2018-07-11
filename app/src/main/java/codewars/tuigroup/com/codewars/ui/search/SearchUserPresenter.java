package codewars.tuigroup.com.codewars.ui.search;

import com.tuigroup.codewars.data.UsersRepository;

import codewars.tuigroup.com.codewars.ui.base.BasePresenter;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class SearchUserPresenter extends BasePresenter<SearchUserContract.View>
        implements SearchUserContract.Presenter {

    private UsersRepository usersRepository;

    public SearchUserPresenter(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    @Override
    public void subscribe() {
        super.subscribe();
        // TODO Use lifecycle from Arch library

        loadSearchHistory();
    }

    private void loadSearchHistory() {
        // TODO load search history
    }

    @Override
    public void searchUser(String username) {
        view.showSearchUserIndicator(true);
        addDisposable(usersRepository.getUser(username)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        user -> {
                            view.showSearchUserIndicator(false);
                            view.showSearchUserSuccess(user.data);
                        },
                        throwable -> {
                            logError(throwable);
                            view.showSearchUserIndicator(false);
                            view.showSearchUserError();
                            // TODO Handle different type of errors
                        }
                ));
    }
}
