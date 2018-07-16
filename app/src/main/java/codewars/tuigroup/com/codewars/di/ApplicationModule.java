package codewars.tuigroup.com.codewars.di;

import android.app.Application;
import android.content.Context;

import codewars.tuigroup.com.codewars.ui.util.rx.AppSchedulerProvider;
import codewars.tuigroup.com.codewars.ui.util.rx.SchedulerProvider;
import dagger.Binds;
import dagger.Module;
import dagger.Provides;

@Module(includes = ApplicationModule.Declarations.class)
public class ApplicationModule {
    @Module
    public interface Declarations {
        @Binds
        Context bindContext(Application application);
    }

    @Provides
    SchedulerProvider provideSchedulerProvider() {
        return new AppSchedulerProvider();
    }
}

