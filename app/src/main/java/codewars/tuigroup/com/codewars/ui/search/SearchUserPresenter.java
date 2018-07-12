package codewars.tuigroup.com.codewars.ui.search;

import com.tuigroup.codewars.data.UserRepository;
import com.tuigroup.codewars.data.local.model.UserEntity;

import javax.inject.Inject;

import codewars.tuigroup.com.codewars.di.ActivityScoped;
import codewars.tuigroup.com.codewars.ui.base.BasePresenter;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

@ActivityScoped
public class SearchUserPresenter extends BasePresenter<SearchUserContract.View>
        implements SearchUserContract.Presenter {

    private final static int SEARCH_USER_HISTORY_LIMIT = 5;

    private UserRepository userRepository;

    private CompositeDisposable searchCompositeDisposable;

    @Inject
    public SearchUserPresenter(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.searchCompositeDisposable = new CompositeDisposable();
    }

    @Override
    public void subscribe() {
        super.subscribe();
        // TODO Use lifecycle from Arch library

        loadSearchHistory();
    }

    private void loadSearchHistory() {
        addDisposable(userRepository.getLastUsersSearched(SEARCH_USER_HISTORY_LIMIT)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        users -> {
                            if(users.isEmpty()) {
                                view.showNoUsersSearchHistory();
                            } else {
                                view.showUsersSearchHistory(users);
                            }
                        },
                        throwable -> {
                            logError(throwable);
                            view.showUsersSearchHistoryError();
                        }
                ));
    }

    @Override
    public void searchUser(String username) {
        view.showSearchUserIndicator(true);
        searchCompositeDisposable.clear();
        searchCompositeDisposable.add(userRepository.getUser(username)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        user -> {
                            view.showSearchUserIndicator(false);
                            view.showSearchUserSuccess(user);
                        },
                        throwable -> {
                            logError(throwable);
                            view.showSearchUserIndicator(false);
                            view.showSearchUserError();
                            // TODO Handle different type of errors
                        }
                ));
    }

    @Override
    public void openUserDetails(UserEntity user) {
        // TODO Open and show the chalenges of the user
    }
}
