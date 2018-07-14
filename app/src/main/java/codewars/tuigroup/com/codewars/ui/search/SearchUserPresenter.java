package codewars.tuigroup.com.codewars.ui.search;

import com.tuigroup.codewars.data.UserRepository;
import com.tuigroup.codewars.data.local.model.UserEntity;
import com.tuigroup.codewars.data.remote.exception.NoConnectivityException;

import javax.inject.Inject;

import codewars.tuigroup.com.codewars.di.ActivityScoped;
import codewars.tuigroup.com.codewars.ui.base.BasePresenter;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.HttpException;

@ActivityScoped
public class SearchUserPresenter extends BasePresenter<SearchUserContract.View>
        implements SearchUserContract.Presenter {

    private final static int SEARCH_USER_HISTORY_LIMIT = 5;

    private UserRepository userRepository;
    private CompositeDisposable searchCompositeDisposable;
    private UserEntity userFound;

    @Inject
    public SearchUserPresenter(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.searchCompositeDisposable = new CompositeDisposable();
        this.userFound = null;
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
                            if (users.isEmpty()) {
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
                            userFound = user;
                            view.showSearchUserIndicator(false);
                            view.showSearchUserSuccess(userFound);
                        },
                        throwable -> {
                            userFound = null;
                            boolean isThrowableHandled = false;
                            if (throwable instanceof HttpException) {
                                int statusCode = ((HttpException) throwable).code();
                                if (statusCode == 404) {
                                    view.showSearchUserIndicator(false);
                                    view.showSearchUserNotFound();
                                    isThrowableHandled = true;
                                }
                            } else if (throwable instanceof NoConnectivityException) {
                                view.showSearchUserIndicator(false);
                                view.showSearchUserNoInternetError();
                                isThrowableHandled = true;
                            }

                            if (!isThrowableHandled) {
                                logError(throwable);
                                view.showSearchUserIndicator(false);
                                view.showSearchUserError();
                            }
                        }
                ));
    }

    @Override
    public void openUserFoundChallenges() {
        openUserChallenges(userFound);
    }

    @Override
    public void openUserChallenges(UserEntity user) {
        view.showUserChallengesView(user.getUsername());
    }
}
