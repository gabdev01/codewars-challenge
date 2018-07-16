package codewars.tuigroup.com.codewars.ui.search;

import com.tuigroup.codewars.data.UserRepositoryContract;
import com.tuigroup.codewars.data.local.model.UserEntity;
import com.tuigroup.codewars.data.remote.exception.NoConnectivityException;

import javax.inject.Inject;

import codewars.tuigroup.com.codewars.di.ActivityScoped;
import codewars.tuigroup.com.codewars.ui.base.BasePresenter;
import codewars.tuigroup.com.codewars.ui.util.rx.SchedulerProvider;
import io.reactivex.disposables.CompositeDisposable;
import retrofit2.HttpException;

@ActivityScoped
public class SearchUserPresenter extends BasePresenter<SearchUserContract.View>
        implements SearchUserContract.Presenter {

    private final static int SEARCH_USER_HISTORY_LIMIT = 5;

    private UserRepositoryContract userRepository;
    private CompositeDisposable searchCompositeDisposable;
    private UserEntity userFound;

    @Inject
    public SearchUserPresenter(UserRepositoryContract userRepository, SchedulerProvider schedulerProvider) {
        super(schedulerProvider);
        this.userRepository = userRepository;
        this.searchCompositeDisposable = new CompositeDisposable();
        this.userFound = null;
    }

    @Override
    public void subscribe() {
        super.subscribe();
        loadSearchHistory(UserRepositoryContract.UserOrderBy.DATE_ADDED);
    }

    @Override
    public void loadSearchHistory(UserRepositoryContract.UserOrderBy orderBy) {
        addDisposable(userRepository.getLastUsersSearched(orderBy, SEARCH_USER_HISTORY_LIMIT)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
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
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
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
