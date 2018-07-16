package codewars.tuigroup.com.codewars.ui.base;

public interface ScopedPresenter<View extends BaseView, State extends BaseState> {

    void attachView(View view);

    void attachView(View view, State state);

    State getState();

    void detachView();

    void subscribe();

    void unsubscribe();
}
