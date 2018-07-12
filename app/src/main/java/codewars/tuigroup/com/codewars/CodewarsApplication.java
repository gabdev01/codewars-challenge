package codewars.tuigroup.com.codewars;

import android.content.Context;
import android.util.Log;

import com.squareup.leakcanary.LeakCanary;

import butterknife.ButterKnife;
import codewars.tuigroup.com.codewars.di.DaggerAppComponent;
import dagger.android.AndroidInjector;
import dagger.android.DaggerApplication;
import io.reactivex.plugins.RxJavaPlugins;

public class CodewarsApplication extends DaggerApplication {

    public static CodewarsApplication from(final Context context) {
        return (CodewarsApplication) context.getApplicationContext();
    }

    @Override
    public void onCreate() {
        super.onCreate();

        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(this);

        initButterKnife();
        initRxJava();
        initStrictMode();
    }

    @Override
    protected AndroidInjector<? extends DaggerApplication> applicationInjector() {
        return DaggerAppComponent.builder().application(this).build();
    }

    private void initButterKnife() {
        if (BuildConfig.DEBUG) {
            ButterKnife.setDebug(true);
        }
    }

    private void initRxJava() {
        RxJavaPlugins.setErrorHandler(throwable -> Log.e(getClass().getSimpleName(), throwable.toString()));
    }

    private void initStrictMode() {
        /*if (BuildConfig.DEBUG) {
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                    .detectAll()
                    .penaltyLog()
                    .penaltyDeath()
                    .build());

            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                    .detectAll()
                    .penaltyLog()
                    .penaltyDeath()
                    .build());
        }*/
    }
}
