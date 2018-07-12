package codewars.tuigroup.com.codewars.ui.search;

import codewars.tuigroup.com.codewars.di.ActivityScoped;
import dagger.Binds;
import dagger.Module;

@Module
public abstract class SearchUserModule {

    @ActivityScoped
    @Binds
    abstract SearchUserContract.Presenter searchUserPresenter(SearchUserPresenter presenter);
}
