package codewars.tuigroup.com.codewars.ui.base;

public interface BaseView<T extends ScopedPresenter> {

    void setPresenter(T presenter);
}
