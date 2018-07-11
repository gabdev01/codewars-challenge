package codewars.tuigroup.com.codewars.ui.base;

public interface ScopedPresenter<View extends BaseView> {

    void attachView(View view);

    void detachView();

    void subscribe();

    void unsubscribe();
}
