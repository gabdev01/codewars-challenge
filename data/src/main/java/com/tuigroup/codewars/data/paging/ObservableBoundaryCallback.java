package com.tuigroup.codewars.data.paging;

import android.arch.paging.PagedList;

public abstract class ObservableBoundaryCallback<T, V> extends PagedList.BoundaryCallback<T> {

    // TODO Should use RxJava for that
    public abstract void setBoundaryCallbackRequestListener(BoundaryCallbackRequestListener listener);

    public abstract void retryRequest();

    public abstract void setParameter(V parameter);

    public interface BoundaryCallbackRequestListener {

        void onRequestInProgress(boolean inProgress);

        void onRequestError(Throwable throwable);

        void onAllDataLoaded();
    }
}
