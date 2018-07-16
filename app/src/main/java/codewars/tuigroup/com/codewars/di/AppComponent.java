package codewars.tuigroup.com.codewars.di;

import android.app.Application;

import com.tuigroup.codewars.data.RepositoriesModule;
import com.tuigroup.codewars.data.local.CodewarsDatabaseModule;
import com.tuigroup.codewars.data.remote.CodewarsRestModule;

import javax.inject.Singleton;

import codewars.tuigroup.com.codewars.CodewarsApplication;
import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjector;
import dagger.android.support.AndroidSupportInjectionModule;

@Singleton
@Component(modules = {CodewarsDatabaseModule.class,
        CodewarsRestModule.class,
        ApplicationModule.class,
        RepositoriesModule.class,
        ActivityBindingModule.class,
        AndroidSupportInjectionModule.class})
public interface AppComponent extends AndroidInjector<CodewarsApplication> {

    @Component.Builder
    interface Builder {

        @BindsInstance
        AppComponent.Builder application(Application application);

        AppComponent build();
    }
}
