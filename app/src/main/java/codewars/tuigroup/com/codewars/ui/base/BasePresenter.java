package codewars.tuigroup.com.codewars.ui.base;

import android.support.annotation.CallSuper;

import codewars.tuigroup.com.codewars.ui.util.rx.SchedulerProvider;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

import static com.google.common.base.Preconditions.checkNotNull;

public abstract class BasePresenter<View extends BaseView> implements ScopedPresenter<View> {

    protected SchedulerProvider schedulerProvider;
    protected View view;
    private CompositeDisposable compositeDisposable;
    private CompositeDisposable compositeLifecyleDisposable;

    public BasePresenter(SchedulerProvider schedulerProvider) {
        this.schedulerProvider = schedulerProvider;
    }

    @Override
    @CallSuper
    public void attachView(View view) {
        this.view = checkNotNull(view, "View cannot be null!");
    }

    @Override
    @CallSuper
    public void detachView() {
        clearDisposables();
        view = null;
    }

    @Override
    @CallSuper
    public void subscribe() {
    }

    @Override
    @CallSuper
    public void unsubscribe() {
        clearLifecycleDisposables();
    }

    protected void addDisposable(final Disposable disposable) {
        if (compositeDisposable == null) {
            compositeDisposable = new CompositeDisposable();
        }
        compositeDisposable.add(disposable);
    }

    private void clearDisposables() {
        if (compositeDisposable != null) {
            compositeDisposable.clear();
        }
    }

    protected void addLifecycleDisposable(final Disposable disposable) {
        if (compositeLifecyleDisposable == null) {
            compositeLifecyleDisposable = new CompositeDisposable();
        }
        compositeLifecyleDisposable.add(disposable);
    }

    private void clearLifecycleDisposables() {
        if (compositeLifecyleDisposable != null) {
            compositeLifecyleDisposable.clear();
        }
    }

    protected void logError(final Throwable throwable) {
        // TODO Need to add a logger
    }
}
