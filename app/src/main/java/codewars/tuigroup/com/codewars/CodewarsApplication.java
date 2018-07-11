package codewars.tuigroup.com.codewars;

import android.app.Application;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.util.Log;

import com.squareup.leakcanary.LeakCanary;
import com.tuigroup.codewars.data.local.CodewarsDatabase;

import butterknife.ButterKnife;
import io.reactivex.plugins.RxJavaPlugins;

public class CodewarsApplication extends Application {

    private CodewarsDatabase codewarsDatabase;

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

        initRoom();
        initButterKnife();
        initRxJava();
        initStrictMode();
    }

    private void initRoom() {
        codewarsDatabase = Room.databaseBuilder(getApplicationContext(),
                CodewarsDatabase.class, "database-codewars").build();
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

    public CodewarsDatabase getDatabase() {
        return codewarsDatabase;
    }
}
