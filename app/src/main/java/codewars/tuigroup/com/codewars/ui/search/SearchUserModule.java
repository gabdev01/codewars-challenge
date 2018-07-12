package codewars.tuigroup.com.codewars.ui.search;

import codewars.tuigroup.com.codewars.di.ActivityScoped;
import dagger.Binds;
import dagger.Module;
import dagger.Provides;

@Module(includes = SearchUserModule.Declarations.class)
public class SearchUserModule {

    @Module
    public interface Declarations {
        @Binds
        @ActivityScoped
        SearchUserContract.Presenter searchUserPresenter(SearchUserPresenter presenter);
    }

    @Provides
    @ActivityScoped
    UsersSearchHistoryAdapter provideUsersHistoryAdapter(SearchUserPresenter searchUserPresenter) {
        return new UsersSearchHistoryAdapter(searchUserPresenter);
    }
}
