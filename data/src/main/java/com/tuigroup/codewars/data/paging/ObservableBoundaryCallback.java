package com.tuigroup.codewars.data.paging;

import android.arch.paging.PagedList;

public abstract class ObservableBoundaryCallback<T> extends PagedList.BoundaryCallback<T> {

    public abstract void setBoundaryCallbackRequestListener(BoundaryCallbackRequestListener listener);

    public interface BoundaryCallbackRequestListener {

        void onRequestInProgress(boolean inProgress);

        void onRequestError(Throwable throwable);

        void onAllDataLoaded();
    }
}
