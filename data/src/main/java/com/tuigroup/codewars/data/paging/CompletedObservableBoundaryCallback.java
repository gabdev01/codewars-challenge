package com.tuigroup.codewars.data.paging;

import android.support.annotation.NonNull;

import com.tuigroup.codewars.data.local.CompletedChallengeDao;
import com.tuigroup.codewars.data.local.model.CompletedChallengeEntity;
import com.tuigroup.codewars.data.mapper.CompletedChallengeMapper;
import com.tuigroup.codewars.data.remote.UserRestApi;
import com.tuigroup.codewars.data.remote.model.CompletedChallengeResponse;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class CompletedObservableBoundaryCallback extends ObservableBoundaryCallback<CompletedChallengeEntity> {

    private UserRestApi userRestApi;
    private CompletedChallengeDao completedChallengeDao;

    private String username;
    private boolean isRequestRunning;
    private boolean isAllDataLoaded;

    private BoundaryCallbackRequestListener boundaryCallbackRequestListener;

    @Inject
    public CompletedObservableBoundaryCallback(UserRestApi userRestApi,
                                               CompletedChallengeDao completedChallengeDao) {
        this.userRestApi = userRestApi;
        this.completedChallengeDao = completedChallengeDao;
        this.username = username;
        this.isRequestRunning = false;
        this.isAllDataLoaded = false;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public void setBoundaryCallbackRequestListener(BoundaryCallbackRequestListener listener) {
        boundaryCallbackRequestListener = listener;
    }

    private void performAllDataLoaded() {
        if (boundaryCallbackRequestListener != null) {
            boundaryCallbackRequestListener.onAllDataLoaded();
        }
    }

    private void performRequestInProgress(boolean inProgress) {
        if (boundaryCallbackRequestListener != null) {
            boundaryCallbackRequestListener.onRequestInProgress(inProgress);
        }
    }

    private void performRequestError(Throwable throwable) {
        if (boundaryCallbackRequestListener != null) {
            boundaryCallbackRequestListener.onRequestError(throwable);
        }
    }

    @Override
    public void onZeroItemsLoaded() {
        getAndSaveData(0);
    }

    @Override
    public void onItemAtEndLoaded(@NonNull CompletedChallengeEntity itemAtEnd) {
        getAndSaveData(itemAtEnd.getPageIndexInResponse() + 1);
    }

    private void getAndSaveData(int pageIndex) {
        if (isRequestRunning || isAllDataLoaded) {
            return;
        }
        isRequestRunning = true;
        performRequestInProgress(isRequestRunning);
        getDataFromApi(pageIndex)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSuccess(challengesResponse -> {
                    isRequestRunning = false;
                    performRequestInProgress(isRequestRunning);

                    isAllDataLoaded = (pageIndex + 1) >= challengesResponse.getTotalPages();
                    if (isAllDataLoaded) {
                        performAllDataLoaded();
                    }
                })
                .doOnError(throwable -> {
                    isRequestRunning = false;
                    performRequestInProgress(isRequestRunning);
                    performRequestError(throwable);
                })
                .subscribe();
    }

    private Single<CompletedChallengeResponse> getDataFromApi(int pageIndex) {
        return userRestApi.getCompletedChallenges(username, pageIndex)
                .map(challengesResponse -> {
                            completedChallengeDao.insertAll(CompletedChallengeMapper.mapFromApiToEntity(
                                    challengesResponse.getData(),
                                    username,
                                    pageIndex));
                            return challengesResponse;
                        }
                );
    }
}