package codewars.tuigroup.com.codewars.ui.search;

import com.tuigroup.codewars.data.UserRepositoryContract;
import com.tuigroup.codewars.data.local.model.UserEntity;
import com.tuigroup.codewars.data.remote.exception.NoConnectivityException;
import com.tuigroup.codewars.data.util.UserOrderBy;

import javax.inject.Inject;

import codewars.tuigroup.com.codewars.di.ActivityScoped;
import codewars.tuigroup.com.codewars.ui.base.BasePresenter;
import codewars.tuigroup.com.codewars.ui.util.RequestResultType;
import codewars.tuigroup.com.codewars.ui.util.rx.SchedulerProvider;
import io.reactivex.disposables.CompositeDisposable;
import retrofit2.HttpException;

@ActivityScoped
public class SearchUserPresenter
        extends BasePresenter<SearchUserContract.View, SearchUserContract.State>
        implements SearchUserContract.Presenter {

    private final static int SEARCH_USER_HISTORY_LIMIT = 5;

    private UserRepositoryContract userRepository;
    private CompositeDisposable searchCompositeDisposable;
    private UserOrderBy usersSearchedOrderBy;
    private RequestResultType searchUserRequestResultType;
    private UserEntity userFound;

    @Inject
    public SearchUserPresenter(UserRepositoryContract userRepository, SchedulerProvider schedulerProvider) {
        super(schedulerProvider);
        this.userRepository = userRepository;
        this.searchCompositeDisposable = new CompositeDisposable();
        this.userFound = null;
        this.usersSearchedOrderBy = UserOrderBy.DATE_ADDED;
        this.searchUserRequestResultType = RequestResultType.UNDEFINED;
    }

    @Override
    public void attachView(SearchUserContract.View view, SearchUserContract.State state) {
        super.attachView(view, state);
        if (state != null && state.getUsersSearchedOrderBy() != null) {
            usersSearchedOrderBy = state.getUsersSearchedOrderBy();
        }
        if (state != null && state.getUserFound() != null) {
            userFound = state.getUserFound();
        }
        if (state != null && state.getSearchUserResultType() != null) {
            searchUserRequestResultType = state.getSearchUserResultType();
        }
    }

    @Override
    public SearchUserContract.State getState() {
        return new SearchUserState(usersSearchedOrderBy, userFound, searchUserRequestResultType);
    }

    @Override
    public void subscribe() {
        super.subscribe();
        loadSearchHistory(usersSearchedOrderBy);
        // Load state of the previous search user request
        if (searchUserRequestResultType == RequestResultType.SUCCESS_RESULT) {
            if (userFound != null) {
                view.showSearchUserSuccess(userFound);
            }
        } else if (searchUserRequestResultType == RequestResultType.SUCCESS_EMPTY) {
            view.showSearchUserNotFound();
        } else if (searchUserRequestResultType == RequestResultType.ERROR) {
            view.showSearchUserError();
        }
    }

    @Override
    public void loadSearchHistory(UserOrderBy orderBy) {
        usersSearchedOrderBy = orderBy;
        addDisposable(userRepository.getLastUsersSearched(usersSearchedOrderBy, SEARCH_USER_HISTORY_LIMIT)
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
                            searchUserRequestResultType = RequestResultType.SUCCESS_RESULT;
                            view.showSearchUserIndicator(false);
                            view.showSearchUserSuccess(userFound);
                        },
                        throwable -> {
                            userFound = null;
                            boolean isThrowableHandled = false;
                            if (throwable instanceof HttpException) {
                                int statusCode = ((HttpException) throwable).code();
                                if (statusCode == 404) {
                                    searchUserRequestResultType = RequestResultType.SUCCESS_EMPTY;
                                    view.showSearchUserIndicator(false);
                                    view.showSearchUserNotFound();
                                    isThrowableHandled = true;
                                }
                            } else if (throwable instanceof NoConnectivityException) {
                                searchUserRequestResultType = RequestResultType.ERROR;
                                view.showSearchUserIndicator(false);
                                view.showSearchUserNoInternetError();
                                isThrowableHandled = true;
                            }

                            if (!isThrowableHandled) {
                                logError(throwable);
                                searchUserRequestResultType = RequestResultType.ERROR;
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
